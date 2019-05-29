//
//  TwoElementRideView.h
//  Letzgo
//
//  Created by Luca Adamo on 02/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "ZegoBaseView.h"

@class TwoElementRideView;

@protocol TwoElementRideViewDelegate

-(void)twoElementRideViewPriceTapped:(TwoElementRideView*)view;

@end

@interface TwoElementRideView : ZegoBaseView
{
    UILabel* tp3;
    RideRequest* request;
}

-(id)initWithFrame:(CGRect)frame request:(RideRequest*)req;

@property (assign) id <TwoElementRideViewDelegate> delegate;
-(void)updatePriceTo:(NSInteger)price;
@end
