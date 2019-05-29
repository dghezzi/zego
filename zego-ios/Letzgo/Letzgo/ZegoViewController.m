//
//  ZegoViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"
#import <CommonCrypto/CommonDigest.h>
#include <sys/sysctl.h>
#import "SplashViewController.h"
#import "CenterMapViewController.h"
#import "LeftMenuViewController.h"
#import "ZegoSidePanelController.h"
#import <UIAlertController+Blocks.h>
#import <UserNotifications/UserNotifications.h>

@interface ZegoViewController ()

@end

@implementation ZegoViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    if(SYSTEM_VERSION_GRATERTHAN_OR_EQUALTO(@"10.0")){
        [[UNUserNotificationCenter currentNotificationCenter] removeAllPendingNotificationRequests];
    } else {
        [[UIApplication sharedApplication] cancelAllLocalNotifications];
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)goBack {
    [self.navigationController popViewControllerAnimated:YES];
}

-(UIFont*)lightFontWithSize:(CGFloat)sz {
    return [UIFont fontWithName:@"Raleway-Light" size:sz];
}

-(UIFont*)italicFontWithSize:(CGFloat)sz {
    return [UIFont fontWithName:@"Raleway-Italic" size:sz];
}

-(UIFont*)boldFontWithSize:(CGFloat)sz {
    return [UIFont fontWithName:@"Raleway-SemiBold" size:sz];
}

-(UIFont*)regularFontWithSize:(CGFloat)sz {
    return [UIFont fontWithName:@"Raleway-Regular" size:sz];
}

-(NSString*)userCountryCode
{
    NSLocale *currentLocale = [NSLocale currentLocale];
    NSString *countryCode = [currentLocale objectForKey:NSLocaleCountryCode];
    return countryCode;
}


-(BOOL)validateEmail:(NSString*)email
{
    NSString *emailRegex =
    @"[^@]+@[^.@]+(\\.[^.@]+)+";
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES[c] %@", emailRegex];
    
    return [emailTest evaluateWithObject:email];
}

-(void)updateUser:(User*)u {
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];

    NSLog(@"Update USER called: %ld",u.uid);
    if(u) {
        NSString* ju = [u JSONString];
        [def setValue:ju forKey:@"zegouser"];
        [def synchronize];
    } else {
        [def setValue:nil forKey:@"zegouser"];
        [def synchronize];
    }
}

-(User*)loggedUser {
   
    NSString* ju = [[NSUserDefaults standardUserDefaults] valueForKey:@"zegouser"];
    if(ju)
    {
        User* shu = [[User alloc] initWithJSONData:[ju dataUsingEncoding:NSUTF8StringEncoding]];
        NSLog(@"Read USER called: %ld",shu.uid);
        return shu;
    }
    else{
        return nil;
    }
}

-(void)updateHud:(NSString*)str
{
    if(_hud)
    {
        _hud.label.text = str;
        _hud.backgroundColor = ZEGOGREEN70;
    }
}

-(void)startLoading:(NSString *)str
{
    _hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    _hud.label.text = str;
    [self.view bringSubviewToFront:_hud];
}

-(void)startLoading
{
    _hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    [self.view bringSubviewToFront:_hud];
}

-(void)stopLoading
{
    if(_hud)
    {
        [_hud hideAnimated:YES];
    }
}

- (NSString*)MD5:(NSString*)str
{
    if(str)
    {
        // Create pointer to the string as UTF8
        const char *ptr = [str UTF8String];
        
        // Create byte array of unsigned chars
        unsigned char md5Buffer[CC_MD5_DIGEST_LENGTH];
        
        // Create 16 byte MD5 hash value, store in buffer
        CC_MD5(ptr, strlen(ptr), md5Buffer);
        
        // Convert MD5 value in the buffer to NSString of hex values
        NSMutableString *output = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH * 2];
        for(int i = 0; i < CC_MD5_DIGEST_LENGTH; i++)
            [output appendFormat:@"%02x",md5Buffer[i]];
        
        return output;
    }
    else
    {
        return @"";
    }
}

