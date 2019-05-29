//
//  SplashViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "SplashViewController.h"

@interface SplashViewController ()

@end

@implementation SplashViewController

- (void)viewDidLoad {
    [super viewDidLoad];    
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidLoad];
    [[ZegoAPIManager sharedManager] globalConf:^(NSMutableArray *resp) {
        if(resp) {
            NSUserDefaults* udef = [NSUserDefaults standardUserDefaults];
            for(Conf* cc in resp) {
                [udef setValue:cc.val forKey:cc.k];
            }
            [udef synchronize];
        }
        [self startZego];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        ErrorResponse* e = [self handleError:error];
        if(e) {
            switch (e.code) {
                case 105: // app outdated
                    [self showOutdatedDialog];
                    break;
                    
                default:
                    [self startZego];
                    break;
            }
        } else {
            [self startZego];
        }
    } withRequest:[self rawBootRequest]];
}

-(void)showOutdatedDialog {
    SCLAlertView *alert = [[SCLAlertView alloc] init];
    
    alert.shouldDismissOnTapOutside = NO;
    [alert showCustom:self image:[UIImage imageNamed:@"icon-update-w"] color:ZEGOGREEN title:NSLocalizedString(@"Aggiornamento",nil) subTitle:NSLocalizedString(@"Clicca OK per scaricare la nuova versione dell'app.",nil) closeButtonTitle:NSLocalizedString(@"Ok",nil) duration:0.0f]; // Custom
    
    //Using Block
    [alert alertIsDismissed:^{
        NSString *simple = @"itms-apps://itunes.apple.com/app/id843265537";
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:simple]];
    }];
    
   
}
-(void) startZego {
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
    BOOL showTutorial = NO;// uncomment here to restore tutorial ![def valueForKey:@"tutorial.seen"];
    
    if(showTutorial) {
        [def setValue:@"ok" forKey:@"tutorial.seen"];
        [def synchronize];
        [self performSegueWithIdentifier:@"tutorialSegue" sender:nil];
    } else {
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1000 * NSEC_PER_MSEC)), dispatch_get_main_queue(), ^{
            User* u = [self loggedUser];
            if(u.uid>0 && u.zegotoken) {
                
                BootRequest* br = [self rawBootRequest];
                br.country = u.country;
                br.prefix = u.prefix;
                br.mobile = u.mobile;
                br.uid = u.uid;
                br.fbid = u.fbid;
                
                [[ZegoAPIManager sharedManager] setAccessToken:u.zegotoken];
                [[ZegoAPIManager sharedManager] silentLogin:^(User *resp) {
                    if(resp) {
                        
                        [self updateUser:resp];
                        [self performSegueWithIdentifier:@"silentLoginSegue" sender:self];
                        [self startPollingWithUser:resp];
                    }
                } failure:^(RKObjectRequestOperation *operation, NSError *error) {
                    [self performSegueWithIdentifier:@"signupLoginSwitchSegue" sender:nil];
                } withRequest:br];
            } else {
                [self performSegueWithIdentifier:@"signupLoginSwitchSegue" sender:nil];
            }
        });
    }
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    
}


@end
