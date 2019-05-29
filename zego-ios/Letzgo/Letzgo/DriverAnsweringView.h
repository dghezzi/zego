//
//  DriverAnsweringView.h
//  Letzgo
//
//  Created by Luca Adamo on 02/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "ZegoBaseView.h"

@class DriverAnsweringView;

@protocol DriverAnsweringDelegate

-(void)driverAnsweringView:(DriverAnsweringView*)view acceptedRequest:(BOOL)accept;

@end

@interface DriverAnsweringView : ZegoBaseView
{
    RideRequest* request;
    UIButton* reject;
    UIButton* accept;
}

-(id) initWithFrame:(CGRect)frame andRequest:(RideRequest*)req;

@property(assign) id<DriverAnsweringDelegate> delegate;
@end
