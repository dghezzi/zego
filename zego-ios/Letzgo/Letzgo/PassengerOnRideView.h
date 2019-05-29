//
//  PassengerOnRideView.h
//  Letzgo
//
//  Created by Luca Adamo on 19/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ZegoBaseView.h"
#import "ThreeElementRideView.h"
#import "AddressField.h"
@class PassengerOnRideView;

@protocol PassengerOnRideViewDelegate
-(void)passengerOnRideReportIssue:(PassengerOnRideView*)view;
-(void)passengerOnRideShareRide:(PassengerOnRideView*)view;
@end

@interface PassengerOnRideView : ZegoBaseView
{
    RideRequest* request;
    UIButton* shareButton;
}

-(id)initWithFrame:(CGRect)frame andRequest:(RideRequest*)re;

@property (assign) id <PassengerOnRideViewDelegate> delegate;

@end
