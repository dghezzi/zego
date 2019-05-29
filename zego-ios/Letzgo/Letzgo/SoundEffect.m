//
//  SoundEffect.m
//  Letzgo
//
//  Created by Luca Adamo on 23/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "SoundEffect.h"

@implementation SoundEffect

- (id)initWithSoundNamed:(NSString *)filename
{
    if ((self = [super init]))
    {
        NSURL *fileURL = [[NSBundle mainBundle] URLForResource:filename withExtension:nil];
        if (fileURL != nil)
        {
            SystemSoundID theSoundID;
            OSStatus error = AudioServicesCreateSystemSoundID((__bridge CFURLRef)fileURL, &theSoundID);
            if (error == kAudioServicesNoError)
                soundID = theSoundID;
        }
    }
    return self;
}

- (void)dealloc
{
    AudioServicesDisposeSystemSoundID(soundID);
}

- (void)play
{
    AudioServicesPlaySystemSound(soundID);
}

@end
