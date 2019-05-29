//
//  LocationManager.m
//  Letzgo
//
//  Created by Luca Adamo on 19/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "LocationManager.h"
#import "CenterMapViewController.h"


@implementation LocationManager
@synthesize map;
NSString *const gpsAuthorizationDenied = @"gpsAuthorizationDenied";
NSString *const gpsAuthorizationGranted = @"gpsAuthorizationGranted";

static LocationManager *sharedManager = nil;


// GCD based singleton implementation
+ (id)sharedManager {
    if(sharedManager == nil)
    {
        sharedManager = [[LocationManager alloc] init];
    }
    return sharedManager;
}

-(id) init {
    self = [super init];
    manager = [CLLocationManager new];
    manager.delegate = self;
    CLAuthorizationStatus authst = CLLocationManager.authorizationStatus;
    [manager requestAlwaysAuthorization];
    manager.distanceFilter = kCLDistanceFilterNone;
    manager.desiredAccuracy = kCLLocationAccuracyBest;
    [manager startUpdatingLocation];

    
    polling = [NSTimer scheduledTimerWithTimeInterval:5.0
                                               target:self
                                             selector:@selector(polling:)
                                             userInfo:nil
                                              repeats:YES];
    
    lastupdate = 0;
    lastUpMin = 5;
    return self;
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    
    _currentLocation = [locations objectAtIndex:[locations count]-1];
    if(map) {
        CenterMapViewController* m = (CenterMapViewController*)map;
        [m locationUpdated:_currentLocation.coordinate];
    }
    
    if(([[NSDate date] timeIntervalSince1970] - lastupdate) > lastUpMin) {
        [self postLocation];
    }
}

