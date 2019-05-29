//
//  InfoViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 22/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "InfoViewController.h"
#import "TutorialViewController.h"

@interface InfoViewController ()

@end

@implementation InfoViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [_backView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(goBack)]];
    _topTitle.font = [self boldFontWithSize:24];
    
    _tutorial.font = [self italicFontWithSize:26];
    _zegoapp.font = [self italicFontWithSize:26];
    _termsAndCond.font = [self italicFontWithSize:26];
    _faq.font = [self italicFontWithSize:26];
    _privacy.font = [self italicFontWithSize:26];
    _contattaci.font = [self italicFontWithSize:26];
    _version.font = [self boldFontWithSize:14];
    
    [_tutorial addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tutorialTap:)]];
    _tutorial.text = NSLocalizedString(@"Tutorial", nil);
    _tutorial.userInteractionEnabled = YES;
    [_zegoapp addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(websiteTap:)]];
    _zegoapp.text = NSLocalizedString(@"Sito web Zego", nil);
    _zegoapp.userInteractionEnabled = YES;
    [_termsAndCond addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(termsTap:)]];
    _termsAndCond.text = NSLocalizedString(@"Info Legali", nil);
    _termsAndCond.userInteractionEnabled = YES;
    [_faq addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(faq:)]];
    _faq.text = NSLocalizedString(@"FAQ", nil);
    _faq.userInteractionEnabled = YES;
    
    [_contattaci addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(contatti:)]];
    _contattaci.userInteractionEnabled = YES;
    _contattaci.text = NSLocalizedString(@"Contattaci", nil);
    
    [_privacy addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(privacy:)]];
    _privacy.userInteractionEnabled = YES;
    _privacy.text = NSLocalizedString(@"Privacy", nil);
    
    _version.text = [NSString stringWithFormat:@"Zego v. %@",[self rawBootRequest].vapp];
    
    _backView.isAccessibilityElement = YES;
    _backView.accessibilityLabel = @"Indietro";
    _backView.accessibilityHint = @"Indietro";
    _backView.accessibilityTraits = UIAccessibilityTraitButton;
    
    _tutorial.hidden = YES; // remove this line to restore link to tutorial
}

- (void) tutorialTap:(UIGestureRecognizer*)sender {

    NSString * storyboardName = @"Main";
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:storyboardName bundle: nil];
    TutorialViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"tutorialController"];
    [self presentViewController:vc animated:YES completion:nil];
}

- (void) websiteTap:(UIGestureRecognizer*)sender {
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"http://www.zegoapp.com"] ];
}

- (void) termsTap:(UIGestureRecognizer*)sender {
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"http://www.zegoapp.com/it/termini-e-condizioni"]];
}

- (void) faq:(UIGestureRecognizer*)sender {
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"http://www.zegoapp.com/it/faq"]];
}

- (void) privacy:(UIGestureRecognizer*)sender {
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"http://www.zegoapp.com/it/privacy"]];
}


- (void) contatti:(UIGestureRecognizer*)sender {
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"mailto:support@zegoapp.com"]];
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
