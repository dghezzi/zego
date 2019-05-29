//
//  ThreeElementRideView.h
//  Letzgo
//
//  Created by Luca Adamo on 19/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ZegoBaseView.h"

@class ThreeElementRideView;

@protocol ThreeElementRideViewDelegate


@end

@interface ThreeElementRideView : ZegoBaseView
{

}
-(id)initWithFrame:(CGRect)frame request:(RideRequest*)req;

@property (assign) id <ThreeElementRideViewDelegate> delegate;
@end