-(void) postLocation {
    NSInteger uid = 0;
    
    CLLocation *lastKnown = manager.location;
    
    if(lastKnown) {
        _currentLocation = lastKnown;
    }
    
    if(!_user) {
        uid = [self loggedUser].uid;
    } else {
        uid = _user.uid;
    }
    Location* loc = [[Location alloc] init];
    loc.uid = uid;
    loc.lat = _currentLocation.coordinate.latitude;
    loc.lng = _currentLocation.coordinate.longitude;
    loc.accuracy = _currentLocation.horizontalAccuracy;
    [[ZegoAPIManager sharedManager] postLocation:^(Location *resp) {
        if(resp) {
            // DO NOTHING NOW
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        // TODO, handle error somehow
    } loc:loc];
    lastupdate = [[NSDate date] timeIntervalSince1970];
}

-(void)polling:(NSTimer*)timer {
    if(st == PollingModeUser) {
        [[ZegoAPIManager sharedManager] getUser:^(User *resp) {
            if(resp) {
                _user = resp;
                if(map) {
                    CenterMapViewController* m = (CenterMapViewController*)map;
                    [m userUpdated:resp];
                    
                }
            }
        } failure:^(RKObjectRequestOperation *operation, NSError *error) {
            // silent failure
        } withId:idx];
    } else if(st == PollingModeRequest){
        [[ZegoAPIManager sharedManager] getRequest:^(RideRequest *resp) {
            _request = resp;
            if(resp) {
                CenterMapViewController* m = (CenterMapViewController*)map;
                [m requestUpdated:_request];
            }
        } failure:^(RKObjectRequestOperation *operation, NSError *error) {
            // silent failure
        } withId:idx];
    }
    
    if(![CLLocationManager locationServicesEnabled]) {
        [[NSNotificationCenter defaultCenter] postNotificationName:gpsAuthorizationDenied object:nil];
    }
    
    if(_user && [_user.umode isEqualToString:@"driver"] && ([[NSDate date] timeIntervalSince1970] - lastupdate) > 3*lastUpMin) {
        [self postLocation];
    }

}

-(void)changePollingMode:(NSUInteger)mode withId:(NSInteger)uid {
    st = mode;
    idx = uid;
    _user = nil;
    _request = nil;
    [self polling:nil];
}


-(void)registerObserver:(UIViewController*)m {
    map = m;
    [(CenterMapViewController*)map locationUpdated:_currentLocation.coordinate];
    
    if(_user) {
        [(CenterMapViewController*)map userUpdated:_user];
    }
    
    if(_request) {
        [(CenterMapViewController*)map requestUpdated:_request];
    }
}

-(void)unregisterObserver:(UIViewController*)m {
    map = nil;
}

- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status {
    if (status == kCLAuthorizationStatusDenied || status == kCLAuthorizationStatusRestricted) {
        // The user denied authorization
        [[NSNotificationCenter defaultCenter] postNotificationName:gpsAuthorizationDenied object:nil];
    }
    else if (status == kCLAuthorizationStatusAuthorizedAlways || status == kCLAuthorizationStatusAuthorizedWhenInUse) {
        // The user accepted authorization
        [[NSNotificationCenter defaultCenter] postNotificationName:gpsAuthorizationGranted object:nil];
    }
}

-(void)revalidateGPS {
    CLAuthorizationStatus status = [CLLocationManager authorizationStatus];
    if (status == kCLAuthorizationStatusDenied || status == kCLAuthorizationStatusRestricted) {
        // The user denied authorization
        [[NSNotificationCenter defaultCenter] postNotificationName:gpsAuthorizationDenied object:nil];
    }
    else if (status == kCLAuthorizationStatusAuthorizedAlways || status == kCLAuthorizationStatusAuthorizedWhenInUse) {
        // The user accepted authorization
        [[NSNotificationCenter defaultCenter] postNotificationName:gpsAuthorizationGranted object:nil];
    }    
}

-(void)updateNow {
    [self polling:nil];
}

-(void)updateWithLocationHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    if(st == PollingModeUser) {
        [[ZegoAPIManager sharedManager] getUser:^(User *resp) {
            if(resp) {
                _user = resp;
                if(map) {
                    CenterMapViewController* m = (CenterMapViewController*)map;
                    [m userUpdated:resp];
                    
                }
            }
            completionHandler(UIBackgroundFetchResultNewData);
        } failure:^(RKObjectRequestOperation *operation, NSError *error) {
            // silent failure
            completionHandler(UIBackgroundFetchResultFailed);
        } withId:idx];
    } else if(st == PollingModeRequest){
        [[ZegoAPIManager sharedManager] getRequest:^(RideRequest *resp) {
            _request = resp;
            if(resp) {
                CenterMapViewController* m = (CenterMapViewController*)map;
                [m requestUpdated:_request];
            }
            completionHandler(UIBackgroundFetchResultNewData);
        } failure:^(RKObjectRequestOperation *operation, NSError *error) {
            // silent failure
            completionHandler(UIBackgroundFetchResultFailed);
        } withId:idx];
    }
    
    if(![CLLocationManager locationServicesEnabled]) {
        [[NSNotificationCenter defaultCenter] postNotificationName:gpsAuthorizationDenied object:nil];
    }
    
}

-(void)changePollingFrequency:(NSInteger)freq {
    [polling invalidate];
    polling = [NSTimer scheduledTimerWithTimeInterval:freq/1.0f
                                               target:self
                                             selector:@selector(polling:)
                                             userInfo:nil
                                              repeats:YES];
}

-(void) configureForMode:(NSString *)mode {
    if([mode isEqualToString:@"rider"]) {
        [manager stopUpdatingLocation];
        manager.distanceFilter = kCLLocationAccuracyHundredMeters;
        manager.desiredAccuracy = kCLLocationAccuracyBest;
        lastUpMin = 120;
        [manager startUpdatingLocation];
    } else {
        [manager stopUpdatingLocation];
        manager.distanceFilter = kCLDistanceFilterNone;
        manager.desiredAccuracy = kCLLocationAccuracyBestForNavigation;
        lastUpMin = 5;
        [manager startUpdatingLocation];
    }
}


-(User*)loggedUser {
    NSString* ju = [[NSUserDefaults standardUserDefaults] valueForKey:@"zegouser"];
    if(ju)
    {
        User* shu = [[User alloc] initWithJSONData:[ju dataUsingEncoding:NSUTF8StringEncoding]];
        return shu;
    }
    else{
        return nil;
    }
}
@end
