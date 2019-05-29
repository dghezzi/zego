//
//  SignupViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"
#import "InsertPinViewController.h"
#import "NumberPadUIView.h"
#import <EMCCountryPickerController.h>
#import "FRHyperLabel.h"

@interface SignupViewController : ZegoViewController<EMCCountryDelegate,NumberPadDelegate>
{
    NSOrderedSet* countryCodes;
    NSOrderedSet* phoneCodes;
    
    NSString* selCountry;
    NSString* currentMobile;
    BOOL tcCheck;
}
@property (strong, nonatomic) IBOutlet FRHyperLabel *termsAndCond;

@property (strong, nonatomic) IBOutlet UIView *backView;
@property (strong, nonatomic) IBOutlet UILabel *topTitle;
@property (strong, nonatomic) IBOutlet UIView *flagBox;
@property (strong, nonatomic) IBOutlet UIImageView *flag;
@property (strong, nonatomic) IBOutlet UILabel *prefix;
@property (strong, nonatomic) IBOutlet UILabel *mobile;
@property (strong, nonatomic) IBOutlet UILabel *fieldMessage;
@property (strong, nonatomic) IBOutlet UILabel *topMessage;
@property (strong, nonatomic) IBOutlet UIView *keyboardBox;
@property (strong, nonatomic) IBOutlet UIButton *next;
@property (strong, nonatomic) IBOutlet UIImageView *checkBox;
@property (strong, nonatomic) IBOutlet UIView *checkBoxView;
-(IBAction)nextAction:(id)sender;
@end
