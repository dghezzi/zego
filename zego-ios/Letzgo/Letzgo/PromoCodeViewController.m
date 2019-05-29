//
//  PromoCodeViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "PromoCodeViewController.h"

@interface PromoCodeViewController ()

@end

@implementation PromoCodeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [_backView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(goBack)]];
    
    _topTitle.font = [self boldFontWithSize:24];
    _promoCode.font = [self italicFontWithSize:26];
    _topMessage.font = [self lightFontWithSize:18];
    _next.titleLabel.font = [self boldFontWithSize:22];
    _skip.titleLabel.font = [self boldFontWithSize:22];
    _error.hidden = YES;
    _error.font = [self italicFontWithSize:14];
    _error.minimumScaleFactor = 10/14.f;
    _promoCode.delegate = self;
     BOOL acti = _promoCode.text.length > 0;
    _next.backgroundColor = acti ? ZEGOGREEN : ZEGODARKGREY;
    _next.enabled = acti;
    [_promoCode addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    
    if(IS_IPHONE_5 || IS_IPHONE_4_OR_LESS) {
         _promoCode.font = [self italicFontWithSize:22];
    }
    
    if(_promoList) {
        _skip.hidden = YES;
        _topCta.text = NSLocalizedString(@"Hai un codice promozionale?",nil);
        [_next setTitle:NSLocalizedString(@"Aggiungi",nil) forState:UIControlStateNormal];
    }
    
    _backView.hidden = !_promoList;
    
    _backView.isAccessibilityElement = YES;
    _backView.accessibilityLabel = @"Indietro";
    _backView.accessibilityHint = @"Indietro";
    _backView.accessibilityTraits = UIAccessibilityTraitButton;
}

-(void) textFieldDidChange:(UITextField*)sender {
    _promoCode.text = [_promoCode.text uppercaseString];
    _error.hidden = YES;
    BOOL acti = _promoCode.text.length > 0;
    _next.backgroundColor = acti ? ZEGOGREEN : ZEGODARKGREY;
    _next.enabled = acti;
}

-(BOOL) textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)nextAction:(id)sender {
    
    
    if(_promoList) {
        [self startLoading];
        RedeemRequest* req = [[RedeemRequest alloc] init];
        req.code = _promoCode.text;
        req.uid = [self loggedUser].uid;
        
        [[ZegoAPIManager sharedManager] redeemPromo:^(NSArray *resp) {
            if(resp) {
                [[self navigationController] popViewControllerAnimated:YES];
            }
            [self stopLoading];
        } failure:^(RKObjectRequestOperation *operation, NSError *error) {
            ErrorResponse* re = [self handleError:error];
            if(re) {
                [self.view makeToast:re.msg duration:3 position:CSToastPositionCenter];
            }
            [self stopLoading];
        } withData:req];
        
    }
    else {
    ReferralRequest* rr = [[ReferralRequest alloc] init];
    rr.referral = _promoCode.text;
    rr.uid = [self loggedUser].uid;
    [self startLoading];
    [[ZegoAPIManager sharedManager]validateReferral :^(User *resp) {
        if(resp) {
            [self promoRedeemedCorrectlyForUser:resp];
        }
        [self stopLoading];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            ErrorResponse* e = [self handleError:error];
            if(e) {
                [self promoRedeemFailedWithMessage:e.msg];
            }
        }
        [self stopLoading];
    } withRequest:rr];
    }
}

-(IBAction)skipAction:(id)sender {
    [self performSegueWithIdentifier:@"promoToStripeSegue" sender:self];
}

-(void) promoRedeemFailedWithMessage:(NSString*)msg {

    _error.text = msg;
    _error.textColor = [UIColor redColor];
    _error.hidden = NO;
}

-(void) promoRedeemedCorrectlyForUser:(User*)u {
    [self updateUser:u];
    [self performSegueWithIdentifier:@"promoToStripeSegue" sender:self];
}
#pragma mark - Navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    
    
}

@end
