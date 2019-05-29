//
//  PassengerWaitingAnswerView.h
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ZegoBaseView.h"

@class PassengerWaitingAnswerView;

@protocol PassengerWaitingAnswerViewDelegate
-(void) passengerWaitingAnswerView:(PassengerWaitingAnswerView*)view canceledRequest:(RideRequest*)req;
@end

@interface PassengerWaitingAnswerView : ZegoBaseView
{
    RideRequest* request;
}
-(id)initWithFrame:(CGRect)frame andRequest:(RideRequest*)req;
@property (assign) id <PassengerWaitingAnswerViewDelegate> delegate;

@end
