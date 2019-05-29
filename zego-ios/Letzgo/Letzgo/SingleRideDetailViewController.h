//
//  SingleRideDetailViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 17/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"

@interface SingleRideDetailViewController : ZegoViewController
{
    
}
@property (nonatomic,strong) NSString* umode;
@property (nonatomic,strong) CompatRidrequest* request;
@property (strong, nonatomic) IBOutlet UIWebView* web;
@property (strong, nonatomic) IBOutlet UIButton* supporto;
@property (strong, nonatomic) IBOutlet UIView *backView;
@property (strong, nonatomic) IBOutlet UILabel *topTitle;

-(IBAction)support:(id)sender;
@end
