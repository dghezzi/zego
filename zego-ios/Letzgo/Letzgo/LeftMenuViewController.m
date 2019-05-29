//
//  LeftMenuViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 16/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "LeftMenuViewController.h"
#import "CreditCardViewController.h"
#import "PromoListViewController.h"
#import "RideListViewController.h"
#import "BecomeDriverViewController.h"


@interface LeftMenuViewController ()

@end

@implementation LeftMenuViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [[self navigationController] setNavigationBarHidden:YES];
    
}

- (void) viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    User* u = [self loggedUser];
    
    _menuwidth.constant = self.view.frame.size.width * 0.85;
    _container.clipsToBounds = NO;
    _container.layer.shadowColor = [[UIColor blackColor] CGColor];
    _container.layer.shadowOffset = CGSizeMake(3,0);
    _container.layer.shadowOpacity = 0.5;
    _container.layer.cornerRadius = 3;
    
    UIFont* mf = [self lightFontWithSize:22];
    _paymentLabel.font = mf;
    _promoLabel.font = mf;
    _profileLabel.font = mf;
    _infoLabel.font = mf;
    _historyLabel.font = mf;
    _switchModeLabel.font = mf;
    
    _paymentLabel.textColor = ZEGODARKGREY;
    _promoLabel.textColor = ZEGODARKGREY;
    _profileLabel.textColor = ZEGODARKGREY;
    _infoLabel.textColor = ZEGODARKGREY;
    _historyLabel.textColor = ZEGODARKGREY;
    _switchModeLabel.textColor = ZEGODARKGREY;
    
    _enableDriverButton.titleLabel.font = [self boldFontWithSize:22];
    _fullname.font = [self boldFontWithSize:24];
    _fullname.minimumScaleFactor = 16/24.f;
    _mobile.font = [self lightFontWithSize:18];
    _mobile.minimumScaleFactor = 12/18.f;
    _refcode.font = [self boldFontWithSize:20];
    _refcode.minimumScaleFactor = 10/20.f;
    _refcode.layer.masksToBounds = YES;
    _refcode.layer.cornerRadius = 13;
    _tapToShare.font = [self italicFontWithSize:12];

    _userimg.layer.masksToBounds = YES;
    _userimg.layer.cornerRadius = 40;
    _userimg.image = [UIImage imageNamed:@"userplaceholder"];
    _userimg.contentMode = UIViewContentModeScaleAspectFill;
    if(u.img){
        [_userimg setImageWithURL:[NSURL URLWithString:u.img] placeholderImage:[UIImage imageNamed:@"userplaceholder"]];
    }
    
    _userimg.isAccessibilityElement = YES;
    _userimg.accessibilityLabel = @"Indietro";
    _userimg.accessibilityHint = @"Indietro";
    _userimg.accessibilityTraits = UIAccessibilityTraitButton;
    _userimg.userInteractionEnabled = YES;
    [_userimg addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(showCenter)]];
    
    
    
    if(IS_IPHONE_5 || IS_IPHONE_4_OR_LESS) {
        _profileHeight.constant = 48.0;
        _historyHeight.constant = 48.0;
        _switchHeight.constant = 48.0;
        _infoHeight.constant = 48.0;
        _paymentHeight.constant = 48.0;
        _promoHeight.constant = 48.0;
    }
    
    _scrollMenu.contentSize = CGSizeMake(_scrollMenu.frame.size.width, self.view.frame.size.height-170);
    _scrollMenu.scrollEnabled = IS_IPHONE_4_OR_LESS;
    
    
    _fullname.text = [NSString stringWithFormat:@"%@ %@",u.fname, u.lname];
    _mobile.text  = [NSString stringWithFormat:@"%@ %@",u.prefix, u.mobile];
    _refcode.text = NSLocalizedString(@"Consiglia e ricevi 5€", nil);
    _tapToShare.text = u.refcode;
    
    [_profileView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(menuTapped:)]];
    [_paymentView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(menuTapped:)]];
    [_historyView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(menuTapped:)]];
    [_promoView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(menuTapped:)]];
    [_infoView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(menuTapped:)]];
    [_switchModeView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(menuTapped:)]];
    
    
    _profileView.isAccessibilityElement = YES;
    _profileView.accessibilityLabel = @"Profilo";
    _profileView.accessibilityHint = @"Profilo";
    _profileView.accessibilityTraits = UIAccessibilityTraitButton;
    
    _paymentView.isAccessibilityElement = YES;
    _paymentView.accessibilityLabel = @"Metodi di pagamento";
    _paymentView.accessibilityHint = @"Metodi di pagamento";
    _paymentView.accessibilityTraits = UIAccessibilityTraitButton;
    
    _historyView.isAccessibilityElement = YES;
    _historyView.accessibilityLabel = @"I tuoi passaggi";
    _historyView.accessibilityHint = @"I tuoi passaggi";
    _historyView.accessibilityTraits = UIAccessibilityTraitButton;
    
    _promoView.isAccessibilityElement = YES;
    _promoView.accessibilityLabel = @"Promo";
    _promoView.accessibilityHint = @"Promo";
    _promoView.accessibilityTraits = UIAccessibilityTraitButton;
    
    _infoView.isAccessibilityElement = YES;
    _infoView.accessibilityLabel = @"Info";
    _infoView.accessibilityHint = @"Info";
    _infoView.accessibilityTraits = UIAccessibilityTraitButton;
    
    _switchModeView.isAccessibilityElement = YES;
    _switchModeView.accessibilityLabel = @"Offri passaggi";
    _switchModeView.accessibilityHint = @"Offri passaggi";
    _switchModeView.accessibilityTraits = UIAccessibilityTraitButton;
    
    _tapToShare.userInteractionEnabled = YES;
    _refcode.userInteractionEnabled = YES;
    
    [_tapToShare addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(shareAction)]];
    [_refcode addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(shareAction)]];
    
    UISwipeGestureRecognizer* left = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(showCenter)];
    left.direction = UISwipeGestureRecognizerDirectionLeft;
    [self.view addGestureRecognizer:left];
    
    _base.userInteractionEnabled = YES;
    [_base addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(showCenter)]];
    
    if([self loggedUser].candrive == 1) {
        if([self loggedUser].davg > 0){
            _mobile.text = [NSString stringWithFormat:@"Rating: %.2f",[self loggedUser].davg];
        } else {
            _mobile.hidden = YES;
        }
        _switchModeView.hidden = NO;
                [_enableDriverButton setTitle:NSLocalizedString(@"Area Driver",nil) forState:UIControlStateNormal];
    } else {
        if([self loggedUser].pavg > 0){
            _mobile.text = [NSString stringWithFormat:@"Rating: %.2f",[self loggedUser].pavg];
        } else {
            _mobile.hidden = YES;
        }
        _switchModeView.hidden = YES;
        [_enableDriverButton setTitle:NSLocalizedString(@"Diventa Driver",nil) forState:UIControlStateNormal];
    }
    
    _switchImage.image = [[self loggedUser].umode isEqualToString:@"driver"] ? [UIImage imageNamed:@"switchon"] : [UIImage imageNamed:@"switchoff"];

    
}

