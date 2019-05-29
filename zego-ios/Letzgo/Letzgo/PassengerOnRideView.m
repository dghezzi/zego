//
//  PassengerOnRideView.m
//  Letzgo
//
//  Created by Luca Adamo on 19/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "PassengerOnRideView.h"

@implementation PassengerOnRideView

-(void)baseInit {
    //[self addSubview:[[ThreeElementRideView alloc] initWithFrame:CGRectMake(0, 0, w, 50) request:request]];
    Address* fake = [[Address alloc] init];
    fake.address = request.puaddr;
    
    Address* fake2 = [[Address alloc] init];
    fake2.address = request.doaddr;
    [self addSubview:[[AddressField alloc] initWithFrame:CGRectMake(0, 0, w, 50) type:@"pickup" address:fake nav:NO editable:NO]];
    [self addSubview:[[AddressField alloc] initWithFrame:CGRectMake(0, 50, w, 50) type:@"dropoff" address:fake2 nav:NO editable:NO]];
    
    UIView* bottom = [[UIView alloc] initWithFrame:CGRectMake(0, 110, w*0.3f, 50)];
    bottom.backgroundColor = [UIColor whiteColor];
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    [button addTarget:self
               action:@selector(report)
     forControlEvents:UIControlEventTouchUpInside];
    [button setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
    [button setTitle:NSLocalizedString(@"Termina",nil) forState:UIControlStateNormal];
    button.frame = CGRectMake(0, 0, w*0.3f, 50);
    [bottom addSubview:button];
    [self addSubview:bottom];
    
    shareButton = [self buttonWithFrame:CGRectMake(w*0.3f, 110, w*0.7f, 50) selector:@selector(share) andTitle:NSLocalizedString(@"Condividi",nil)];
    [self enableButton:shareButton];
    [self addSeparatorAtQuota:0 full:YES];
    [self addSeparatorAtQuota:50 full:NO];
    [self addSeparatorAtQuota:100 full:YES];
     [self verticalConnectorAtQuota:31 lenght:38];
}

-(id)initWithFrame:(CGRect)frame andRequest:(RideRequest*)re {
    self = [super initWithFrame:frame];
    request = re;
    [self baseInit];
    return self;
}

-(void)share {
    [self.delegate passengerOnRideShareRide:self];
}

-(void)report {
    [self.delegate passengerOnRideReportIssue:self];
}

@end

