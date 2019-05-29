//
//  LoginViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"
#import "InsertPinViewController.h"
#import <EMCCountryPickerController.h>
#import "NumberPadUIView.h"

@interface LoginViewController : ZegoViewController<EMCCountryDelegate, NumberPadDelegate>
{
    NSOrderedSet* countryCodes;
    NSOrderedSet* phoneCodes;
    
}
@property (strong, nonatomic) NSString* currentMobile;
@property (strong, nonatomic) NSString* selCountry;
@property (strong, nonatomic) IBOutlet UIView *backView;
@property (strong, nonatomic) IBOutlet UILabel *topTitle;
@property (strong, nonatomic) IBOutlet UIButton *next;
@property (strong, nonatomic) IBOutlet UILabel *fieldMessage;
@property (strong, nonatomic) IBOutlet UILabel *topMessage;
@property (strong, nonatomic) IBOutlet UIView *flagBox;
@property (strong, nonatomic) IBOutlet UIImageView *flag;
@property (strong, nonatomic) IBOutlet UILabel *prefix;
@property (strong, nonatomic) IBOutlet UILabel *mobile;
@property (strong, nonatomic) IBOutlet UIView *keyboardBox;
@property (strong, nonatomic) IBOutlet UIButton *loginWithFacebook;

-(void) evaluateMobileData;
-(IBAction)nextAction:(id)sender;
-(IBAction)loginWithFacebookAction:(id)sender;

@end
