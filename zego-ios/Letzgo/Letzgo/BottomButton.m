//
//  BottomButton.m
//  Letzgo
//
//  Created by Luca Adamo on 17/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "BottomButton.h"

@implementation BottomButton

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (BOOL)pointInside:(CGPoint)point withEvent:(UIEvent *)event
{
    BOOL inside = [super pointInside: point withEvent: event];
    
    if (inside && !self.isHighlighted && event.type == UIEventTypeTouches)
    {
        self.highlighted = YES;
    }
    
    return inside;
}

@end
