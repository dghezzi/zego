//
//  SignupLoginChooserViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "SignupLoginChooserViewController.h"

@interface SignupLoginChooserViewController ()

@end

@implementation SignupLoginChooserViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    _signup.titleLabel.font = [self boldFontWithSize:20];
    _signup.layer.borderColor = ZEGOGREEN.CGColor;
    _signup.layer.borderWidth = 1;
    _signup.layer.cornerRadius = 5;
    _login.titleLabel.font = [self boldFontWithSize:20];
    _login.layer.borderColor = ZEGOGREEN.CGColor;
    _login.layer.borderWidth = 1;
    _login.layer.cornerRadius = 5;
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)nextAction:(id)sender {
    
}


#pragma mark - Navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {

    
}


@end
