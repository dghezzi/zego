//
//  SingleRideDetailViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 17/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "SingleRideDetailViewController.h"

@interface SingleRideDetailViewController ()

@end

@implementation SingleRideDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [_backView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(goBack)]];
    _topTitle.font = [self boldFontWithSize:24];
    [[self navigationController] setNavigationBarHidden:YES];
    
    [_web loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:[NSString stringWithFormat:(TEST == 1 ? @"http://testdata.zegoapp.com/details/%@/%@/%@" : @"http://data.zegoapp.com/details/%@/%@/%@"),_umode,_request.shid,[self loggedUser].zegotoken]]]];
    _topTitle.text = [NSString stringWithFormat:NSLocalizedString(@"Dettagli #%@",nil),_request.shortid];
    [_supporto addTarget:self action:@selector(support:) forControlEvents:UIControlEventTouchUpInside];

    _backView.isAccessibilityElement = YES;
    _backView.accessibilityLabel = @"Indietro";
    _backView.accessibilityHint = @"Indietro";
    _backView.accessibilityTraits = UIAccessibilityTraitButton;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)support:(id)sender {
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"mailto:support@zegoapp.com"]];
}

@end
