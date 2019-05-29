//
//  StripeAddCardViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"
#import <Stripe/Stripe.h>
#import "CenterMapViewController.h"
#import "CreditCardViewController.h"

@interface StripeAddCardViewController : ZegoViewController<STPPaymentCardTextFieldDelegate>
{
    
}

@property(nonatomic,strong) CenterMapViewController* map;
@property(nonatomic,strong) CreditCardViewController* ccard;
@property(nonatomic,strong) IBOutlet STPPaymentCardTextField* form;
@property (strong, nonatomic) IBOutlet UIView *backView;
@property (strong, nonatomic) IBOutlet UILabel *topTitle;
@property (strong, nonatomic) IBOutlet UILabel *error;
@property (strong, nonatomic) IBOutlet UIButton *skip;
@property (strong, nonatomic) IBOutlet UIButton *next;
@property (strong, nonatomic) IBOutlet UILabel *topMessage;
-(IBAction)nextAction:(id)sender;
-(IBAction)skipAction:(id)sender;
@end
