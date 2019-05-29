//
//  StripeAddCardViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "StripeAddCardViewController.h"

@interface StripeAddCardViewController ()

@end

@implementation StripeAddCardViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [_backView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(goBack)]];
    
    _topTitle.font = [self boldFontWithSize:24];
    _topMessage.font = [self lightFontWithSize:18];
    _form.delegate = self;
    _next.titleLabel.font = [self boldFontWithSize:22];
    _skip.titleLabel.font = [self boldFontWithSize:22];
    _error.hidden = YES;
    _error.font = [self italicFontWithSize:14];
    _error.minimumScaleFactor = 10/14.f;
    self.view.userInteractionEnabled = YES;
    [self.view addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(resignKey:)]];
    
    if(_map || _ccard) {
        _skip.hidden = YES;
    } else {
        _skip.hidden = NO;
    }
    
    _backView.isAccessibilityElement = YES;
    _backView.accessibilityLabel = @"Indietro";
    _backView.accessibilityHint = @"Indietro";
    _backView.accessibilityTraits = UIAccessibilityTraitButton;
}

-(void)resignKey:(UIGestureRecognizer*)sender {
    [_form resignFirstResponder];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)nextAction:(id)sender {
    
    [self startLoading];
    [[STPAPIClient sharedClient] createTokenWithCard:_form.cardParams completion:^(STPToken *token, NSError *error) {
        if (error) {
            // TODO show the error, maybe by presenting an alert to the user
            NSString* err = [error.userInfo objectForKey:@"com.stripe.lib:ErrorMessageKey"];
            [self addPaymentFailedWithMessage:err];
            [self stopLoading];
        } else {
            
            StripeSaveRequest* rr = [[StripeSaveRequest alloc] init];
            rr.token = token.tokenId;
            rr.uid = [self loggedUser].uid;
            [[ZegoAPIManager sharedManager]addStripeCard :^(User *resp) {
                if(resp) {
                    [self addPaymentSourceSucceededForUser:resp];
                } else {
                    [self stopLoading];
                }
            } failure:^(RKObjectRequestOperation *operation, NSError *error) {
                if(error) {
                    ErrorResponse* e = [self handleError:error];
                    if(e) {
                        [self addPaymentFailedWithMessage:e.msg];
                    }
                }
                [self stopLoading];
            } withRequest:rr];
        }
    }];
    
    
    
}

-(void) addPaymentSourceSucceededForUser:(User*)u{
    [self updateUser:u];
    if(_map) {
        [_map refreshUserCards];
        [self stopLoading];
        [self goBack];
    } else if (_ccard) {
        [self stopLoading];
        [self goBack];
    }else {
        [self stopLoading];
        [self startPollingWithUser:u];
        [self performSegueWithIdentifier:@"signupToMapSegue" sender:self];
    }
}

-(void) addPaymentFailedWithMessage:(NSString*)message {
    _error.hidden = NO;
    _error.textColor = [UIColor redColor];
    _error.text = message;
}

-(IBAction)skipAction:(id)sender {
    [self performSegueWithIdentifier:@"signupToMapSegue" sender:self];
}

- (void)paymentCardTextFieldDidChange:(nonnull STPPaymentCardTextField *)textField {
    
    if(textField.valid) {        
        _next.backgroundColor = ZEGOGREEN;
        _next.enabled = YES;
        [textField resignFirstResponder];
    } else {
        
        _next.backgroundColor =ZEGODARKGREY;
        _next.enabled = NO;
    }
    _error.hidden = YES;
}


#pragma mark - Navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    
    
}

@end
