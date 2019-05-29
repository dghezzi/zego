//
//  AppDelegate.h
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LocationManager.h"
#import <UserNotifications/UserNotifications.h>
#import "AppsFlyerTracker.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate,UNUserNotificationCenterDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) LocationManager* locmanager;

@end

