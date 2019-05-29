//
//  CreditCardViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 11/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "CreditCardViewController.h"
#import "StripeAddCardViewController.h"

@interface CreditCardViewController ()

@end

@implementation CreditCardViewController

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
    
    _cashLabel.text = NSLocalizedString(@"Utilizza i contanti", @"");
    [_cashView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(cashtap:)]];
    _cashActive.image = [_method isEqualToString:@"card" ] ? [UIImage imageNamed:@"card_off"] : [UIImage imageNamed:@"card_on"];
    _payDebtNow.hidden = YES;
    _payDebtLabel1.hidden = YES;
    _payDebtLabel2.hidden = YES;
    
}

-(void)cashtap:(UIGestureRecognizer*)sender {
    _cashActive.image = [UIImage imageNamed:@"card_on"];
    _method = @"cash";
    [_table reloadData];
    [_map useCash];
}

-(void)viewWillAppear:(BOOL)animated {
    _placeHolder.hidden = YES;
    [[ZegoAPIManager sharedManager] userCards:^(NSArray *resp) {
        cards = [[NSMutableArray alloc] initWithArray:resp];
        [_table reloadData];
        _table.hidden = !resp || [resp count] == 0;
        _placeHolder.hidden = YES;// !_table.hidden;
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
    } forUserWithId:[self loggedUser].uid];
    [self checkUserDebt];
}


-(void)checkUserDebt {
    [[ZegoAPIManager sharedManager] getUser:^(User *resp) {
        if(resp) {
            [self updateUser:resp];
            _payDebtNow.hidden = resp.debt == 0;
            _payDebtLabel1.hidden = resp.debt == 0;
            _payDebtLabel2.hidden = resp.debt == 0;
            
            if(resp.debt > 0) {
                _payDebtLabel2.text = [NSString stringWithFormat:NSLocalizedString(@"Pagamento in sospeso %.2f €", nil),resp.debt/100.0];
                _payDebtLabel1.text = NSLocalizedString(@"Verifica la carta e poi premi sotto per sbloccare il pagamento", nil);
                [_payDebtNow setTitle:NSLocalizedString(@"PAGA ORA", nil) forState:UIControlStateNormal];
            }
            
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        
    } withId:[self loggedUser].uid];
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [cards count];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 100.f;
}

-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: @"cardCell"];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cardCell"];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;

    }
    
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [self startLoading];
     _method = @"card";
    StripeCard* sc = [cards objectAtIndex:indexPath.row];
    [[ZegoAPIManager sharedManager] makeCardDefault:^(NSArray *resp) {
        cards = [NSMutableArray arrayWithArray:resp];
        [_table reloadData];
        [self stopLoading];
        if(_map){
            [_map refreshUserCards];
        }
        [self checkUserDebt];
        _cashActive.image = [UIImage imageNamed:@"card_off"];

    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        [self stopLoading];
    } forUser:[self loggedUser].uid withData:sc];
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath {
    if(cell) {
        StripeCard* sc = [cards objectAtIndex:indexPath.row];
        UIView* box = [(UIView*)cell.contentView viewWithTag:200];
        box.layer.cornerRadius = 3;
        box.layer.shadowColor = [UIColor blackColor].CGColor;
        box.layer.shadowOffset = CGSizeMake(4, 4);
        box.layer.shadowOpacity = 0.9;
        box.layer.shadowRadius = 5;
        box.layer.masksToBounds = YES;
        UILabel* brand = [(UILabel*)cell.contentView viewWithTag:101];
        UIImageView* logo = [(UIImageView*)cell.contentView viewWithTag:100];
        UILabel* last4 = [(UILabel*)cell.contentView viewWithTag:102];
        UILabel* topExp = [(UILabel*)cell.contentView viewWithTag:103];
        UILabel* expiry = [(UILabel*)cell.contentView viewWithTag:104];
        UIImageView* dot = [(UIImageView*)cell.contentView viewWithTag:105];
        brand.text = [sc.brand isEqualToString:@"American Express"] ? @"Amex" : sc.brand;
        NSString* name = [NSString stringWithFormat:@"%@",[sc.brand stringByReplacingOccurrencesOfString:@" " withString:@"_"]];
        logo.image = [UIImage imageNamed:[name lowercaseString]];
        last4.text = [NSString stringWithFormat:@"**** %@",sc.lastdigit];
        expiry.text = [NSString stringWithFormat:@"%02d/%d",sc.expmonth,sc.expyear];
        expiry.font = [self italicFontWithSize:14];
        topExp.font = [self boldFontWithSize:14];
        dot.image = [UIImage imageNamed:((sc.preferred == 1 && ![_method isEqualToString:@"cash"])? @"card_on" : @"card_off")];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}


// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [self startLoading];
        StripeCard* sc = [cards objectAtIndex:indexPath.row];
        [[ZegoAPIManager sharedManager] deleteUserCard:^(NSArray *resp) {
            cards = [NSMutableArray arrayWithArray:resp];
            [_table reloadData];
            [self stopLoading];
            if(_map){
                [_map refreshUserCards];
            }
            [self checkUserDebt];
        } failure:^(RKObjectRequestOperation *operation, NSError *error) {
            [self stopLoading];
        } forUser:[self loggedUser].uid withData:sc];
    }
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)addCard:(id)sender {
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
    StripeAddCardViewController * vc = (StripeAddCardViewController*)[storyboard instantiateViewControllerWithIdentifier:@"stripeVCID"];
    vc.ccard = self;
    vc.map = _map;
    [self.navigationController pushViewController:vc animated:YES];
}

-(IBAction)payDebt:(id)sender {
    [self startLoading];
    User* u = [self loggedUser];
    [[ZegoAPIManager sharedManager] paydebt:^(User *resp) {
        if(resp) {
            [self updateUser:resp];
            _payDebtNow.hidden = resp.debt == 0;
            _payDebtLabel1.hidden = resp.debt == 0;
            _payDebtLabel2.hidden = resp.debt == 0;
        }
        [self stopLoading];
        
        [self.view makeToast:NSLocalizedString(@"Pagamento effettuato", nil)];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            ErrorResponse* re = [self handleError:error];
            if(re) {
                
            }
        }
         [self.view makeToast:NSLocalizedString(@"Pagamento non riuscito. Verifica con la tua banca", nil)];
        [self stopLoading];
    } withRequest:u];
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
