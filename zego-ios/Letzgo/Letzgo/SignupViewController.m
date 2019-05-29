//
//  SignupViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "SignupViewController.h"

@interface SignupViewController ()

@end

@implementation SignupViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self updateUser:nil];
    [_backView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(goBack)]];
    NSDictionary *dictionary = [NSDictionary dictionaryWithContentsOfFile:[[NSBundle mainBundle]
                                                                           pathForResource:@"config" ofType:@"plist"]];
    NSArray* ct = [dictionary objectForKey:@"countries"];
    countryCodes = [NSOrderedSet orderedSetWithArray:ct];
    
    phoneCodes = [NSOrderedSet orderedSetWithArray:[dictionary objectForKey:@"prefixes"]];

    _topTitle.font = [self boldFontWithSize:24];
    _topMessage.font = [self lightFontWithSize:16];
    _fieldMessage.font = [self italicFontWithSize:13];
    
    _flagBox.userInteractionEnabled = YES;
    [_flagBox addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(openCountrySel:)]];
    
    _next.titleLabel.font = [self boldFontWithSize:22];
    _mobile.textColor = [UIColor lightGrayColor];
    currentMobile = @"";
    tcCheck = NO;
    
    _checkBoxView.userInteractionEnabled = YES;
    _checkBox.userInteractionEnabled = YES;
    [_checkBox addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(toggleCheckBox:)]];
    [_checkBoxView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(toggleCheckBox:)]];
    
    [self evaluateMobileData];
    selCountry = @"it";
    _termsAndCond.userInteractionEnabled = YES;
    NSDictionary *attributes = @{NSFontAttributeName: [UIFont preferredFontForTextStyle:UIFontTextStyleHeadline],NSUnderlineStyleAttributeName: @(NSUnderlineStyleSingle)};
    
    _termsAndCond.attributedText = [[NSAttributedString alloc]initWithString:_termsAndCond.text attributes:@{NSFontAttributeName: [UIFont preferredFontForTextStyle:UIFontTextStyleHeadline]}];
    _termsAndCond.linkAttributeDefault=attributes;
    
    //Step 2: Define a selection handler block
    void(^handler)(FRHyperLabel *label, NSString *substring) = ^(FRHyperLabel *label, NSString *substring){
        if([substring isEqualToString:NSLocalizedString(@"Privacy Policy",nil)]) {
             [[UIApplication sharedApplication] openURL:[NSURL URLWithString:NSLocalizedString(@"http://www.zegoapp.com/it/privacy",nil)]];
        } else {
             [[UIApplication sharedApplication] openURL:[NSURL URLWithString:NSLocalizedString(@"http://www.zegoapp.com/it/termini-e-condizioni",nil)]];
        }
    };
    
    
    //Step 3: Add link substrings
    [_termsAndCond setLinksForSubstrings:@[NSLocalizedString(@"Termini e Condizioni",nil), NSLocalizedString(@"Privacy Policy",nil)] withLinkHandler:handler];
    
    _backView.isAccessibilityElement = YES;
    _backView.accessibilityLabel = @"Indietro";
    _backView.accessibilityHint = @"Indietro";
    _backView.accessibilityTraits = UIAccessibilityTraitButton;

}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
    if ([currentMobile length] < 14) {
    currentMobile = [currentMobile stringByAppendingString:d];
    if([currentMobile length] == 3) {
        currentMobile = [currentMobile stringByAppendingString:@" "];
    }
    [self evaluateMobileData];
    }
}

-(void) deleteTapped {
    
    if(currentMobile.length > 0) {
        currentMobile = [currentMobile substringToIndex:(currentMobile.length-1)];
    }
    [self evaluateMobileData];
}

