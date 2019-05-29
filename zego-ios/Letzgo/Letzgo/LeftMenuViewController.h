//
//  LeftMenuViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 16/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"
#import "LocationManager.h"

@interface LeftMenuViewController : ZegoViewController
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *menuwidth;
@property (strong, nonatomic) IBOutlet UIView *container;
@property (strong, nonatomic) IBOutlet UILabel *fullname;
@property (strong, nonatomic) IBOutlet UILabel *mobile;
@property (strong, nonatomic) IBOutlet UILabel *refcode;
@property (strong, nonatomic) IBOutlet UIImageView *userimg;
@property (strong, nonatomic) IBOutlet UILabel *profileLabel;
@property (strong, nonatomic) IBOutlet UILabel *paymentLabel;
@property (strong, nonatomic) IBOutlet UILabel *historyLabel;
@property (strong, nonatomic) IBOutlet UILabel *promoLabel;
@property (strong, nonatomic) IBOutlet UILabel *infoLabel;
@property (strong, nonatomic) IBOutlet UILabel *switchModeLabel;
@property (strong, nonatomic) IBOutlet UIView *profileView;
@property (strong, nonatomic) IBOutlet UIView *paymentView;
@property (strong, nonatomic) IBOutlet UIView *historyView;
@property (strong, nonatomic) IBOutlet UIView *promoView;
@property (strong, nonatomic) IBOutlet UIView *infoView;
@property (strong, nonatomic) IBOutlet UIView *switchModeView;
@property (strong, nonatomic) IBOutlet UIButton *enableDriverButton;
@property (strong, nonatomic) IBOutlet UILabel *tapToShare;
@property (strong, nonatomic) IBOutlet UIScrollView *scrollMenu;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *profileHeight;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *paymentHeight;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *historyHeight;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *promoHeight;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *infoHeight;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *switchHeight;
@property (strong, nonatomic) IBOutlet UIView *base;
@property (strong, nonatomic) IBOutlet UIImageView *switchImage;

-(IBAction)bottomButtonTapped:(id)sender;

@end
