//
//  ReasonSelectorView.m
//  Letzgo
//
//  Created by Luca Adamo on 01/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "ReasonSelectorView.h"

@implementation ReasonSelectorView

-(void)baseInit {
    UIView* bg = [[UIView alloc] initWithFrame:self.bounds];
    bg.backgroundColor = [UIColor colorWithWhite:0 alpha:0.7];
    [self addSubview:bg];
    bg.userInteractionEnabled = YES;
    [bg addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(outTap)]];
    
    
    CGFloat sh = (subtitle.length > 60 ? 140:100)+45*[options count];
    UIView* dialog = [[UIView alloc] initWithFrame:CGRectMake(w*0.1f, (h-sh)/2.f, w*0.8f, sh)];
    dialog.backgroundColor = [UIColor whiteColor];
    dialog.layer.masksToBounds = YES;
    dialog.layer.cornerRadius = 5;
    [self addSubview:dialog];
    
    UILabel* t = [[UILabel alloc] initWithFrame:CGRectMake(w*0.02f, 0, w*0.76f, 30)];
    t.font = [self boldFontWithSize:18];
    t.textAlignment = NSTextAlignmentCenter;
    t.text = title;
    t.textColor = [UIColor redColor];
    [dialog addSubview:t];
    
    UILabel* st = [[UILabel alloc] initWithFrame:CGRectMake(w*0.02f, 30, w*0.76f, (subtitle.length > 60 ? 70: 30))];
    st.font = [self lightFontWithSize:16];
    st.textColor = ZEGODARKGREY;
    st.adjustsFontSizeToFitWidth = YES;
    st.minimumScaleFactor = 0.7;
    st.textAlignment = NSTextAlignmentJustified;
    st.text = subtitle;
    st.numberOfLines = 0;
    [dialog addSubview:st];
    
    for(int i = 0; i < [options count]; i++) {
        UILabel* it = [[UILabel alloc] initWithFrame:CGRectMake(w*0.02f, (subtitle.length > 60 ? 100: 60)+i*45, w*0.76f, 45)];
        it.font = [self regularFontWithSize:16];
        it.textAlignment = NSTextAlignmentLeft;
        it.numberOfLines = 2;
        it.text = [options objectAtIndex:i];
        it.tag = 100+i;
        [dialog addSubview:it];
        it.userInteractionEnabled = YES;
        [it addGestureRecognizer:[[UITapGestureRecognizer alloc]
                                  initWithTarget:self action:@selector(reasonTap:)]];
    }
    
    UIButton *cancelButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [cancelButton addTarget:self
                     action:@selector(cancelAct:)
     forControlEvents:UIControlEventTouchUpInside];
    [cancelButton setTitleColor:ZEGOGREEN forState:UIControlStateNormal];
    [cancelButton setTitle:[title isEqualToString:@""] ? NSLocalizedString(@"Indietro",nil) : NSLocalizedString(@"Chiudi",nil) forState:UIControlStateNormal];
    cancelButton.frame = CGRectMake(0, sh-40, w*0.8f, 40);
    [dialog addSubview:cancelButton];
    self.layer.borderColor = [UIColor lightGrayColor].CGColor;
    self.layer.borderWidth = 0.5;
}

-(void)reasonTap:(UIGestureRecognizer*)sender {
    if(_feedback) {
        _feedback.reason = [options objectAtIndex:(sender.view.tag - 100)];
        [self.delegate reasonSelector:self sendFeedback:_feedback];
    } else {
        [self.delegate reasonSelector:self selected:[options objectAtIndex:(sender.view.tag - 100)] atIdx:(sender.view.tag - 100)];
    }
}

-(void)cancelAct:(id)sender {
    [self.delegate reasonSelectorCanceled:self];
}

-(void)outTap {
    [self.delegate reasonSelectorCanceled:self];
}


-(id)initWithFrame:(CGRect)frame title:(NSString*)tit sub:(NSString*)sub andOptions:(NSArray *)opt {
    self = [super initWithFrame:frame];
    if(self) {
        subtitle = sub;
        title = tit;
        options = opt;
        [self baseInit];
    }
    return self;
}


@end