-(ErrorResponse*)handleError:(NSError*)err
{
    [self stopLoading];
    ErrorResponse* sh = [[err.userInfo objectForKey:@"RKObjectMapperErrorObjectsKey"] firstObject];
    if(sh)
    {
        if(sh.code == 403) {
            [self showUnauthorized:sh.msg];
        }
        return sh;
    } else {
        //
        if([err.userInfo objectForKey:@"NSLocalizedDescription"]){
            
            [self showOfflineDialog:[err.userInfo objectForKey:@"NSLocalizedDescription"]];
        }
        return nil; // TODO Dialog?
    }
    
}
-(BootRequest*)rawBootRequest {
    BootRequest* br = [[BootRequest alloc] init];
    br.os = @"iOS";
    br.vos = [[UIDevice currentDevice] systemVersion];
    br.vapp = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"];
    br.device = [self getModel];
    br.deviceId = [[UIDevice currentDevice] identifierForVendor].UUIDString;
    br.pushid = [[NSUserDefaults standardUserDefaults] valueForKey:@"pushid"];
    return br;
}

-(NSString *)getModel {
    size_t size;
    sysctlbyname("hw.machine", NULL, &size, NULL, 0);
    char *model = malloc(size);
    sysctlbyname("hw.machine", model, &size, NULL, 0);
    NSString *deviceModel = [NSString stringWithCString:model encoding:NSUTF8StringEncoding];
    free(model);
    return deviceModel;
}

-(void)fetchFacebookUserInfo
{
    if ([FBSDKAccessToken currentAccessToken])
    {
        NSLog(@"Token is available : %@",[[FBSDKAccessToken currentAccessToken]tokenString]);
        NSLog(@"Token expiration date : %@",[[FBSDKAccessToken currentAccessToken]expirationDate]);
        
        [[[FBSDKGraphRequest alloc] initWithGraphPath:@"me" parameters:@{@"fields": @"id, name, link, first_name, last_name, picture.type(large), email, gender, birthday, bio ,location ,friends ,hometown , friendlists"}]
         startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
             if (!error)
             {
                 NSLog(@"resultis:%@",result);
                 
                 User* us = [self loggedUser];
                 us.email = [result valueForKey:@"email"];
                 us.fname = [result valueForKey:@"first_name"];
                 us.lname = [result valueForKey:@"last_name"];
                 us.gender = [result valueForKey:@"gender"];
                 us.birthdate = [result valueForKey:@"birthday"];
                 us.fbid = [result valueForKey:@"id"];
                 
                 [self updateUser:us];
                 
             }
             else
             {
                 NSLog(@"Error %@",error);
                 
             }
             
         }];
        
    }
}

-(void)fetchFacebookUserInfoWithHandler:(FBSDKGraphRequestHandler)handler {
    if ([FBSDKAccessToken currentAccessToken])
    {
        NSLog(@"Token is available : %@",[[FBSDKAccessToken currentAccessToken]tokenString]);
        NSLog(@"Token expiration date : %@",[[FBSDKAccessToken currentAccessToken]expirationDate]);
        
        [[[FBSDKGraphRequest alloc] initWithGraphPath:@"me" parameters:@{@"fields": @"id, name, link, first_name, last_name, picture.type(large), email, gender, birthday ,location ,hometown"}]
         startWithCompletionHandler:handler];
    }
}

