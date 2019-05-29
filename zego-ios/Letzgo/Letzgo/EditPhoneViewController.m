//
//  EditPhoneViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 21/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "EditPhoneViewController.h"
#import "InsertPinViewController.h"

@interface EditPhoneViewController ()

@end

@implementation EditPhoneViewController

- (void)viewDidLoad {
    [super viewDidLoad];
     [self navigationController].navigationBarHidden = YES;
    User* u = [self loggedUser];
    if(u.prefix && u.mobile && u.country) {
        self.selCountry = [u.country uppercaseString];
        self.prefix.text = u.prefix;
        self.mobile.text = u.mobile;
        self.flag.image = [UIImage imageNamed:self.selCountry];
        self.currentMobile = [NSString stringWithFormat:@"%@",self.mobile.text];
    }
    [self evaluateMobileData];
    
    
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
   
}

-(void)loginFailedWithErrorMessage:(NSString*)err {
    self.fieldMessage.textColor = [UIColor redColor];
    self.fieldMessage.text = err;
    self.next.backgroundColor = ZEGODARKGREY;
    self.next.enabled = NO;
}

-(void)viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)saveAction:(id)sender {
    [self startLoading];
    [[ZegoAPIManager sharedManager] getUser:^(User *resp) {
        if(resp) {
            [self updateUser:resp];
            
            resp.prefix = self.prefix.text;
            resp.mobile = self.mobile.text;
            resp.country = self.selCountry;
            
            
            [[ZegoAPIManager sharedManager] update:^(User *resp) {
                if(resp) {
                    [self updateUser:resp];
                    
                    if(resp.mobok == 1) {
                        [self goBack];
                    } else {
                        NSString * storyboardName = @"Main";
                        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:storyboardName bundle: nil];
                        InsertPinViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"pinController"];
                        vc.currentMobile = self.currentMobile;
                        vc.delegate = self;
                        [self presentViewController:vc animated:YES completion:nil];
                    }
                }
                [self stopLoading];
            } failure:^(RKObjectRequestOperation *operation, NSError *error) {
                if(error) {
                    ErrorResponse* e = [self handleError:error];
                    if(e) {
                        [self loginFailedWithErrorMessage:e.msg];
                    }
                }
                [self stopLoading];
            } withRequest:resp];
            
        }
        [self stopLoading];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            [self handleError:error];
        }
        [self stopLoading];
    } withId:[self loggedUser].uid];
}

-(void) pinValidatedCorrectly {
    [self goBack];
}

-(void) goBack {
    [super goBack];
}

- (void)countryController:(id)sender didSelectCountry:(EMCCountry *)chosenCountry;
{
    [super countryController:sender didSelectCountry:chosenCountry];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
