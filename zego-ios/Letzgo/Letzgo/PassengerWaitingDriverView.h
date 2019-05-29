//
//  PassengerWaitingDriverView.h
//  Letzgo
//
//  Created by Luca Adamo on 19/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ZegoBaseView.h"
#import "AddressField.h"
#import "RideRequest.h"


@class PassengerWaitingDriverView;

@protocol PassengerWaitingDriverDelegate
-(void)passengerViewRideCanceled:(PassengerWaitingDriverView*)view;
-(void)passengerViewRideCallDriver:(PassengerWaitingDriverView*)view;
@end

@interface PassengerWaitingDriverView : ZegoBaseView
{
    UIButton* call;
    UILabel* te2min;
    RideRequest* request;
    UILabel* te;
}
-(id)initWithFrame:(CGRect)frame andRequest:(RideRequest*)req;

@property (assign) id <PassengerWaitingDriverDelegate> delegate;
@property (strong, nonatomic) NSTimer *timer;
-(void)updateEtaTo:(NSInteger)eta;

@end