-(void)uploadImageToS3:(UIImage*)image withSize:(CGSize)size andName:(NSString *)name{
    
    image = [image resizedImageWithContentMode:UIViewContentModeScaleAspectFit bounds:size interpolationQuality:kCGInterpolationDefault];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *filePath = [[paths objectAtIndex:0] stringByAppendingPathComponent:[NSString stringWithFormat:@".jpg"]];
    [UIImageJPEGRepresentation(image,0.8) writeToFile:filePath atomically:YES];
    
    NSURL* fileUrl = [NSURL fileURLWithPath:filePath];
    
    //upload the image
    AWSS3TransferManagerUploadRequest *uploadRequest = [AWSS3TransferManagerUploadRequest new];
    uploadRequest.body = fileUrl;
    uploadRequest.bucket = @"zegoapp";
    uploadRequest.key = name;
    uploadRequest.contentType = @"image/jpg";
    uploadRequest.ACL = AWSS3BucketCannedACLPublicRead;
    
    AWSS3TransferManager *transferManager = [AWSS3TransferManager defaultS3TransferManager];
    
    [[transferManager upload:uploadRequest] continueWithExecutor:[AWSExecutor mainThreadExecutor]
                                                       withBlock:^id(AWSTask *task) {
                                                           if (task.error != nil) {
                                                               NSLog(@"%s %@","Error uploading :", uploadRequest.key);
                                                           }
                                                           else { NSLog(@"Upload completed");
                                                               User* uu = [self loggedUser];
                                                               uu.img = [NSString stringWithFormat:@"https://s3-eu-west-1.amazonaws.com/zegoapp/%@",name];
                                                               [[ZegoAPIManager sharedManager] update:^(User *resp) {
                                                                   [self updateUser:uu];
                                                                   UINavigationController* presenter = (UINavigationController*)[self presentingViewController];
                                                                   
                                                                   LeftMenuViewController* left = nil;
                                                                   CenterMapViewController* map = nil;
                                                                   for(UIViewController* uvc in [presenter viewControllers]) {
                                                                       if([uvc isKindOfClass:[ZegoSidePanelController class]]) {
                                                                           UINavigationController* l = ((UINavigationController*)((ZegoSidePanelController*)uvc).leftPanel);
                                                                           for(UIViewController* uvc in [l viewControllers]) {
                                                                               if([uvc isKindOfClass:[LeftMenuViewController class]]) {
                                                                                   left = (LeftMenuViewController*)uvc;
                                                                               }
                                                                           }
                                                                           map = ((CenterMapViewController*)((ZegoSidePanelController*)uvc).centerPanel);
                                                                           NSLog(@"");
                                                                       }
                                                                   }
                                                                   
                                                                   left.userimg.image = image;
                                                                   map.userImg.image = image;
                                                                   
                                                                   [[self presentingViewController] dismissViewControllerAnimated:YES completion:^{
                                                                       
                                                                   }];
                                                                   [self stopLoading];
                                                               } failure:^(RKObjectRequestOperation *operation, NSError *error) {
                                                                   [self stopLoading];
                                                                   // TODO show dialog upload failure?
                                                               } withRequest:uu];
                                                           }
                                                           return nil;
                                                       }];
}
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    
}

-(void)logoutAction {
    
    if([self loggedUser].rtstatus % 100 != 0) {
        SCLAlertView *alert = [[SCLAlertView alloc] init];
        alert.shouldDismissOnTapOutside = YES;
        
        [alert showCustom:self image:[UIImage imageNamed:@"icon-update-w"] color:ZEGOGREEN title:NSLocalizedString(@"Attenzione",nil) subTitle:NSLocalizedString(@"Non puoi eseguire il logout in questo momento.",nil) closeButtonTitle:NSLocalizedString(@"Chiudi",nil) duration:0.0f]; // Custom
        
        
        //Using Block
        [alert alertIsDismissed:^{
            
        }];
    }
    else {
        [UIAlertController showAlertInViewController:self
                                           withTitle:@""
                                             message:NSLocalizedString(@"Sei sicuro di voler uscire da Zego?",nil)
                                   cancelButtonTitle:NSLocalizedString(@"Annulla",nil)
                              destructiveButtonTitle:NSLocalizedString(@"Logout",nil)
                                   otherButtonTitles:nil
                                            tapBlock:^(UIAlertController *controller, UIAlertAction *action, NSInteger buttonIndex){
                                                
                                                if (buttonIndex == controller.cancelButtonIndex) {
                                                    
                                                } else if (buttonIndex == controller.destructiveButtonIndex) {
                                                    [self exit];
                                                    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
                                                    [def setValue:nil forKey:@"zegouser"];
                                                    [def synchronize];

                                                }
                                            }];
    }
}

-(void)exit {
    
    [[ZegoAPIManager sharedManager] kill:^(User *resp) {
        // DO NOTHING
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        // DO NOTHING
    } withRequest:[self loggedUser]];
    
    UINavigationController* nav =[self navigationController];
    if([self sidePanelController]) {
        nav = [[[self sidePanelController] centerPanel] navigationController];
    }
    
    for(UIViewController* uvc in [nav viewControllers]) {
        if([uvc isKindOfClass:[SplashViewController class]]){
            NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
            [def setValue:nil forKey:@"zegouser"];
            [def synchronize];
            [nav popToViewController:uvc animated:YES];
        }
    }
    
}
-(void)showOfflineDialog:(NSString*)msg {
    [UIAlertController showAlertInViewController:self
                                       withTitle:@""
                                         message:msg
                               cancelButtonTitle:@"Ok"
                          destructiveButtonTitle:nil
                               otherButtonTitles:nil
                                        tapBlock:^(UIAlertController *controller, UIAlertAction *action, NSInteger buttonIndex){
                                            
                                            if (buttonIndex == controller.cancelButtonIndex) {
                                                
                                            }
                                        }];
}


