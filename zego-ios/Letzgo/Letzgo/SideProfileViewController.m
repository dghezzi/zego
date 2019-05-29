//
//  SideProfileViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 20/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "SideProfileViewController.h"
#import "EditSimpleFieldViewController.h"
#import "EditAddressViewController.h"

@interface SideProfileViewController ()

@end

@implementation SideProfileViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    u = [self loggedUser];
    
    [_backView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(goBack)]];
    _topTitle.font = [self boldFontWithSize:24];
    [[self navigationController] setNavigationBarHidden:YES];
    
    _table.dataSource = self;
    _table.delegate = self;
    
    CGFloat w = self.view.frame.size.width;
    UIView* footer = [[UIView alloc] initWithFrame:CGRectMake(0, 0, w, 135)];
    UIView* blu = [[UIView alloc] initWithFrame:CGRectMake((w-240)/2, 20, 240, 40)];
    blu.tag = 200;
    blu.backgroundColor = BLUFACEBOOK;
    [footer addSubview:blu];
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    [button addTarget:self action:@selector(connectFacebook:)
     forControlEvents:UIControlEventTouchUpInside];
    [button setTitle:NSLocalizedString(@"Collega con Facebook",nil) forState:UIControlStateNormal];
    button.frame = CGRectMake(20, 0, 200, 40.0);
    button.titleLabel.font = [self boldFontWithSize:16];
    button.titleLabel.textColor = [UIColor whiteColor];
    [blu addSubview:button];
    
    UIView* sep = [[UIView alloc] initWithFrame:CGRectMake(0, 80, w, 1)];
    sep.backgroundColor = [UIColor lightGrayColor];
    [footer addSubview:sep];
    
    UIButton* logout = [UIButton buttonWithType:UIButtonTypeCustom];
    [logout setTitle:NSLocalizedString(@"Logout",nil) forState:UIControlStateNormal];
    [logout addTarget:self action:@selector(logoutAction)
     forControlEvents:UIControlEventTouchUpInside];
    logout.frame = CGRectMake(0, 95, w, 25);
    logout.titleLabel.font = [self boldFontWithSize:20];
    [logout setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
    [footer addSubview:logout];
    _table.tableFooterView = footer;
    _profileImgCta.font = [self lightFontWithSize:14];
    
    _profileImg.userInteractionEnabled = YES;
    _profileImgCta.userInteractionEnabled = YES;
    [_profileImg addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self
                                                                              action:@selector(uploadProfileImage)]];
    [_profileImgCta addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(uploadProfileImage)]];
 
    _backView.isAccessibilityElement = YES;
    _backView.accessibilityLabel = @"Indietro";
    _backView.accessibilityHint = @"Indietro";
    _backView.accessibilityTraits = UIAccessibilityTraitButton;
}


- (void) viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    u = [self loggedUser];
    [_table reloadData];
    [self refresh];
    
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 7;
}