- (void)menuTapped:(UIGestureRecognizer*)sender {
    if([sender.view isEqual:_profileView]) {
        [self performSegueWithIdentifier:@"profileSegue" sender:nil];
    } else if([sender.view isEqual:_paymentView]) {
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
        CreditCardViewController * vc = (CreditCardViewController*)[storyboard instantiateViewControllerWithIdentifier:@"cardListVCID"];
        [self.navigationController pushViewController:vc animated:YES];
    } else if([sender.view isEqual:_promoView]) {
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
        PromoListViewController * vc = (PromoListViewController*)[storyboard instantiateViewControllerWithIdentifier:@"promoListVCID"];
        [self.navigationController pushViewController:vc animated:YES];
       
        
    } else if([sender.view isEqual:_historyView]) {
      
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
        RideListViewController * vc = (RideListViewController*)[storyboard instantiateViewControllerWithIdentifier:@"rideListVCID"];
        [self.navigationController pushViewController:vc animated:YES];
        
    } else if([sender.view isEqual:_infoView]) {
        [self performSegueWithIdentifier:@"aboutSegue" sender:nil];
        
    } else if([sender.view isEqual:_switchModeView]) {
        
        if([self loggedUser].rtstatus % 100 == 0) {
            NSString* newMode = @"";
            if([[self loggedUser].umode isEqualToString:@"driver"]) {
                newMode = @"rider";
            } else {
                newMode = @"driver";
            }
            [self startLoading];
            
          [[ZegoAPIManager sharedManager] userChangeMode:^(User *resp) {
              if(resp) {
                  [self updateUser:resp];
                  if([resp.umode isEqualToString:@"driver"]){
                      _switchImage.image = [UIImage imageNamed:@"switchon"];
                  } else {
                      _switchImage.image = [UIImage imageNamed:@"switchoff"];
                  }
                  [[LocationManager sharedManager] updateNow];
              }
              [self stopLoading];
          } failure:^(RKObjectRequestOperation *operation, NSError *error) {
              ErrorResponse* re = [self handleError:error];
              if(re) {
                  [self.view makeToast:re.msg];
              }
              [self stopLoading];
          } to:newMode withUserData:[self loggedUser]];
        } else {
            [self.view makeToast:NSLocalizedString(@"Non puoi cambiare modalità durante una ride.",nil)];
        }
    }
}


-(void) shareAction {
    NSString* code = [NSString stringWithFormat:NSLocalizedString(@"Con Zego puoi spostarti in città con un passaggio. Io lo uso già, scarica l’app anche tu su http://www.zegoapp.com ! Con il codice %@ ricevi 5€ di credito.",nil), [[self loggedUser].refcode uppercaseString]];
    NSArray *items   = [NSArray arrayWithObjects:code, nil];
    
    UIActivityViewController *shareChooser = [[UIActivityViewController alloc]
                                              initWithActivityItems:items applicationActivities:nil];
    [shareChooser setExcludedActivityTypes: @[UIActivityTypeAssignToContact,
                                              UIActivityTypePrint,
                                              UIActivityTypeSaveToCameraRoll]];
    [self presentViewController:shareChooser animated:YES completion:nil];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void) showCenter {
    [[self sidePanelController] showCenterPanelAnimated:YES];
}

-(IBAction)bottomButtonTapped:(id)sender {
    [self startLoading];
    if([self loggedUser].candrive == 1) {
        NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"http://v2.zegoapp.com/gateway.php?zt=%@",[self loggedUser].zegotoken]];
        if ([[UIApplication sharedApplication] canOpenURL:url]) {
            [[UIApplication sharedApplication] openURL:url];
        }
        [self stopLoading];
    }  else {
        [[ZegoAPIManager sharedManager] getUserDriverData:^(DriverData *resp) {
            [self stopLoading];
            UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
            BecomeDriverViewController * vc = (BecomeDriverViewController*)[storyboard instantiateViewControllerWithIdentifier:@"becomeDriverVCID"];
            vc.driverData = resp;
            [self.navigationController pushViewController:vc animated:YES];
        } failure:^(RKObjectRequestOperation *operation, NSError *error) {
            ErrorResponse* re = [self handleError:error];
            if(re) {
                [self.view makeToast:re.msg];
            }
            [self stopLoading];
        } forUserId:[self loggedUser].uid];
    }
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
