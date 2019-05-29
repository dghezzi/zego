//
//  PassengerFeedbackView.h
//  Letzgo
//
//  Created by Luca Adamo on 19/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoBaseView.h"
#import <HCSStarRatingView/HCSStarRatingView.h>
@class PassengerFeedbackView;

@protocol PassengerFeedbackViewDelegate
    -(void)passengerFeedbackViewSubmitFeedback:(Feedback*)feedback;
@end

@interface PassengerFeedbackView : ZegoBaseView
{
    RideRequest* request;
    UIButton* feedbackButton;
    HCSStarRatingView *starRatingView;
}

-(id) initWithFrame:(CGRect)frame andRequest:(RideRequest*)request;
@property(assign) id<PassengerFeedbackViewDelegate> delegate;

@end
