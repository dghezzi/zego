//
//  SoundEffect.h
//  Letzgo
//
//  Created by Luca Adamo on 23/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//
#import <Foundation/Foundation.h>
#import <AudioToolbox/AudioServices.h>

@interface SoundEffect : NSObject
{
    SystemSoundID soundID;
}

- (id)initWithSoundNamed:(NSString *)filename;
- (void)play;

@end