-(void) evaluateMobileData {
    
    _checkBox.image = [UIImage imageNamed:tcCheck ?@"tcon" : @"tcoff"];
    if([currentMobile length] == 0) {
        _mobile.textColor = [UIColor lightGrayColor];
        _mobile.text =  @"333 1234567";
        _fieldMessage.textColor = [UIColor darkGrayColor];
        _fieldMessage.text = NSLocalizedString(@"Riceverai un SMS di verifica",nil);
        _next.backgroundColor = ZEGODARKGREY;
        _next.enabled = NO;
    }
    
    else {
        
        if([currentMobile length] >= 5 && [currentMobile length] <= 13) {
            _fieldMessage.textColor = [UIColor darkGrayColor];
            _fieldMessage.text = NSLocalizedString(@"Riceverai un SMS di verifica",nil);
            _next.backgroundColor = ZEGOGREEN;
            _next.enabled = YES;
        } else {
           
            _fieldMessage.textColor = [UIColor redColor];
            _fieldMessage.text =NSLocalizedString(@"il numero inserito non sembra valido",nil);
            _next.backgroundColor = ZEGODARKGREY;
            _next.enabled = NO;
        }
        _mobile.textColor = [UIColor blackColor];
        _mobile.text =  currentMobile;
    }
    
    if(!tcCheck) {
        _next.backgroundColor = ZEGODARKGREY;
        _next.enabled = NO;
    }
    
}

-(IBAction)nextAction:(id)sender {
    
    BootRequest* br = [self rawBootRequest];
    br.prefix = _prefix.text;
    br.mobile = [_mobile.text stringByReplacingOccurrencesOfString:@" " withString:@""];
    br.country = selCountry;
    [self startLoading];
    [[ZegoAPIManager sharedManager] signup:^(User *resp) {
        if(resp) {
            [self signupSucceededWithUser:resp];
        }
        [self stopLoading];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            ErrorResponse* e = [self handleError:error];
            if(e) {
                [self signupFailedWithErrorMessage:e.msg];
            }
        }
        [self stopLoading];
    } withRequest:br];
    
}

-(void)signupSucceededWithUser:(User*)u {
    [self updateUser:u];
    [self performSegueWithIdentifier:@"signupToPinSegue" sender:self];
}

-(void)signupFailedWithErrorMessage:(NSString*)err {
    _fieldMessage.textColor = [UIColor redColor];
    _fieldMessage.text = err;
    _next.backgroundColor = ZEGODARKGREY;
    _next.enabled = NO;
}

-(void)openTc {
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:NSLocalizedString(@"http://www.zegoapp.com/it/termini-e-condizioni",nil)]];
}

-(void)toggleCheckBox:(UIGestureRecognizer*)sender {
    tcCheck = !tcCheck;
    [self evaluateMobileData];
}

#pragma mark - EMCCountry

- (void)openCountrySel:(UIGestureRecognizer*)sel {
    [self performSegueWithIdentifier:@"openCountryPickerSignup" sender:self];
}

- (void)countryController:(id)sender didSelectCountry:(EMCCountry *)chosenCountry;
{
    NSString* ccode = chosenCountry.countryCode;
    _flag.image = [UIImage imageNamed:ccode];
    _flag.layer.borderWidth = 0.5;
    _flag.layer.borderColor = [UIColor lightGrayColor].CGColor;
    
    selCountry = ccode;
    _prefix.text = [self prefixForCountry:ccode];
    //_prefix.text = [[phoneCodes array] objectAtIndex:[[countryCodes array] indexOfObject:ccode]];
    [self dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - Navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if([[segue identifier] isEqualToString:@"openCountryPickerSignup"]){
        EMCCountryPickerController *countryPicker = segue.destinationViewController;
        countryPicker.availableCountryCodes = countryCodes;
        countryPicker.countryDelegate = self;

    } else {
        InsertPinViewController* ipc = (InsertPinViewController*)[segue destinationViewController];
        ipc.mode = 1; // TODO enum
        ipc.currentMobile = [NSString stringWithFormat:@"%@ %@",_prefix.text,currentMobile];
    }
}

@end
