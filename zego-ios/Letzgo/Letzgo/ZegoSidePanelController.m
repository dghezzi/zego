//
//  ZegoSidePanelController.m
//  Letzgo
//
//  Created by Luca Adamo on 16/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoSidePanelController.h"

@interface ZegoSidePanelController ()

@end

@implementation ZegoSidePanelController


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.panningLimitedToTopViewController = NO;
    self.leftGapPercentage = 1;
    [[self navigationController] setNavigationBarHidden:YES];
}

-(void) viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [[self navigationController] setNavigationBarHidden:YES animated:NO];
}

-(UIViewController*) fetchRightPanel
{
    return [self rightPanel];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void) awakeFromNib
{
    [super awakeFromNib];
    [self setLeftPanel:[self.storyboard instantiateViewControllerWithIdentifier:@"leftViewController"]];
    [self setCenterPanel:[self.storyboard instantiateViewControllerWithIdentifier:@"centerMapViewController"]];
    self.shouldDelegateAutorotateToVisiblePanel = NO;
}

@end
