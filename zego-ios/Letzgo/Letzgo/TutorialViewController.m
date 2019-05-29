//
//  TutorialViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 21/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "TutorialViewController.h"
#import "SplashViewController.h"

@interface TutorialViewController ()

@end

@implementation TutorialViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self startLoading];
    _web.delegate = self;
    [_web loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:@"http://data.zegoapp.com/tutorial/it.html"]]];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1500 * NSEC_PER_MSEC)), dispatch_get_main_queue(), ^{
        [self stopLoading];
    });
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];

}

-(IBAction)skip:(id)sender {
    [self close];
}

-(BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {
    return YES;
}

-(void) webViewDidFinishLoad:(UIWebView *)webView {
    [self stopLoading];
}

-(void) webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error {
    [self stopLoading];
}

-(void)close {
    [self.presentingViewController dismissViewControllerAnimated:YES completion:^{
       
    }];
}

#pragma mark - Navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {

}


@end
