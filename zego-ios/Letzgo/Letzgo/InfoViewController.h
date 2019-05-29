//
//  InfoViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 22/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"

@interface InfoViewController : ZegoViewController
{
    
}

@property (strong, nonatomic) IBOutlet UIView *backView;
@property (strong, nonatomic) IBOutlet UILabel *topTitle;
@property (strong, nonatomic) IBOutlet UIButton *next;

@property (strong, nonatomic) IBOutlet UILabel *tutorial;
@property (strong, nonatomic) IBOutlet UILabel *termsAndCond;
@property (strong, nonatomic) IBOutlet UILabel *faq;
@property (strong, nonatomic) IBOutlet UILabel *zegoapp;
@property (strong, nonatomic) IBOutlet UILabel *version;
@property (strong, nonatomic) IBOutlet UILabel *privacy;
@property (strong, nonatomic) IBOutlet UILabel *contattaci;
@end
