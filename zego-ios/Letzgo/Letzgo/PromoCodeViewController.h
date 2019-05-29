//
//  PromoCodeViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"
#import "PromoListViewController.h"

@interface PromoCodeViewController : ZegoViewController<UITextFieldDelegate>
{
    
}

@property (strong, nonatomic) PromoListViewController* promoList;

@property (strong, nonatomic) IBOutlet UILabel *error;

@property (strong, nonatomic) IBOutlet UIView *backView;
@property (strong, nonatomic) IBOutlet UILabel *topTitle;
@property (strong, nonatomic) IBOutlet UILabel *topMessage;
@property (strong, nonatomic) IBOutlet UITextField *promoCode;
@property (strong, nonatomic) IBOutlet UIButton *next;
@property (strong, nonatomic) IBOutlet UIButton *skip;
@property (strong, nonatomic) IBOutlet UILabel *topCta;
-(IBAction)nextAction:(id)sender;
-(IBAction)skipAction:(id)sender;
@end
