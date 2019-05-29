//
//  ReasonSelectorView.h
//  Letzgo
//
//  Created by Luca Adamo on 01/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ZegoBaseView.h"

@class ReasonSelectorView;

@protocol ReasonSelectorDelegate
-(void)reasonSelector:(ReasonSelectorView*)selector selected:(NSString*)reason atIdx:(NSInteger)idx;
-(void)reasonSelector:(ReasonSelectorView*)selector sendFeedback:(Feedback*)fe;
-(void)reasonSelectorCanceled:(ReasonSelectorView*)selector;
@end

@interface ReasonSelectorView : ZegoBaseView
{
    NSArray* options;
    NSString* title;
    NSString* subtitle;
}

-(id)initWithFrame:(CGRect)frame title:(NSString*)tit sub:(NSString*)sub andOptions:(NSArray *)opt;
@property(assign) id<ReasonSelectorDelegate> delegate;
@property(nonatomic,strong) NSString* data;
@property(nonatomic,strong) Feedback* feedback;
@end
