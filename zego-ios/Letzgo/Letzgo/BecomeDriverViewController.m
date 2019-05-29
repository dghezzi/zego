//
//  BecomeDriverViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 22/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "BecomeDriverViewController.h"

@interface BecomeDriverViewController ()

@end

@implementation BecomeDriverViewController

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
    
}

-(void)refreshVc {
    _table.hidden = _driverData;
    _placeHolder.hidden = !_table.hidden;
    _uploadDocument.hidden = !_table.hidden;
    _cta.hidden = _table.hidden;
    if(_driverData) {
    switch (_driverData.status) {
        case 1:
        {
            _placeHolder.text = NSLocalizedString(@"Completa il tuo profilo in pochi passi e attendi la nostra email di attivazione del profilo DRIVER",nil);
            break;
        }
        case 2:
        {
            _placeHolder.text =NSLocalizedString(@"La tua richiesta è stata approvata.",nil);
            break;
        }
        case 3:
        {
            _placeHolder.text = NSLocalizedString(@"La tua richiesta non è stata approvata.",nil);
            break;
        }
        case 4:
        {
            _placeHolder.text = NSLocalizedString(@"Affinchè la tua richiesta venga approvata devi fornire tutti i documenti necessari.",nil);
            break;
        }
        case 5:
        {
            _placeHolder.text = NSLocalizedString(@"Il nostro staff sta valutando i documenti che hai inviato.",nil);
            break;
        }
        case 6:
        {
            _placeHolder.text = NSLocalizedString(@"I tuoi documenti sono scaduti. Aggiornali per riattivare il tuo profilo.",nil);
            break;
        }
        default:
        {
            break;
        }
    }}
    else {
        [[ZegoAPIManager sharedManager] getAllAreas:^(NSArray *resp) {
            if(resp) {
                areas = [NSMutableArray arrayWithArray:resp];
                [_table reloadData];
                _table.hidden = NO;
                _cta.hidden = _table.hidden;
                _placeHolder.hidden = !_table.hidden;
                _uploadDocument.hidden = !_table.hidden;
            }
        } failure:^(RKObjectRequestOperation *operation, NSError *error) {
            // DO NOTHING
        }];
    }
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [areas count];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 50.f;
}

-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: @"areaCell"];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"areaCell"];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
    }
    
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    Area* a = [areas objectAtIndex:indexPath.row];
    SCLAlertView *alert = [[SCLAlertView alloc] init];
    alert.shouldDismissOnTapOutside = YES;
    [alert addButton:@"Invia" actionBlock:^(void) {
        _driverData = [[DriverData alloc] init];
        _driverData.status = 1;
        _driverData.uid = [self loggedUser].uid;
        _driverData.area = a.name;
        [self startLoading];
        [[ZegoAPIManager sharedManager] createDriverData:^(DriverData *resp) {
            if(resp) {
                _driverData = resp;
                [self refreshVc];
            }
            [self stopLoading];
        } failure:^(RKObjectRequestOperation *operation, NSError *error) {
            ErrorResponse* re = [self handleError:error];
            if(re) {
                [self.view makeToast:re.msg];
            }
            [self stopLoading];
        } withData:_driverData];
    }];
    
    NSString* old = [NSString stringWithFormat:NSLocalizedString(@"Vuoi inviare una richiesta per diventare driver a %@?",nil),a.name];
    
    [alert showNotice:self title:NSLocalizedString(@"Diventa Driver",nil) subTitle:old closeButtonTitle:NSLocalizedString(@"Annulla",nil) duration:0.0f];
    
    
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath {
    if(cell) {
        Area* a = [areas objectAtIndex:indexPath.row];
        UILabel* l = (UILabel*)[cell.contentView viewWithTag:100];
        l.text = a.name;        
    }
}

-(IBAction)goToWebArea:(id)sender {
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"http://v2.zegoapp.com/gateway.php?zt=%@",[self loggedUser].zegotoken]];
    if ([[UIApplication sharedApplication] canOpenURL:url]) {
        [[UIApplication sharedApplication] openURL:url];
    }
}

-(void) viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self refreshVc];
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
