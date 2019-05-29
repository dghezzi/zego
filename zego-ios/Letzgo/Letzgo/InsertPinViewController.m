//
//  InsertPinViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "InsertPinViewController.h"

@interface InsertPinViewController ()

@end

@implementation InsertPinViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [_backView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(goBack)]];
    
    cidx = 0;
    digitArray = [[NSMutableArray alloc] init];
    _firstDigit.text = @"";
    [digitArray addObject:_firstDigit];
    _secondDigit.text = @"";
    [digitArray addObject:_secondDigit];
    _thirdDigit.text = @"";
    [digitArray addObject:_thirdDigit];
    _fourthDigit.text = @"";
    [digitArray addObject:_fourthDigit];
    
    
    _smsSentText.font = [self lightFontWithSize:16];
    _smsSentText.text = [NSString stringWithFormat:NSLocalizedString(@"Inviato un SMS a %@.\nDigita il PIN.",nil),_currentMobile];
    
    _topTitle.font = [self boldFontWithSize:24];
    _sendAgain.titleLabel.font = [self boldFontWithSize:20];
    _sendAgain.layer.borderColor = ZEGOGREEN.CGColor;
    _sendAgain.layer.borderWidth = 1;
    _sendAgain.layer.cornerRadius = 5;
    _problemSMS.font = [self lightFontWithSize:16];
    _next.titleLabel.font = [self boldFontWithSize:22];
    _pinError.hidden = YES;
    _pinError.font = [self italicFontWithSize:15];
    previousError = NO;
    for(UILabel* lb in digitArray) {
        lb.textColor = ZEGODARKGREY;
    }
    [self checkEnabled];
    
    _backView.isAccessibilityElement = YES;
    _backView.accessibilityLabel = @"Indietro";
    _backView.accessibilityHint = @"Indietro";
    _backView.accessibilityTraits = UIAccessibilityTraitButton;
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void) goBack {
    if(_delegate) {
        
        [[self presentingViewController] dismissViewControllerAnimated:YES completion:^{
        }];
    } else {
        [super goBack];
    }
}
-(void) viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    [[_keyboardBox subviews] makeObjectsPerformSelector:@selector(removeFromSuperview)];
    NumberPadUIView* num = [[NumberPadUIView alloc] initWithFrame:
                            CGRectMake(0, 0, _keyboardBox.frame.size.width, _keyboardBox.frame.size.height)];
    num.delegate = self;
    [_keyboardBox addSubview:num];
    
}

-(void) digitTapped:(NSString *)d
{
    if(cidx<4){
        ((UILabel*)[digitArray objectAtIndex:cidx]).text = d;
        cidx++;
    }
    [self checkEnabled];
}

-(void) deleteTapped {
    if(cidx>0){
        cidx--;
        ((UILabel*)[digitArray objectAtIndex:cidx]).text = @"";
    }
    [self checkEnabled];
}

-(void) checkEnabled {
    if(cidx == 4 && !previousError) {
        _next.backgroundColor = ZEGOGREEN;
        _next.enabled = YES;
    } else {
        _next.backgroundColor =ZEGODARKGREY;
        _next.enabled = NO;
    }
    
    if(previousError) {
        previousError = NO; // clear the flag
        _pinError.hidden = YES;
        for(UILabel* lb in digitArray) {
            lb.textColor = ZEGODARKGREY;
        }
    }
}

-(IBAction)resendAction:(id)sender {
    cidx = 0;
    for(UILabel* lb in digitArray) {
        lb.text = @"";
    }
    [self checkEnabled];
    
    PinResendRequest* pr = [[PinResendRequest alloc] init];
    pr.uid = [self loggedUser].uid;
    [self startLoading];
    [[ZegoAPIManager sharedManager] resendPin:^(User *resp) {
        if(resp) {
            [self.view makeToast:NSLocalizedString(@"PIN inviato correttamente.",nil)
                        duration:3.0
                        position:CSToastPositionCenter];
        }
        [self stopLoading];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            ErrorResponse* e = [self handleError:error];
            if(e) {
                [self pinVerificationFailedWithMessage:e.msg];
            }
        }
        [self stopLoading];
    } withRequest:pr];
}

-(IBAction)nextAction:(id)sender {
   
    User* u = [self loggedUser];
    PinRequest* pr = [[PinRequest alloc] init];
    pr.pin = [NSString stringWithFormat:@"%@%@%@%@",_firstDigit.text,_secondDigit.text,_thirdDigit.text,_fourthDigit.text];
    pr.uid = u.uid;
    
    [[ZegoAPIManager sharedManager] validatePin:^(User *resp) {
        if(resp) {
            [self pinVerifiedSuccessfullyWithUser:resp];
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            ErrorResponse* e = [self handleError:error];
            if(e) {
                [self pinVerificationFailedWithMessage:e.msg];
            }
        }
    } withRequest:pr];
   
}

-(void) pinVerifiedSuccessfullyWithUser:(User*)u {
    [self updateUser:u];
     // TODO enum
    
    if(_delegate) {
    
        [[self presentingViewController] dismissViewControllerAnimated:YES completion:^{
            [_delegate pinValidatedCorrectly];
        }];
        
    } else {
        
        if(_mode == 0) { // from login
            [self performSegueWithIdentifier:@"pinToMapSegue" sender:self];
            [self startPollingWithUser:u];
        } else {
            [self performSegueWithIdentifier:@"signupToCompleteSegue" sender:self];
        }
    }
}

-(void) pinVerificationFailedWithMessage:(NSString*)message {
     previousError = YES;
    _pinError.hidden = NO;
    _pinError.textColor = [UIColor redColor];
    _pinError.text = message;
    for(UILabel* lb in digitArray) {
        lb.textColor = [UIColor redColor];
    }
}
#pragma mark - Navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    
    
}

@end
