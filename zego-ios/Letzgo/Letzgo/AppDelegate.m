//
//  AppDelegate.m
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "AppDelegate.h"
#import <Stripe/Stripe.h>
#import "ZegoAPIManager.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <AWSCore/AWSCore.h>
#import <AWSCognito/AWSCognito.h>



@import GoogleMaps;

@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    [[UIApplication sharedApplication] setMinimumBackgroundFetchInterval:10];
    
    // AppsFlyer
    [AppsFlyerTracker sharedTracker].appsFlyerDevKey = @"iaduS6foKokLycBsuNojri";
    [AppsFlyerTracker sharedTracker].appleAppID = @"843265537";
    [AppsFlyerTracker sharedTracker].currencyCode = @"EUR";
    
    // SETIP GMAPS
    [GMSServices provideAPIKey:@"AIzaSyDGJW08GItmlZSTosuQxQr2Cw6F_aLSM38"];
    
    // SETUP Facebook SDK
    [[FBSDKApplicationDelegate sharedInstance] application:application
                             didFinishLaunchingWithOptions:launchOptions];
    
    [[STPPaymentConfiguration sharedConfiguration] setPublishableKey:TEST == 0 ?@"pk_live_3ZiPA0QCpHcyi0c0eqGq1LmP" : @"pk_test_kWnzPvkiYyShQzLzLZnjpOZf"];
 
    [[ZegoAPIManager sharedManager] setupWithMappingFactory:[[ZegoMappingFactory alloc]init]];
    RKLogConfigureByName("RestKit/Network", RKLogLevelTrace);
    //RKLogConfigureByName("RestKit/ObjectMapping", RKLogLevelTrace);
    
    // Initialize the Amazon Cognito credentials provider
    
    AWSCognitoCredentialsProvider *credentialsProvider = [[AWSCognitoCredentialsProvider alloc]
                                                          initWithRegionType:AWSRegionEUWest1
                                                          identityPoolId:@"eu-west-1:a9805a9b-3f6d-4009-9ba7-75e1119a23e4"];
    
    AWSServiceConfiguration *configuration = [[AWSServiceConfiguration alloc] initWithRegion:AWSRegionEUWest1 credentialsProvider:credentialsProvider];
    
    [AWSServiceManager defaultServiceManager].defaultServiceConfiguration = configuration;
    
    // START Location Manager
    _locmanager = [LocationManager sharedManager];
    if(SYSTEM_VERSION_GRATERTHAN_OR_EQUALTO(@"10.0")){
        UNUserNotificationCenter *center = [UNUserNotificationCenter currentNotificationCenter];
        center.delegate = self;
        [center requestAuthorizationWithOptions:(UNAuthorizationOptionSound | UNAuthorizationOptionAlert | UNAuthorizationOptionBadge) completionHandler:^(BOOL granted, NSError * _Nullable error){
            if(!error){
                [[UIApplication sharedApplication] registerForRemoteNotifications];
            }
        }];
    }
    if ([application respondsToSelector:@selector(registerUserNotificationSettings:)]) {
        NSLog(@"Requesting permission for push notifications..."); // iOS 8
        UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:
                                                UIUserNotificationTypeAlert | UIUserNotificationTypeBadge |
                                                UIUserNotificationTypeSound categories:nil];
        [UIApplication.sharedApplication registerUserNotificationSettings:settings];
    } else {
        NSLog(@"Registering device for push notifications..."); // iOS 7 and earlier
        [UIApplication.sharedApplication registerForRemoteNotificationTypes:
         UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeBadge |
         UIRemoteNotificationTypeSound];
    }
    return YES;
}

- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
            options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {
    
    BOOL handled = [[FBSDKApplicationDelegate sharedInstance] application:application
                                                                  openURL:url
                                                        sourceApplication:options[UIApplicationOpenURLOptionsSourceApplicationKey]
                                                               annotation:options[UIApplicationOpenURLOptionsAnnotationKey]
                    ];
    return handled;
}


- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
}


- (void)applicationDidEnterBackground:(UIApplication *)application {
    User* u = [self loggedUser];
    if(u && [u.umode isEqualToString:@"driver"]) {
        [[UIApplication sharedApplication] setMinimumBackgroundFetchInterval:10];
    } else {
        [[UIApplication sharedApplication] setMinimumBackgroundFetchInterval:20];
    }
}


- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    [UIApplication sharedApplication].applicationIconBadgeNumber = 0;
    
    if(SYSTEM_VERSION_GRATERTHAN_OR_EQUALTO(@"10.0")){
        [[UNUserNotificationCenter currentNotificationCenter] removeAllPendingNotificationRequests];
    } else {
        [[UIApplication sharedApplication] cancelAllLocalNotifications];
    }
    [[UIApplication sharedApplication] setMinimumBackgroundFetchInterval:20];
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
     [FBSDKAppEvents activateApp];
     [[AppsFlyerTracker sharedTracker] trackAppLaunch];
}


- (void)applicationWillTerminate:(UIApplication *)application {
    
}


- (void)application:(UIApplication *)application
didRegisterUserNotificationSettings:(UIUserNotificationSettings *)settings
{
    NSLog(@"Registering device for push notifications..."); // iOS 8
    [application registerForRemoteNotifications];
}


- (void)application:(UIApplication *)application
didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)token
{
    NSLog(@"Registration successful, bundle identifier: %@, mode: %@, device token: %@",
          [NSBundle.mainBundle bundleIdentifier], [self modeString], token);
    
    NSString* tk = [self stringWithDeviceToken:token];
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
    [def setValue:tk forKey:@"pushid"];
    [def synchronize];
}

- (void)application:(UIApplication *)application
didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
{
    NSLog(@"Failed to register: %@", error);
}

- (void)application:(UIApplication *)application handleActionWithIdentifier:(NSString *)identifier
forRemoteNotification:(NSDictionary *)notification completionHandler:(void(^)())completionHandler
{
    NSLog(@"Received push notification: %@, identifier: %@", notification, identifier); // iOS 8
    completionHandler();
}

- (void)application:(UIApplication *)application
didReceiveRemoteNotification:(NSDictionary *)notification
{
    [[LocationManager sharedManager] updateNow];
}

-(void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    [[LocationManager sharedManager] updateWithLocationHandler:completionHandler];
    [UIApplication sharedApplication].applicationIconBadgeNumber = 0;
}
//Called when a notification is delivered to a foreground app.
-(void)userNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(UNNotificationPresentationOptions options))completionHandler{
        [[LocationManager sharedManager] updateNow];
    completionHandler(UNAuthorizationOptionSound | UNAuthorizationOptionAlert | UNAuthorizationOptionBadge);
}

//Called to let your app know which action was selected by the user for a given notification.
-(void)userNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void(^)())completionHandler{
    completionHandler();
    [[LocationManager sharedManager] updateNow];
}

- (NSString *)stringWithDeviceToken:(NSData *)deviceToken {
    const char *data = [deviceToken bytes];
    NSMutableString *token = [NSMutableString string];
    
    for (NSUInteger i = 0; i < [deviceToken length]; i++) {
        [token appendFormat:@"%02.2hhX", data[i]];
    }
    
    return [token copy];
}


-(void) application:(UIApplication *)application performFetchWithCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler
{
    [[LocationManager sharedManager] updateWithLocationHandler:completionHandler];
}

- (NSString *)modeString
{
#if DEBUG
    return @"Development (sandbox)";
#else
    return @"Production";
#endif
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
