//
//  DriverOnRideView.h
//  Letzgo
//
//  Created by Luca Adamo on 02/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "ZegoBaseView.h"
#import "TwoElementRideView.h"
#import "CustomUISlider.h"
#import "AddressField.h"

@class DriverOnRideView;

@protocol DriverOnRideViewDelegate

-(void)driverOnRideView:(DriverOnRideView*)view terminatedRideWithPrice:(NSInteger)upPrice;
-(void)driverOnRideMinimumPrice:(DriverOnRideView*)view;
-(void)driverOnRideNavigateToDropoff:(DriverOnRideView*)view;
@end

@interface DriverOnRideView : ZegoBaseView<TwoElementRideViewDelegate, AddressFieldDelegate>
{
    RideRequest* request;
    UIButton* terminateRequest;
    UIView* sliderBox;
    CustomUISlider* slider;
    UILabel* sliderLabel;
    TwoElementRideView* two;
    NSInteger driverPrice;
    NSInteger sliderPrice;
    AddressField* pickup;
    AddressField* dropoff;
    UIButton* bottomButton;
}
-(id)initWithFrame:(CGRect)frame andRequest:(RideRequest*)req;
@property(assign) id<DriverOnRideViewDelegate> delegate;
@end
