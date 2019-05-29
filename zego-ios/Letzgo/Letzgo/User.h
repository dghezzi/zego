//
//  User.h
//  Letzgo
//
//  Created by Luca Adamo on 15/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//


#import <Foundation/Foundation.h>
#import <NSObject+ObjectMap.h>
#import <GoogleMaps/GoogleMaps.h>

@interface User : NSObject
{
    
}
enum UserRealTimeStatus : NSUInteger {
    PassengerStatusIdle = 100,
    PassengerStatusWaitingForAnswer = 101,
    PassengerStatusWaitingForDriver = 102,
    PassengerStatusOnRide = 103,
    PassengerStatusPaymentDue = 104,
    PassengerStatusFeedbackDue = 105,
    DriverStatusIdle = 200,
    DriverStatusAnswering = 201,
    DriverStatusPickingUp = 202,
    DriverStatusOnRide = 203,
    DriverStatusFeedbackDue = 204
};


@property(nonatomic,assign) NSInteger uid;
@property(nonatomic,strong) NSString* utype;
@property(nonatomic,assign) NSInteger cashdue;
@property(nonatomic,assign) NSInteger cardonly;
@property(nonatomic,strong) NSString* umode;
@property(nonatomic,assign) NSInteger candrive;
@property(nonatomic,assign) NSInteger current;
@property(nonatomic,strong) NSString* fname;
@property(nonatomic,strong) NSString* lname;
@property(nonatomic,strong) NSString* email;
@property(nonatomic,strong) NSString* wemail;
@property(nonatomic,strong) NSString* country;
@property(nonatomic,strong) NSString* prefix;
@property(nonatomic,strong) NSString* mobile;
@property(nonatomic,strong) NSString* insdate;
@property(nonatomic,strong) NSString* moddate;
@property(nonatomic,strong) NSString* lastseen;
@property(nonatomic,strong) NSString* img;
@property(nonatomic,strong) NSString* fbid;
@property(nonatomic,assign) NSInteger payok;
@property(nonatomic,assign) NSInteger tcok;
@property(nonatomic,assign) NSInteger mobok;
@property(nonatomic,strong) NSString* refcode;
@property(nonatomic,assign) NSInteger refuid;
@property(nonatomic,assign) NSInteger cashused;
@property(nonatomic,strong) NSString* device;
@property(nonatomic,strong) NSString* vos;
@property(nonatomic,strong) NSString* os;
@property(nonatomic,strong) NSString* pushid;
@property(nonatomic,strong) NSString* vapp;
@property(nonatomic,strong) NSString* gender;
@property(nonatomic,strong) NSString* birthdate;
@property(nonatomic,strong) NSString* deviceid;
@property(nonatomic,assign) double llat;
@property(nonatomic,assign) double llon;
@property(nonatomic,assign) double pavg;
@property(nonatomic,assign) double davg;
@property(nonatomic,strong) NSString* llocdate;
@property(nonatomic,assign) NSInteger status;
@property(nonatomic,assign) NSInteger rtstatus;
@property(nonatomic,assign) NSInteger bitmask;
@property(nonatomic,strong) NSString* banexpdate;
@property(nonatomic,strong) NSString* banreason;
@property(nonatomic,strong) NSString* stripeid;
@property(nonatomic,strong) NSString* zegotoken;
@property(nonatomic,assign) NSInteger debt;
-(CLLocationCoordinate2D) coordinate;
@end
