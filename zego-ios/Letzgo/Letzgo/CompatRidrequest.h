//
//  CompatRidrequest.h
//  Letzgo
//
//  Created by Luca Adamo on 17/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CompatRidrequest : NSObject
{
    
}

@property(assign,nonatomic) NSInteger rid;
@property(strong,nonatomic) NSString* shortid;
@property(strong,nonatomic) NSString* method;
@property(assign,nonatomic) NSInteger drivprice;
@property(assign,nonatomic) NSInteger passprice;
@property(assign,nonatomic) NSInteger status;
@property(assign,nonatomic) NSInteger lev;
@property(assign,nonatomic) NSInteger discount;
@property(strong,nonatomic) NSString* pickup;
@property(strong,nonatomic) NSString* dropoff;
@property(strong,nonatomic) NSString* reqdate;
@property(strong,nonatomic) NSString* shid;

@end
