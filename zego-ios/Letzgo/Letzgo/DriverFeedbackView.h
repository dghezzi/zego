//
//  DriverFeedbackView.h
//  Letzgo
//
//  Created by Luca Adamo on 02/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "ZegoBaseView.h"
#import <HCSStarRatingView/HCSStarRatingView.h>

@class DriverFeedbackView;

@protocol DriverFeedbackViewDelegate
-(void)driverFeedbackViewSubmitFeedback:(Feedback*)feedback;
@end

@interface DriverFeedbackView : ZegoBaseView
{
    RideRequest* request;
    UIButton* feedbackButton;
    HCSStarRatingView *starRatingView;
}

-(id) initWithFrame:(CGRect)frame andRequest:(RideRequest*)request;
@property(assign) id<DriverFeedbackViewDelegate> delegate;

@end




