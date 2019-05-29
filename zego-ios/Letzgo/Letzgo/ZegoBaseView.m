//
//  ZegoBaseView.m
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoBaseView.h"

@implementation ZegoBaseView

-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    w = self.frame.size.width;
    h = self.frame.size.height;
    return self;
}

-(UIButton*)buttonWithFrame:(CGRect)fr selector:(SEL)sel andTitle:(NSString*)title {
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    [button addTarget:self
               action:sel
     forControlEvents:UIControlEventTouchUpInside];
    [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [button setTitle:title forState:UIControlStateNormal];
    button.titleLabel.font = [self boldFontWithSize:20];
    button.frame = fr;
    [self addSubview:button];
    return button;
}

-(BottomButton*)bottomButtonWithFrame:(CGRect)fr selector:(SEL)sel andTitle:(NSString*)title {
    BottomButton *button = [BottomButton buttonWithType:UIButtonTypeCustom];
    [button addTarget:self
               action:sel
     forControlEvents:UIControlEventTouchUpInside];
    [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [button setTitle:title forState:UIControlStateNormal];
    button.titleLabel.font = [self boldFontWithSize:20];
    button.frame = fr;
    [self addSubview:button];
    return button;
}

-(void) enableButton:(UIButton*)b {
    b.userInteractionEnabled = YES;
    b.backgroundColor = ZEGOGREEN;
    [self bringSubviewToFront:b];

}

-(void) disableButton:(UIButton*)b {
    b.userInteractionEnabled = NO;
    b.backgroundColor = ZEGODARKGREY;
}

-(UIFont*)lightFontWithSize:(CGFloat)sz {
    return [UIFont fontWithName:@"Raleway-Light" size:sz];
}

-(UIFont*)boldFontWithSize:(CGFloat)sz {
    return [UIFont fontWithName:@"Raleway-SemiBold" size:sz];
}

-(UIFont*)regularFontWithSize:(CGFloat)sz {
    return [UIFont fontWithName:@"Raleway-Regular" size:sz];
}

-(void)addSeparatorAtQuota:(CGFloat)q full:(BOOL)full {
    UIView* sep = [[UIView alloc] initWithFrame:CGRectMake(full ? 0 : w*0.2, q,full ? w :  w*0.8, 0.5f)];
    sep.backgroundColor  =[UIColor lightGrayColor];
    [self addSubview:sep];
}

-(void)verticalConnectorAtQuota:(CGFloat)q lenght:(CGFloat)len {
    UIView* sep = [[UIView alloc] initWithFrame:CGRectMake(18, q,0.5, len)];
    sep.backgroundColor  =[UIColor lightGrayColor];
    [self addSubview:sep];
}
@end
