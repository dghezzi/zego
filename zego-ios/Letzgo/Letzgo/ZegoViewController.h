//
//  ZegoViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//
#import <UIKit/UIKit.h>
#import "ZegoAPIManager.h"
#import <MBProgressHUD/MBProgressHUD.h>
#import <Toast/UIView+Toast.h>
#import "UIViewController+JASidePanel.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <SCLAlertView-Objective-C/SCLAlertView.h>
#import <UIImage+Resize.h>
#import "JASidePanelController.h"
#import <UIViewController+JASidePanel.h>
#import <AWSS3.h>
#import <SDWebImage/SDImageCache.h>
#import "LocationManager.h"
#import "SoundEffect.h"

#define ZEGOGREEN [UIColor colorWithRed:0 green:189/255.f blue:156/255.f alpha:1]
#define ZEGOPINK [UIColor colorWithRed:227/255.f green:95/255.f blue:184/255.f alpha:1]
#define ZEGOCONTROL [UIColor colorWithRed:231/255.f green:55/255.f blue:24/255.f alpha:1]
#define ZEGODARKGREEN [UIColor colorWithRed:0 green:117/255.f blue:98/255.f alpha:1]
#define ZEGOGREEN70 [UIColor colorWithRed:0 green:189/255.f blue:156/255.f alpha:0.7]
#define ZEGODARKGREY [UIColor colorWithRed:129/255.f green:129/255.f blue:129/255.f alpha:1]
#define BLUFACEBOOK [UIColor colorWithRed:59/255.0 green:89/255.f blue:152/255.f alpha:1]

#define IS_IPAD (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad)
#define IS_IPHONE (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
#define IS_RETINA ([[UIScreen mainScreen] scale] >= 2.0)

#define SCREEN_WIDTH ([[UIScreen mainScreen] bounds].size.width)
#define SCREEN_HEIGHT ([[UIScreen mainScreen] bounds].size.height)
#define SCREEN_MAX_LENGTH (MAX(SCREEN_WIDTH, SCREEN_HEIGHT))
#define SCREEN_MIN_LENGTH (MIN(SCREEN_WIDTH, SCREEN_HEIGHT))

#define IS_IPHONE_4_OR_LESS (IS_IPHONE && SCREEN_MAX_LENGTH < 568.0)
#define IS_IPHONE_5 (IS_IPHONE && SCREEN_MAX_LENGTH == 568.0)
#define IS_IPHONE_6 (IS_IPHONE && SCREEN_MAX_LENGTH == 667.0)
#define IS_IPHONE_6P (IS_IPHONE && SCREEN_MAX_LENGTH == 736.0)
#define IS_IPAD_PRO (IS_IPAD && SCREEN_MAX_LENGTH == 1366.0)

@interface ZegoViewController : UIViewController
{
    
}
@property(nonatomic,strong) MBProgressHUD* hud;

-(void)goBack;

-(UIFont*)lightFontWithSize:(CGFloat)sz;
-(UIFont*)italicFontWithSize:(CGFloat)sz;
-(UIFont*)boldFontWithSize:(CGFloat)sz;
-(UIFont*)regularFontWithSize:(CGFloat)sz;
-(BOOL)validateEmail:(NSString*)email;

-(void)updateUser:(User*)u;
-(User*)loggedUser;
-(void)checkRevalidateTCDialog;


-(void)startLoading:(NSString*)str;
-(void)startLoading;
-(void)updateHud:(NSString*)str;
-(void)stopLoading;
-(void)logoutAction;

-(ErrorResponse*)handleError:(NSError*)err;
-(BootRequest*)rawBootRequest;
-(void)startPollingWithUser:(User*)resp;
-(void)fetchFacebookUserInfo;
-(void)fetchFacebookUserInfoWithHandler:(FBSDKGraphRequestHandler)handler;

-(void)uploadImageToS3:(UIImage*)image withSize:(CGSize)size andName:(NSString*)name;
-(NSMutableArray*)arrayWithType:(NSString*)t;
-(void)navigateTo:(CLLocationCoordinate2D)coordinate;
-(void)playShort;
-(void)playLong;
-(NSString*)prefixForCountry:(NSString*)iso;
@end
