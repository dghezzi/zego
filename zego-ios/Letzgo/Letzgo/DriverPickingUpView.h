//
//  DriverPickingUpView.h
//  Letzgo
//
//  Created by Luca Adamo on 02/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "ZegoBaseView.h"
#import "AddressField.h"
@class DriverPickingUpView;

@protocol DriverPickingUpDelegate

-(void)driverPikcingUpViewCalled:(DriverPickingUpView*)view;
-(void)driverPikcingUpViewStartedRide:(DriverPickingUpView*)view;
-(void)driverPikcingUpViewAbortedRide:(DriverPickingUpView*)view;
-(void)driverPikcingUpViewNavigateToDropoff:(DriverPickingUpView*)view;

@end
@interface DriverPickingUpView : ZegoBaseView<AddressFieldDelegate>
{
    RideRequest* request;
    UIButton* startRide;
    UIButton* abortRide;
}

-(id)initWithFrame:(CGRect)frame andRequest:(RideRequest*)req;

@property(assign) id<DriverPickingUpDelegate> delegate;

@end
