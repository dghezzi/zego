//
//  DriverPickingUpView.m
//  Letzgo
//
//  Created by Luca Adamo on 02/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "DriverPickingUpView.h"
#import "ThreeElementRideViewDriver.h"

@implementation DriverPickingUpView

-(void)baseInit {
    [self addSubview:[[ThreeElementRideViewDriver alloc] initWithFrame:CGRectMake(0, 0, w, 50) request:request]];
    
    UIView* line = [[UIView alloc] initWithFrame:CGRectMake(0, 60, w, 80)];
    line.backgroundColor = [UIColor whiteColor];
    line.layer.borderColor = [UIColor lightGrayColor].CGColor;
    line.layer.borderWidth = 0.5;
    
    [self addSubview:line];
    
    UIImageView* userimg = [[UIImageView alloc] initWithFrame:CGRectMake(5, 55, 90, 90)];
    userimg.contentMode = UIViewContentModeScaleAspectFit;
    [userimg setImageWithURL:[NSURL URLWithString:request.rider.userimg] placeholderImage:[UIImage imageNamed:@"userplaceholder"]];
    
    UIImageView* callMask = [[UIImageView alloc] initWithFrame:CGRectMake(5, 55, 90, 90)];
    callMask.contentMode = UIViewContentModeScaleAspectFit;
    callMask.image = [UIImage imageNamed:@"user_tel"];
    callMask.userInteractionEnabled = YES;
    callMask.layer.borderColor = ZEGOGREEN70.CGColor;
    callMask.layer.borderWidth = 0.5;
    callMask.layer.cornerRadius = 45;
    callMask.layer.masksToBounds = YES;
    [callMask addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(callAction)]];
    
    userimg.layer.cornerRadius = 45;
    userimg.layer.masksToBounds = YES;
    userimg.layer.borderColor = ZEGOGREEN70.CGColor;
    userimg.layer.borderWidth = 0.5;
    [self addSubview:userimg];
    
    
    UILabel* name = [[UILabel alloc] initWithFrame:CGRectMake(100, 10, w-180, 25)];
    name.font = [self boldFontWithSize:18];
    name.text = request.rider.name;
    [line addSubview:name];
    
    UIImageView* rating = [[UIImageView alloc] initWithFrame:CGRectMake(100, 45, 100, 25)];
    rating.contentMode = UIViewContentModeScaleAspectFit;
    int s = ceil(request.rider.rating);
    if(s == 0) {
        s = 5;
    }
    rating.image = [UIImage imageNamed:[NSString stringWithFormat:@"s%d",s]];
    [line addSubview:rating];
    
    UIImageView* tp2 = [[UIImageView alloc] initWithFrame:CGRectMake(w-80, 15, 70, 50)];
    tp2.contentMode = UIViewContentModeScaleAspectFit;
    tp2.image = [UIImage imageNamed:[NSString stringWithFormat:@"p%d",request.numpass]];
    
    UIView* lev = [[UIView alloc] initWithFrame:CGRectMake(w-80, 65, 70, 10)];
    if(request.reqlevel > 0) {
        lev.backgroundColor = request.reqlevel == 2 ? [UIColor colorWithRed:202/255.f green:104/255.f blue:180/255.f alpha:1] : [UIColor colorWithRed:223/255.f green:112/255.f blue:48/255.f alpha:1];
        [line addSubview:lev];
    }
    [line addSubview:tp2];

    
    
    Address* fake = [[Address alloc] init];
    fake.address = request.puaddr;
    
    Address* fake2 = [[Address alloc] init];
    fake2.address = request.doaddr;
    AddressField* pickup = [[AddressField alloc] initWithFrame:CGRectMake(0, 160, w, 50) type:@"pickup" address:fake nav:YES editable:NO];
    pickup.delegate = self;
    [self addSubview:pickup];
    [self addSubview:[[AddressField alloc] initWithFrame:CGRectMake(0, 210, w, 50) type:@"dropoff" address:fake2 nav:NO editable:NO]];
    
    
    UIView* bottom = [[UIView alloc] initWithFrame:CGRectMake(0, 270, w*0.3f, 50)];
    bottom.backgroundColor = [UIColor whiteColor];
    abortRide = [UIButton buttonWithType:UIButtonTypeCustom];
    [abortRide addTarget:self
               action:@selector(abortAction)
     forControlEvents:UIControlEventTouchUpInside];
    [abortRide setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
    [abortRide setTitle:NSLocalizedString(@"Annulla",nil) forState:UIControlStateNormal];
    abortRide.frame = CGRectMake(0, 0, w*0.3f, 50);
    [bottom addSubview:abortRide];
    [self addSubview:bottom];
    
    startRide = [self buttonWithFrame:CGRectMake(w*0.3f, 270, w*0.7f, 50) selector:@selector(startAction) andTitle:NSLocalizedString(@"Inizia",nil)];
    [self enableButton:startRide];
    [self addSubview:callMask];
    [self bringSubviewToFront:userimg];
    [self bringSubviewToFront:callMask];
    [self addSeparatorAtQuota:160 full:YES];
    [self addSeparatorAtQuota:210 full:NO];
    [self addSeparatorAtQuota:260 full:YES];
    [self verticalConnectorAtQuota:191 lenght:38];
}

-(void) startAction {
    [self.delegate driverPikcingUpViewStartedRide:self];
}

-(void) abortAction {
    [self.delegate driverPikcingUpViewAbortedRide:self];
}

-(void) callAction {
    [self.delegate driverPikcingUpViewCalled:self];
}

-(void)navSelectedForAddressField:(AddressField*)field {
    [self.delegate driverPikcingUpViewNavigateToDropoff:self];
}

-(id)initWithFrame:(CGRect)frame andRequest:(RideRequest*)req {
    self = [super initWithFrame:frame];
    if(self) {
        request = req;
        [self baseInit];
    }
    return self;
}

@end
