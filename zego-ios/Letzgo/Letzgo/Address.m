//
//  Address.m
//  Letzgo
//
//  Created by Luca Adamo on 22/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "Address.h"

@implementation Address

-(NSString*) addressForComponents:(NSInteger)com {
    NSArray* comps = [_address componentsSeparatedByString:@","];
    NSString* part = @"";
    for (int i = 0; i<com; i++) {
        if([comps count] > i) {
            part = [part stringByAppendingString:[comps objectAtIndex:i]];
            if(i<(com -1)) {
                part = [part stringByAppendingString:@", "];
            }
        }
    }
    return part;
}
@end
