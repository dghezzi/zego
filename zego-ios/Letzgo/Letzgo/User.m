//
//  User.m
//  Letzgo
//
//  Created by Luca Adamo on 15/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "User.h"

@implementation User

-(CLLocationCoordinate2D) coordinate {
    if(_llat && _llat*_llon != 0){
        return CLLocationCoordinate2DMake(_llat, _llon);
    } else {
        return CLLocationCoordinate2DMake(0,0);
    }
}

@end
