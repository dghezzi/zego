//
//  CompleteSignupViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"

@interface CompleteSignupViewController : ZegoViewController<UITextFieldDelegate>
{

}

@property (strong, nonatomic) IBOutlet UIView *backView;
@property (strong, nonatomic) IBOutlet UILabel *topTitle;
@property (strong, nonatomic) IBOutlet UITextField* fname;
@property (strong, nonatomic) IBOutlet UITextField* lname;
@property (strong, nonatomic) IBOutlet UITextField* email;
@property (strong, nonatomic) IBOutlet UILabel* error;
@property (strong, nonatomic) IBOutlet UIButton *completeWithFacebook;
@property (strong, nonatomic) IBOutlet UIButton *next;
-(IBAction)nextAction:(id)sender;
-(IBAction)completeWithFacebookAction:(id)sender;
@end
