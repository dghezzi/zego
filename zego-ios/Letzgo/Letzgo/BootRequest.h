//
//  BootRequest.h
//  Letzgo
//
//  Created by Luca Adamo on 15/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface BootRequest : NSObject
{
    
}

@property(nonatomic,strong) NSString* fbid;
@property(nonatomic,strong) NSString* country;
@property(nonatomic,strong) NSString* prefix;
@property(nonatomic,strong) NSString* mobile;
@property(nonatomic,strong) NSString* device;
@property(nonatomic,strong) NSString* os;
@property(nonatomic,strong) NSString* pushid;
@property(nonatomic,assign) NSInteger uid;
@property(nonatomic,strong) NSString* vos;
@property(nonatomic,strong) NSString* vapp;
@property(nonatomic,strong) NSString* deviceId;


@end