-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: (indexPath.row == 4 ? @"mobileCell" : @"editCell")];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier: (indexPath.row == 4 ? @"mobileCell" : @"editCell")];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    
    cell.backgroundColor = [UIColor clearColor];
    cell.contentView.backgroundColor = [UIColor clearColor];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return IS_IPHONE_5 || IS_IPHONE_4_OR_LESS ? 45.0 : 60.0;
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath {
    if(cell) {
        if(indexPath.row == 4) {
            UIImageView* flag = (UIImageView*)[cell.contentView viewWithTag:200];
            UITextField* mob = (UITextField*)[cell.contentView viewWithTag:201];
            UIImageView* pen = (UIImageView*)[cell.contentView viewWithTag:202];
            flag.image = [UIImage imageNamed:[u.country uppercaseString]];
            
            mob.text = [NSString stringWithFormat:@"%@ %@",u.prefix, u.mobile];
            mob.font = [self italicFontWithSize:IS_IPHONE_5 || IS_IPHONE_4_OR_LESS ? 20 : 26];
            mob.enabled = NO;
            pen.hidden = NO;
            pen.isAccessibilityElement = YES;
            pen.accessibilityLabel = @"Modifca";
            pen.accessibilityHint = @"Modifca";
            pen.accessibilityTraits = UIAccessibilityTraitButton;
            
        } else {
            UITextField* field = [cell.contentView viewWithTag:100];
            UIImageView* pen = (UIImageView*)[cell.contentView viewWithTag:105];
            field.font = [self italicFontWithSize:IS_IPHONE_5 || IS_IPHONE_4_OR_LESS ? 20 : 26];
            if(indexPath.row == 0) {
                field.text = u.fname;
                field.enabled = !u.fbid || u.candrive == 0;
                pen.hidden = u.candrive == 1 || (u.fbid && u.fbid.length > 0);
            } else if (indexPath.row == 1) {
                field.text = u.lname;
                field.enabled = !u.fbid || u.candrive == 0;
                pen.hidden = u.candrive == 1 || (u.fbid && u.fbid.length > 0);
            } else if (indexPath.row == 2) {
                field.text = u.email;
            } else if (indexPath.row == 3) {
                field.text = u.wemail;
                field.placeholder = NSLocalizedString(@"Email lavoro",nil);
            } else if (indexPath.row == 5) {
                field.placeholder = NSLocalizedString(@"Indirizzo Casa",nil);
                if(homeAddress) {
                    field.text = [homeAddress addressForComponents:2];
                }
            } else if (indexPath.row == 6) {
                field.placeholder = NSLocalizedString(@"Indirizzo Lavoro",nil);
                if(workAddress) {
                    field.text = [workAddress addressForComponents:2];
                }
            }
            field.enabled = NO;
            pen.isAccessibilityElement = YES;
            pen.accessibilityLabel = @"Modifca";
            pen.accessibilityHint = @"Modifca";
            pen.accessibilityTraits = UIAccessibilityTraitButton;
        }
        
        
    }
    
    UIView* fbbut = [_table.tableFooterView viewWithTag:200];
    if(fbbut) {
        fbbut.hidden = u.fbid && [u.fbid length]>0;
    }
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    idx = indexPath.row;
    
    if (indexPath.row == 0) {
        if((!u.fbid || u.fbid.length == 0) && u.candrive == 0){
            [self performSegueWithIdentifier:@"editFieldSegue" sender:self];
        }
    } else if (indexPath.row == 1) {
       if((!u.fbid || u.fbid.length == 0) && u.candrive == 0){
            [self performSegueWithIdentifier:@"editFieldSegue" sender:self];
        }
    } else if (indexPath.row == 2) {
        [self performSegueWithIdentifier:@"editFieldSegue" sender:self];
        
    } else if (indexPath.row == 3) {
        [self performSegueWithIdentifier:@"editFieldSegue" sender:self];
    } else if (indexPath.row == 4) {
        // edit mobile is disabled
         [self performSegueWithIdentifier:@"editMobileSegue" sender:self];
        /*
        SCLAlertView *alert = [[SCLAlertView alloc] init];
        [alert showNotice:self title:@"Attenzione" subTitle:@"Per modificare il numero di telefono contattaci a support@zegoapp.com."
         closeButtonTitle:@"Ok" duration:0.0f];
         */
        
    } else if (indexPath.row == 5) {
        [self performSegueWithIdentifier:@"editAddressSegue" sender:self];
    } else if (indexPath.row == 6) {
        [self performSegueWithIdentifier:@"editAddressSegue" sender:self];
    }
 
}

