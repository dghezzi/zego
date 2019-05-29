//
//  CustomUISlider.m
//  Letzgo
//
//  Created by Luca Adamo on 30/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "CustomUISlider.h"

@implementation CustomUISlider

- (CGRect)trackRectForBounds:(CGRect)bounds {
    CGRect rect = CGRectMake(0, 0, self.bounds.size.width, 10);
    return rect;
}

@end
