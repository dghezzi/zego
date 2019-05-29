//
//  PromoListViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 17/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "PromoListViewController.h"
#import "PromoCodeViewController.h"

@interface PromoListViewController ()

@end

@implementation PromoListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [_backView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(goBack)]];
    _topTitle.font = [self boldFontWithSize:24];
    [[self navigationController] setNavigationBarHidden:YES];
    
    _table.dataSource = self;
    _table.delegate = self;
    _table.separatorStyle = UITableViewCellSeparatorStyleNone;
    
    _backView.isAccessibilityElement = YES;
    _backView.accessibilityLabel = @"Indietro";
    _backView.accessibilityHint = @"Indietro";
    _backView.accessibilityTraits = UIAccessibilityTraitButton;
    _promoDisclaimer.hidden = YES;
    _promoDisclaimer.text = NSLocalizedString(@"I crediti sono validi per i pagamenti con carta di credito o carta prepagata", @"");
    
}
-(void)viewWillAppear:(BOOL)animated {
    [self startLoading];
    _placeHolder.hidden = YES;
    [[ZegoAPIManager sharedManager] userPromo:^(NSArray *resp) {
        if(resp) {
            promos = [[NSMutableArray alloc] initWithArray:resp];
            [_table reloadData];
        }
        _table.hidden = !resp || [resp count] == 0;
        _promoDisclaimer.hidden = _table.hidden;
        _placeHolder.hidden = !_table.hidden;
        [self stopLoading];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            ErrorResponse* re = [self handleError:error];
            if(re) {
                [self.view makeToast:re.msg];
            }
        }
        [self stopLoading];
    } forUserWithId:[self loggedUser].uid];
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [promos count];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 100.f;
}

-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: @"promoCell"];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"promoCell"];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
    }
    
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    SCLAlertView *alert = [[SCLAlertView alloc] init];
    alert.shouldDismissOnTapOutside = YES;
    UserPromo* pro = [promos objectAtIndex:indexPath.row];
    
    [alert showNotice:self title:@"" subTitle:pro.promo.promodesc closeButtonTitle:@"Ok" duration:0.0f];
    
    // Using Block
    [alert alertIsDismissed:^{
        
    }];
    
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath {
    if(cell) {
        UserPromo* pro = [promos objectAtIndex:indexPath.row];
        UIView* box = [(UIView*)cell.contentView viewWithTag:200];
        box.layer.cornerRadius = 3;
        box.layer.shadowColor = [UIColor blackColor].CGColor;
        box.layer.shadowOffset = CGSizeMake(4, 4);
        box.layer.shadowOpacity = 0.9;
        box.layer.shadowRadius = 5;
        box.layer.masksToBounds = YES;
        UILabel* title = [(UILabel*)cell.contentView viewWithTag:100];
        UILabel* code = [(UILabel*)cell.contentView viewWithTag:101];
        UILabel* expiry = [(UILabel*)cell.contentView viewWithTag:102];
        UILabel* value = [(UILabel*)cell.contentView viewWithTag:103];
        UILabel* details = [(UILabel*)cell.contentView viewWithTag:104];
        
        title.text = pro.promo.promotitle;
        code.text = pro.promo.code;
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setDateFormat:@"dd-MM-yyyy"];
        NSDate *epochNSDate = [[NSDate alloc] initWithTimeIntervalSince1970:[pro.expirydate doubleValue]];
        expiry.text = [NSString stringWithFormat:NSLocalizedString(@"Scade il %@",nil),[dateFormatter stringFromDate:epochNSDate]];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        if([pro.promo.type isEqualToString:@"euro"]) {
            value.text = [NSString stringWithFormat:@"%.2f €",(long)pro.promo.value/100.f];
            details.text = @"sulla prossima ride";
            // richiesta svinella
            details.hidden = YES;
        } else if([pro.promo.type isEqualToString:@"percent"]) {
            value.text = [NSString stringWithFormat:@"-%ld %%",(long)pro.promo.value];
            details.text = @"sconto percentuale";
            // richiesta svinella
            details.hidden = YES;
        } else if([pro.promo.type isEqualToString:@"wallet"]) {
            value.text = [NSString stringWithFormat:@"%.2f €",(long)pro.promo.value/100.f];
            details.text = NSLocalizedString(@"Crediti Zego",nil);
        } else if([pro.promo.type isEqualToString:@"freeride"]) {
            value.text = [NSString stringWithFormat:@"%ld",(long)pro.promo.value];
            details.text = (long)pro.promo.value > 1 ?  NSLocalizedString(@"Corse Gratuite",nil) : NSLocalizedString(@"Corsa Gratuita",nil);
        }
        
        
    }
}




- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)addPromo:(id)sender {
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
    PromoCodeViewController * vc = (PromoCodeViewController*)
    [storyboard instantiateViewControllerWithIdentifier:@"promoVCID"];
    vc.promoList = self;
    [self.navigationController pushViewController:vc animated:YES];
}


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
