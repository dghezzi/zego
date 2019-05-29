//
//  RideRequestAction.h
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface RideRequestAction : NSObject
{
    
}

@property(nonatomic,strong) NSString* action;
@property(nonatomic,assign) int uid;
@property(nonatomic,assign) int rid;
@property(nonatomic,strong) NSString* text;
@property(nonatomic,assign) int priceupdate;
@property(nonatomic,assign) int capture;
@end
