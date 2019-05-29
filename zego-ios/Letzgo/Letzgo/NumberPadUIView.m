//
//  NumberPadUIView.m
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "NumberPadUIView.h"

@implementation NumberPadUIView

- (void)baseInit {
    _delegate = nil;
    CGFloat w = self.frame.size.width;
    CGFloat h = self.frame.size.height;
    CGFloat ch = (h*0.9)/4;
    CGFloat cw = (w*0.8)/3;
    [self addLabel:[[UILabel alloc] initWithFrame:CGRectMake(w*0.1, h*0.05, cw, ch)] withVal:1];
    [self addLabel:[[UILabel alloc] initWithFrame:CGRectMake(w*0.1+cw, h*0.05, cw, ch)] withVal:2];
    [self addLabel:[[UILabel alloc] initWithFrame:CGRectMake(w*0.1+2*cw, h*0.05, cw, ch)] withVal:3];
    
    [self addLabel:[[UILabel alloc] initWithFrame:CGRectMake(w*0.1, h*0.05+ch, cw, ch)] withVal:4];
    [self addLabel:[[UILabel alloc] initWithFrame:CGRectMake(w*0.1+cw, h*0.05+ch, cw, ch)] withVal:5];
    [self addLabel:[[UILabel alloc] initWithFrame:CGRectMake(w*0.1+2*cw, h*0.05+ch, cw, ch)] withVal:6];
    
    [self addLabel:[[UILabel alloc] initWithFrame:CGRectMake(w*0.1, h*0.05+2*ch, cw, ch)] withVal:7];
    [self addLabel:[[UILabel alloc] initWithFrame:CGRectMake(w*0.1+cw, h*0.05+2*ch, cw, ch)] withVal:8];
    [self addLabel:[[UILabel alloc] initWithFrame:CGRectMake(w*0.1+2*cw, h*0.05+2*ch, cw, ch)] withVal:9];
    
    [self addLabel:[[UILabel alloc] initWithFrame:CGRectMake(w*0.1+cw, h*0.05+3*ch, cw, ch)] withVal:0];
    [self addErase:[[UIView alloc] initWithFrame:CGRectMake(w*0.1+2*cw, h*0.05+3*ch, cw, ch)]];
}

-(void) addLabel:(UILabel*)lb withVal:(NSInteger)v {
    lb.tag = 100+v;
    lb.textAlignment = NSTextAlignmentCenter;
    lb.text = [NSString stringWithFormat:@"%ld",(long)v];
    lb.font = [UIFont fontWithName:@"Raleway-Regular" size:32];
    lb.userInteractionEnabled = YES;
    [lb addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(digitTapped:)]];
    
    [self addSubview:lb];
}

-(void) addErase:(UIView*)lb {
    CGSize ss = lb.frame.size;
    UIImageView* er = [[UIImageView alloc] initWithFrame:CGRectMake(ss.width*0.25, ss.height*0.25, ss.width*0.5, ss.height*0.5)];
    er.image = [UIImage imageNamed:@"canc-icon"];
    er.contentMode = UIViewContentModeScaleAspectFit;
    [lb addSubview:er];
    lb.userInteractionEnabled = YES;
    [lb addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(eraseTapped:)]];
    er.userInteractionEnabled = YES;
    [er addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(eraseTapped:)]];
    
    [self addSubview:lb];
}

-(void) digitTapped:(UIGestureRecognizer*)sender {
    [self.delegate digitTapped:[NSString stringWithFormat:@"%ld",sender.view.tag-100]];
}

-(void) eraseTapped:(UIGestureRecognizer*)sender {
    [self.delegate deleteTapped];
}
- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self baseInit];
    }
    return self;
}

- (id)initWithCoder:(NSCoder *)aDecoder {
    if ((self = [super initWithCoder:aDecoder])) {
        [self baseInit];
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
}


@end
