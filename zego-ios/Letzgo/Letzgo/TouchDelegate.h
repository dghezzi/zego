//
//  TouchDelegate.h
//  Teeser
//
//  Created by Luca Adamo on 27/12/14.
//  Copyright (c) 2014 Get App Srl. All rights reserved.
//

#ifndef Teeser_TouchDelegate_h
#define Teeser_TouchDelegate_h

#import <UIKit/UIKit.h>

@protocol TouchDelegate <NSObject>
@optional

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event;
-(void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event;
-(void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event;

@end

#endif
