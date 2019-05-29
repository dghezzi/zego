//
//  TutorialViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 21/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"

@interface TutorialViewController : ZegoViewController<UIWebViewDelegate>

@property (strong, nonatomic) IBOutlet UIButton *next;
@property (strong, nonatomic) IBOutlet UIWebView *web;

-(IBAction)skip:(id)sender;
@end
