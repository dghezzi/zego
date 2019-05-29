//
//  PassengerWaitingAnswerView.m
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "PassengerWaitingAnswerView.h"

@implementation PassengerWaitingAnswerView


-(void)baseInit {
    UIView* top = [[UIView alloc] initWithFrame:CGRectMake(0, 0, w, 50)];
    top.backgroundColor = [UIColor whiteColor];
    UILabel* lb = [[UILabel alloc] initWithFrame:CGRectMake(50, 0, w-100, 50)];
    lb.numberOfLines = 2;
    lb.font = [self regularFontWithSize:14];
    lb.textAlignment = NSTextAlignmentCenter;
    lb.text = NSLocalizedString(@"Stiamo cercando un driver disponibile.\nCon ZEGO ci si siede davanti.",nil);
    [top addSubview:lb];
    UIView* rtop = [[UIView alloc] initWithFrame:CGRectMake(w-50, 10, 30, 30)];
    MBProgressHUD* bhud = [MBProgressHUD showHUDAddedTo:rtop animated:YES];
    bhud.mode = MBProgressHUDModeIndeterminate;
    bhud.color = [UIColor clearColor];
    [bhud setAlpha:0.7];
    bhud.activityIndicatorColor = ZEGOGREEN;
    [self addSubview:top];
    [top addSubview:rtop];
    
    UIView* bottom = [[UIView alloc] initWithFrame:CGRectMake(0, 60, w, 50)];
    bottom.backgroundColor = [UIColor whiteColor];
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    [button addTarget:self
               action:@selector(cancel)
     forControlEvents:UIControlEventTouchUpInside];
    [button setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
    [button setTitle:NSLocalizedString(@"Cancella Richiesta",nil) forState:UIControlStateNormal];
    button.frame = CGRectMake((w-200)/2, 0, 200, 50);
    [self addSubview:bottom];
    [bottom addSubview:button];
}


-(id)initWithFrame:(CGRect)frame andRequest:(RideRequest*)req {
    self = [super initWithFrame:frame];
    if(self) {
        request = req;
        [self baseInit];
    }
    return self;
}

-(void)cancel{
    [self.delegate passengerWaitingAnswerView:self canceledRequest:request];
}
@end
