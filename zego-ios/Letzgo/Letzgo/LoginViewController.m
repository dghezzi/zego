//
//  LoginViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "LoginViewController.h"

@interface LoginViewController ()

@end

@implementation LoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    NSDictionary *dictionary = [NSDictionary dictionaryWithContentsOfFile:[[NSBundle mainBundle]
                                                                           pathForResource:@"config" ofType:@"plist"]];
    NSArray* ct = [dictionary objectForKey:@"countries"];
    
    countryCodes = [NSOrderedSet orderedSetWithArray:ct];
    NSInteger ccct = [countryCodes count];
    NSArray* pf = [dictionary objectForKey:@"prefixes"];
    phoneCodes = [NSOrderedSet orderedSetWithArray:pf];
    NSInteger pfct = [phoneCodes count];
    
    for(int i = 0; i<206;i++) {
        NSLog(@"%@ [%@]",[ct objectAtIndex:i],[pf objectAtIndex:i]);
    }
    
    [_backView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(goBack)]];
    _topTitle.font = [self boldFontWithSize:24];
    _topMessage.font = [self lightFontWithSize:16];
    _fieldMessage.font = [self italicFontWithSize:13];
    
    _flagBox.userInteractionEnabled = YES;
    [_flagBox addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(openCountrySel:)]];
    
    _next.titleLabel.font = [self boldFontWithSize:22];
    _loginWithFacebook.titleLabel.font = [self boldFontWithSize:18];
    _mobile.textColor = [UIColor lightGrayColor];
    _currentMobile = @"";
   
    [self evaluateMobileData];
    _selCountry = @"it";
    
    _backView.isAccessibilityElement = YES;
    _backView.accessibilityLabel = @"Indietro";
    _backView.accessibilityHint = @"Indietro";
        _backView.accessibilityTraits = UIAccessibilityTraitButton;
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
    if( [_currentMobile length]  < 14) {
    _currentMobile = [_currentMobile stringByAppendingString:d];
    if([_currentMobile length] == 3) {
        _currentMobile = [_currentMobile stringByAppendingString:@" "];
    }
    [self evaluateMobileData];
    }
}

-(void) deleteTapped {
    
    if(_currentMobile.length > 0) {
        _currentMobile = [_currentMobile substringToIndex:(_currentMobile.length-1)];
    }
    [self evaluateMobileData];
}

-(void) evaluateMobileData {
    if([_currentMobile length] == 0) {
        _mobile.textColor = [UIColor lightGrayColor];
        _mobile.text =  @"333 1234567";
        _fieldMessage.textColor = [UIColor darkGrayColor];
        _fieldMessage.text = NSLocalizedString(@"Riceverai un SMS di verifica",nil);
        _next.backgroundColor = ZEGODARKGREY;
        _next.enabled = NO;
    }
    
    else {
        
        if([_currentMobile length] >= 5 && [_currentMobile length] <= 13) {
            _fieldMessage.textColor = [UIColor darkGrayColor];
            _fieldMessage.text = NSLocalizedString(@"Riceverai un SMS di verifica",nil);
            _next.backgroundColor = ZEGOGREEN;
            _next.enabled = YES;
        } else {
            _fieldMessage.textColor = [UIColor redColor];
            _fieldMessage.text = NSLocalizedString(@"il numero inserito non sembra valido",nil);
            _next.backgroundColor = ZEGODARKGREY;
            _next.enabled = NO;
        }
        _mobile.textColor = [UIColor blackColor];
        _mobile.text =  _currentMobile;
    }

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


-(IBAction)nextAction:(id)sender {
    
    BootRequest* br = [self rawBootRequest];
    br.prefix = _prefix.text;
    br.mobile = [_mobile.text stringByReplacingOccurrencesOfString:@" " withString:@""];
    br.country = _selCountry;

    [self loginWithBootRequest:br];
    
}

-(void)loginWithBootRequest:(BootRequest*)br {
    [self startLoading];
    [[ZegoAPIManager sharedManager] login:^(User *resp) {
        if(resp) {
            [self loginSucceededWithUser:resp fromFB:br.fbid];
        }
        [self stopLoading];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            ErrorResponse* e = [self handleError:error];
            if(e) {
                [self loginFailedWithErrorMessage:e.msg];
            }
        }
        [self stopLoading];
    } withRequest:br];
}

-(void)loginSucceededWithUser:(User*)u fromFB:(BOOL)fromFb {
    [self updateUser:u];
    if(fromFb && u.mobok && u.mobok == 1)
    {
        [self performSegueWithIdentifier:@"loginToMapSegue" sender:self];
    } else {
        [self performSegueWithIdentifier:@"loginToPinSegue" sender:self];
    }
    
}

-(void)loginFailedWithErrorMessage:(NSString*)err {
    _fieldMessage.textColor = [UIColor redColor];
    _fieldMessage.text = err;
    _next.backgroundColor = ZEGODARKGREY;
    _next.enabled = NO;
}

-(IBAction)loginWithFacebookAction:(id)sender {
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
                     us.email = [result valueForKey:@"email"];
                     us.fname = [result valueForKey:@"first_name"];
                     us.lname = [result valueForKey:@"last_name"];
                     us.gender = [result valueForKey:@"gender"];
                     us.birthdate = [result valueForKey:@"birthday"];
                     us.fbid = [result valueForKey:@"id"];
                     [self updateUser:us];
                     
                     // then reload and login
                     BootRequest* br = [self rawBootRequest];
                     br.fbid = us.fbid;
                     
                     [self loginWithBootRequest:br];
                     
                 }
                 
             }];
             
         }
     }];
    
}

#pragma mark - EMCCountry

- (void)openCountrySel:(UIGestureRecognizer*)sel {
    [self performSegueWithIdentifier:@"openCountryPicker" sender:self];
}

- (void)countryController:(id)sender didSelectCountry:(EMCCountry *)chosenCountry;
{
    NSString* ccode = chosenCountry.countryCode;
    _flag.image = [UIImage imageNamed:ccode];
    _flag.layer.borderWidth = 0.5;
    _flag.layer.borderColor = [UIColor lightGrayColor].CGColor;
    
    _selCountry = ccode;
    //long ctidx = [[countryCodes array] indexOfObject:ccode];
    _prefix.text = [self prefixForCountry:ccode];//[[phoneCodes array] objectAtIndex:ctidx];
    [self dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - Navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"openCountryPicker"])
    {
        EMCCountryPickerController *countryPicker = segue.destinationViewController;
        countryPicker.availableCountryCodes = countryCodes;
        countryPicker.countryDelegate = self;
    }
    else  if ([segue.identifier isEqualToString:@"loginToPinSegue"])
    {
        InsertPinViewController* ipc = (InsertPinViewController*)[segue destinationViewController];
        ipc.mode = 0; // TODO enum
        ipc.currentMobile = [NSString stringWithFormat:@"%@ %@",_prefix.text,_currentMobile];
    }
    
}



@end