-(void)showUnauthorized:(NSString*)msg {
    [UIAlertController showAlertInViewController:self
                                       withTitle:@""
                                         message:msg
                               cancelButtonTitle:@"Ok"
                          destructiveButtonTitle:nil
                               otherButtonTitles:nil
                                        tapBlock:^(UIAlertController *controller, UIAlertAction *action, NSInteger buttonIndex){
                                            
                                            if (buttonIndex == controller.cancelButtonIndex) {
                                                [self exit];
                                            }
                                        }];
}

-(void)checkRevalidateTCDialog {
    if(![self loggedUser].tcok || [self loggedUser].tcok == 0) {
        
        SCLAlertView *alert = [[SCLAlertView alloc] init];
        alert.shouldDismissOnTapOutside = YES;
        [alert addButton:NSLocalizedString(@"Leggi",nil) actionBlock:^(void) {
            [self flagTcComplete:TRUE];
        }];
        
        [alert showNotice:self title:NSLocalizedString(@"Attenzione",nil) subTitle:NSLocalizedString(@"Abbiamo aggiornato Termini e Condizioni  e Privacy Policy.",nil) closeButtonTitle:NSLocalizedString(@"Ok",nil) duration:0.0f];
        
        // Using Block
        [alert alertIsDismissed:^{
            [self flagTcComplete:FALSE];
        }];
        
    }
}

-(void)flagTcComplete:(BOOL)navigate{
    User* u = [self loggedUser];
    u.tcok = 1;
    [[ZegoAPIManager sharedManager] update:^(User *resp) {
        if(resp) {
            [self updateUser:resp];
            if(navigate) {
                [[UIApplication sharedApplication] openURL:[NSURL URLWithString:NSLocalizedString(@"http://www.zegoapp.com/it/termini-e-condizioni",nil)]];
            }
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        //
    } withRequest:u];
}

-(void)startPollingWithUser:(User*)resp {
    if(resp.current && resp.current !=0) {
        [[LocationManager sharedManager] changePollingMode:PollingModeRequest withId:resp.current];
    } else {
        [[LocationManager sharedManager] changePollingMode:PollingModeUser withId:resp.uid];
    }
}

-(NSMutableArray*)arrayWithType:(NSString*)t {
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
    NSMutableArray* ret = [[NSMutableArray alloc] init];
    NSString* ct = [[self loggedUser].country lowercaseString];
    if([ct isEqualToString:@"it"]) {
        
    } else {
        ct = @"en";
    }
    for(int i = 1; i<5; i++){
        NSString* key = [NSString stringWithFormat:@"%@%d.%@",t,i,ct];
        NSString* val = [def valueForKey:key];
        if(val) {
            [ret addObject:val];
        }
    }
    return ret;
}

-(void)navigateTo:(CLLocationCoordinate2D)coordinate {
    if ([[UIApplication sharedApplication] canOpenURL:
         [NSURL URLWithString:@"comgooglemaps://"]])
    {
        NSString *urlString=[NSString stringWithFormat:@"comgooglemaps://?daddr=%f,%f&directionsmode=driving",coordinate.latitude,coordinate.longitude];
        [[UIApplication sharedApplication] openURL:
         [NSURL URLWithString:urlString]];
    }
    else
    {
        NSString *string = [NSString stringWithFormat:@"http://maps.apple.com/?daddr=%f,%f&dirflg=d",coordinate.latitude,coordinate.longitude];
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:string]];
    }
}

-(NSString*)prefixForCountry:(NSString*)iso {
    NSDictionary *dictionary = [NSDictionary dictionaryWithContentsOfFile:[[NSBundle mainBundle]
                                                                           pathForResource:@"config" ofType:@"plist"]];
    NSArray* ct = [dictionary objectForKey:@"countries"];
    NSArray* pf = [dictionary objectForKey:@"prefixes"];
    
    return [pf objectAtIndex:[ct indexOfObject:iso]];
}
@end
