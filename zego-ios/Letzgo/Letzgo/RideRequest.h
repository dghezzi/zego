//
//  RideRequest.h
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CompactRider.h"
#import "CompactDriver.h"

@interface RideRequest : NSObject
{
    
}

enum RideRequestState : NSUInteger {
    RideRequestStatusIdle = 0,
    RideRequestStatusNoDrivers = 1,
    RideRequestStatusSubmitted = 2,
    RideRequestStatusDriverAnswered = 3,
    RideRequestStatusPassengerCanceled = 4,
    RideRequestStatusDriverCanceled = 5,
    RideRequestStatusOnRide = 6,
    RideRequestStatusDriverAborted = 7,
    RideRequestStatusPassengerAborted = 8,
    RideRequestStatusEnded = 9,
    RideRequestStatusPaid = 10,
    RideRequestStatusPassengerTerminated = 11,
    RideRequestStatusPaymentFailed = 12,
    RideRequestStatusMissingFunds = 13,
    RideRequestStatusSystemCanceled = 14
};

@property(nonatomic,assign) int rid;
@property(nonatomic,assign) int pid;
@property(nonatomic,assign) int did;
@property(nonatomic,assign) int status;
@property(nonatomic,assign) double pulat;
@property(nonatomic,assign) double pulng;
@property(nonatomic,strong) NSString* puaddr;
@property(nonatomic,assign) double dolat;
@property(nonatomic,assign) double dolng;
@property(nonatomic,strong) NSString* doaddr;
@property(nonatomic,strong) NSString* method;
@property(nonatomic,strong) NSString* reqdate;
@property(nonatomic,strong) NSString* assigndate;
@property(nonatomic,strong) NSString* canceldate;
@property(nonatomic,strong) NSString* abortdate;
@property(nonatomic,strong) NSString* startdate;
@property(nonatomic,strong) NSString* enddate;
@property(nonatomic,assign) int extmeters;
@property(nonatomic,assign) int reqlevel;
@property(nonatomic,assign) int extsecond;
@property(nonatomic,assign) int extshort;
@property(nonatomic,assign) int drivereta;
@property(nonatomic,assign) double realpulat;
@property(nonatomic,assign) double realpulng;
@property(nonatomic,strong) NSString* realpuaddr;
@property(nonatomic,assign) double realdolat;
@property(nonatomic,assign) double realdolng;
@property(nonatomic,strong) NSString* realdoaddr;
@property(nonatomic,assign) int extprice;
@property(nonatomic,assign) int driverfee;
@property(nonatomic,assign) int zegofee;
@property(nonatomic,assign) int stripedriverfee;
@property(nonatomic,assign) int stripezegofee;
@property(nonatomic,assign) int passprice;
@property(nonatomic,assign) int driverprice;
@property(nonatomic,assign) int numpass;
@property(nonatomic,assign) int options;
@property(nonatomic,assign) int passrating;
@property(nonatomic,assign) int drivrating;
@property(nonatomic,assign) int discount;
@property(nonatomic,assign) int promoid;
@property(nonatomic,strong) NSString* shid;
@property(nonatomic,strong) NSString* shortid;
@property(nonatomic,strong) NSString* abortreason;
@property(nonatomic,strong) NSString* cancelreason;
@property(nonatomic,strong) CompactDriver* driver;
@property(nonatomic,strong) CompactRider* rider;
@end
