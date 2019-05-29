//
//  ThreeElementIdleView.h
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ZegoBaseView.h"

@class ThreeElementIdleView;

@protocol ThreeElementIdleViewDelegate

-(void)elementViewCardSelected:(ThreeElementIdleView*)view;
-(void)elementViewPriceSelected:(ThreeElementIdleView*)view;
-(void)elementView:(ThreeElementIdleView*)view passnumUpdatedTo:(NSInteger)p;

@end

@interface ThreeElementIdleView : ZegoBaseView
{
    NSInteger passNum;
    UILabel* last4;
    UIImageView* tp1l;
    UIImageView* tp1cash;
    
    UILabel* lb3;
    UILabel* tp3;
    NSString* method;
}
-(void)setToCash;
-(void)updatePrice:(int)p;
-(void)updateCard:(StripeCard*)card;
-(id)initWithFrame:(CGRect)frame request:(RideRequest*)req stripeCard:(StripeCard*)card;
@property (assign) id <ThreeElementIdleViewDelegate> delegate;
@end
