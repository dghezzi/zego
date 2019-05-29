//
//  CompleteSignupViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "CompleteSignupViewController.h"


@interface CompleteSignupViewController ()

@end

@implementation CompleteSignupViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [_backView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(goBack)]];
    _topTitle.font = [self boldFontWithSize:24];
    _email.font = [self italicFontWithSize:26];
    _fname.font = [self italicFontWithSize:26];
    _lname.font = [self italicFontWithSize:26];
    _lname.delegate = self;
    _fname.delegate = self;
    _email.delegate = self;
    [_email addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
     [_fname addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
     [_lname addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];

    _next.titleLabel.font = [self boldFontWithSize:22];
    _completeWithFacebook.titleLabel.font = [self boldFontWithSize:16];
    _error.hidden = YES;
    _error.font = [self italicFontWithSize:14];
    _error.minimumScaleFactor = 10/14.f;
    
    _backView.isAccessibilityElement = YES;
    _backView.accessibilityLabel = @"Indietro";
    _backView.accessibilityHint = @"Indietro";
    _backView.accessibilityTraits = UIAccessibilityTraitButton;
}

-(BOOL) textFieldShouldReturn:(UITextField *)textField{
    
   
    if([textField isEqual:_fname]) {
        [_lname becomeFirstResponder];
    }
    else if([textField isEqual:_lname]) {
        [_email becomeFirstResponder];
    } else {
         [textField resignFirstResponder];
    }
    return YES;
}

-(void) textFieldDidChange:(UITextField*)sender {
    BOOL enable = YES;
    
    enable = enable && [_lname.text length] > 0;
    enable = enable && [_fname.text length] > 0;
    enable = enable && [self validateEmail:_email.text];
    
    if([sender isEqual:_email]) {
        if(![self validateEmail:_email.text]) {
            _email.textColor = [UIColor redColor];            
        } else {
            _email.textColor = [UIColor blackColor];
        }
    }
    
    if(enable) {
        _next.backgroundColor = ZEGOGREEN;
        _next.enabled = YES;
    } else {
        _next.backgroundColor =ZEGODARKGREY;
        _next.enabled = NO;
    }
    
    if(!_error.hidden) {
        _error.hidden = YES;
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)nextAction:(id)sender {
    User* u = [self loggedUser];
    u.email = [_email.text lowercaseString];
    u.fname = _fname.text;
    u.lname = _lname.text;
    [self nextStepWithUser:u];
}
-(void)nextStepWithUser:(User*)u {
    [self startLoading];
    [[ZegoAPIManager sharedManager]complete :^(User *resp) {
        if(resp) {
            [self completeSuccessfullyWithUser:resp];
        }
        [self stopLoading];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            ErrorResponse* e = [self handleError:error];
            if(e) {
                [self failedToCompleteWithError:e.msg];
                if(e.code == 111) { // TODO enum
                    User* uold = [self loggedUser];
                    uold.fbid = nil;
                    [self updateUser:uold];
                }
            }
        }
        [self stopLoading];
    } withRequest:u];
}

-(void)completeSuccessfullyWithUser:(User*)u {
    [self updateUser:u];
    [self performSegueWithIdentifier:@"completeToPromoSegue" sender:self];
}

-(void)failedToCompleteWithError:(NSString*)err {
    _error.textColor = [UIColor redColor];
    _error.text = err;
    _error.hidden = NO;
    _next.backgroundColor =ZEGODARKGREY;
    _next.enabled = NO;
    _email.textColor = [UIColor redColor];
    _fname.enabled = YES;
    _email.enabled = YES;
    _lname.enabled = YES;
    
}

-(void)goBack {
    _fname.enabled = YES;
    _fname.text = @"";
    _email.enabled = YES;
    _email.text = @"";
    _lname.enabled = YES;
    _fname.text = @"";
    [super goBack];
}
#pragma mark - Navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    
    
}

-(IBAction)completeWithFacebookAction:(id)sender
{
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
                     us.email = [result valueForKey:@"email"];
                     us.fname = [result valueForKey:@"first_name"];
                     us.lname = [result valueForKey:@"last_name"];
                     us.gender = [result valueForKey:@"gender"];
                     us.birthdate = [result valueForKey:@"birthday"];
                     us.fbid = [result valueForKey:@"id"];
                     if(us.fbid && !us.img) {
                         us.img =  [NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=normal",us.fbid];
                     }
                     [self updateUser:us];
                     
                     // re-read it and populate UI
                     User* u = [self loggedUser];
                     _fname.enabled = !(u.fname && u.fname.length > 0);
                     _fname.text = u.fname;
                     _email.enabled = !([self validateEmail:u.email]);
                     _email.text = u.email;
                     _lname.enabled = !(u.lname && u.lname.length > 0);
                     _lname.text = u.lname;
                     
                     if(!_fname.enabled && !_lname.enabled && !_email.enabled) {
                         [self nextStepWithUser:u];
                     }
                     
                 }
                 
             }];
             
         }
     }];
}

@end
