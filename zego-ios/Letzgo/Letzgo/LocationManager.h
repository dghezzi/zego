//
//  LocationManager.h
//  Letzgo
//
//  Created by Luca Adamo on 19/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import <MobileCoreServices/MobileCoreServices.h>
#import "ZegoAPIManager.h"

extern NSString *const gpsAuthorizationDenied;
extern NSString *const gpsAuthorizationGranted;

enum PollingMode : NSUInteger {
    PollingModeUser = 0,
    PollingModeRequest = 1
};

@interface LocationManager : NSObject<CLLocationManagerDelegate>
{
    CLLocationManager* manager;
    NSTimer* polling;
    NSUInteger st;
    NSInteger idx;
    NSInteger lastupdate;
    
    NSInteger lastUpMin;
}

+ (id) sharedManager;

@property(nonatomic,strong) CLLocation *currentLocation;
@property(nonatomic,strong) RideRequest *request;
@property(nonatomic,strong) User *user;
@property(nonatomic,strong) UIViewController* map;

-(void)changePollingFrequency:(NSInteger)freq;
-(void)configureForMode:(NSString*)mode;
-(void)changePollingMode:(NSUInteger)mode withId:(NSInteger)uid;
-(void)registerObserver:(UIViewController*)map;
-(void)unregisterObserver:(UIViewController*)map;
-(void)revalidateGPS;
-(void)updateNow;
-(void)updateWithLocationHandler:(void (^)(UIBackgroundFetchResult))completionHandler;

@end