-(void) refresh {
    [self startLoading];
    
    if(u.img) {
        [_profileImg setImageWithURL:[NSURL URLWithString:u.img] placeholderImage:[UIImage imageNamed:@"userplaceholder"]];
        _profileImg.contentMode = UIViewContentModeScaleAspectFit;
        _profileImg.layer.cornerRadius = 35;
        _profileImg.layer.masksToBounds = YES;
    }
    
    [[ZegoAPIManager sharedManager] lastAddress:^(NSArray *resp) {
        if(resp) {
            for(Address* a in resp) {
                if([a.type isEqualToString:@"home"]) {
                    homeAddress = a;
                } else if([a.type isEqualToString:@"work"]) {
                    workAddress = a;
                }
            }
            [_table reloadData];
        }
        [self stopLoading];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            [self handleError:error];
        }
        [self stopLoading];
    }ofType:@"all" forUserWithId:[self loggedUser].uid];
    
    
    [[ZegoAPIManager sharedManager] getUser:^(User *resp) {
        if(resp) {
            [self updateUser:resp];
            [_table reloadData];
        }
        [self stopLoading];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            [self handleError:error];
        }
        [self stopLoading];
    } withId:[self loggedUser].uid];
}
-(void) connectFacebook:(UIGestureRecognizer*)sender {
    
    FBSDKLoginManager *login = [[FBSDKLoginManager alloc] init];
    [login
     logInWithReadPermissions: @[@"public_profile",@"email"]
     fromViewController:self
     handler:^(FBSDKLoginManagerLoginResult *result, NSError *error) {
         if (error) {
             NSLog(@"Process error");
             // TODO aggiungi dialog modale
         } else if (result.isCancelled) {
             NSLog(@"Cancelled");
             // TODO aggiungi dialog modale
         } else {
             [self fetchFacebookUserInfoWithHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
                 if(error || !result) {
                     
                 }
                 else {
                     NSLog(@"resultis:%@",result);
                     
                     // first update local cache
                     User* us = [self loggedUser];
                     if(!us) {
                         us = [[User alloc] init];
                     }
                     us.fbid = [result valueForKey:@"id"];
                     us.email = [result valueForKey:@"email"];
                     us.fname = [result valueForKey:@"first_name"];
                     us.lname = [result valueForKey:@"last_name"];
                     us.gender = [result valueForKey:@"gender"];
                     us.birthdate = [result valueForKey:@"birthday"];
                     [self updateUser:us];
                     [[ZegoAPIManager sharedManager] update:^(User *resp) {
                         if(resp) {
                             [self updateUser:resp];
                             u = resp;
                             
                             [_table reloadData];
                             
                             UIView* fbbut = [_table.tableFooterView viewWithTag:200];
                             if(fbbut) {
                                 fbbut.hidden = u.fbid && [u.fbid length]>0;
                             }
                             
                             if(u.img) {
                                 [_profileImg setImageWithURL:[NSURL URLWithString:u.img] placeholderImage:[UIImage imageNamed:@"userplaceholder"]];
                                 _profileImg.contentMode = UIViewContentModeScaleAspectFit;
                                 _profileImg.layer.cornerRadius = 35;
                                 _profileImg.layer.masksToBounds = YES;
                             }
                         }
                     } failure:^(RKObjectRequestOperation *operation, NSError *error) {
                         if(error) {
                            ErrorResponse* msg = [self handleError:error];
                             SCLAlertView *alert = [[SCLAlertView alloc] init];
                             [alert showNotice:self title:msg.title subTitle:msg.msg
                                    closeButtonTitle:@"Ok" duration:0.0f];
                         }
                     } withRequest:us];
                     
                 }
                 
             }];
             
         }
     }];

}


- (void) uploadProfileImage {
    [self performSegueWithIdentifier:@"selectPicSegue" sender:self];

}
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {

    if([segue.identifier isEqualToString:@"selectPicSegue"]) {
        
    } else {
    if(idx>=0 && idx<=3) {
        ((EditSimpleFieldViewController*)[segue destinationViewController]).field = idx;
    } else if(idx>=5) {
        EditAddressViewController* evc = ((EditAddressViewController*)[segue destinationViewController]);
        evc.previous = idx == 5 ? homeAddress.address : workAddress.address;
        evc.type = idx == 5 ? @"home" : @"work";
    }
    }
}


@end
