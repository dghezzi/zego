//
//  ZegoBaseView.h
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ZegoViewController.h"
#import "BottomButton.h"

@interface ZegoBaseView : UIView
{
    CGFloat w;
    CGFloat h;
}

-(UIButton*)buttonWithFrame:(CGRect)fr selector:(SEL)sel andTitle:(NSString*)title;
-(BottomButton*)bottomButtonWithFrame:(CGRect)fr selector:(SEL)sel andTitle:(NSString*)title;
-(void) enableButton:(UIButton*)b;
-(void) disableButton:(UIButton*)b;
-(id)initWithFrame:(CGRect)frame;
-(void)addSeparatorAtQuota:(CGFloat)q full:(BOOL)full;
-(void)verticalConnectorAtQuota:(CGFloat)q lenght:(CGFloat)len;
-(UIFont*)lightFontWithSize:(CGFloat)sz;
-(UIFont*)boldFontWithSize:(CGFloat)sz;
-(UIFont*)regularFontWithSize:(CGFloat)sz;

@end
