//
//  InsertPinViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"
#import "NumberPadUIView.h"


@protocol InsertPinControllerDelegate
    -(void) pinValidatedCorrectly;
@end


@interface InsertPinViewController : ZegoViewController<NumberPadDelegate>
{
    NSInteger cidx;
    NSMutableArray* digitArray;
    BOOL previousError;
}

@property (nonatomic, assign) NSInteger mode;
@property (nonatomic, strong) NSString* currentMobile;

@property (strong, nonatomic) IBOutlet UILabel *problemSMS;
@property (strong, nonatomic) IBOutlet UIView *backView;
@property (strong, nonatomic) IBOutlet UILabel *topTitle;
@property (strong, nonatomic) IBOutlet UIView *keyboardBox;
@property (strong, nonatomic) IBOutlet UILabel *smsSentText;
@property (strong, nonatomic) IBOutlet UILabel *firstDigit;
@property (strong, nonatomic) IBOutlet UILabel *secondDigit;
@property (strong, nonatomic) IBOutlet UILabel *thirdDigit;
@property (strong, nonatomic) IBOutlet UILabel *fourthDigit;
@property (strong, nonatomic) IBOutlet UIButton *sendAgain;
@property (strong, nonatomic) IBOutlet UIButton *next;
@property (strong, nonatomic) IBOutlet UILabel *pinError;
-(IBAction)nextAction:(id)sender;
-(IBAction)resendAction:(id)sender;

@property (assign) id <InsertPinControllerDelegate> delegate;
@end
