//
//  DriverFeedbackView.m
//  Letzgo
//
//  Created by Luca Adamo on 02/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "DriverFeedbackView.h"

@implementation DriverFeedbackView

-(void)baseInit {
    UIView* top = [[UIView alloc] initWithFrame:CGRectMake(0, 5, w, 80)];
    top.layer.borderColor = [UIColor lightGrayColor].CGColor;
    top.layer.borderWidth = 0.5;
    top.backgroundColor = [UIColor whiteColor];
    
    UIImageView* img = [[UIImageView alloc] initWithFrame:CGRectMake(5, 0, 90, 90)];
    img.layer.cornerRadius = 45;
    img.clipsToBounds = YES;
    img.layer.borderColor = ZEGOGREEN70.CGColor;
    img.layer.borderWidth = 0.5;
    img.contentMode = UIViewContentModeScaleAspectFit;
    [img setImageWithURL:[NSURL URLWithString:request.rider.userimg] placeholderImage:[UIImage imageNamed:@"userplaceholder"]];
    [self addSubview:img];
    
    UILabel* name = [[UILabel alloc] initWithFrame:CGRectMake(110, 3, w-110, 37)];
    name.font = [self boldFontWithSize:16];
    name.text = [NSString stringWithFormat:@"Dai un voto a %@ per questo viaggio",request.rider.name];
    name.adjustsFontSizeToFitWidth = YES;
    name.minimumScaleFactor = 0.5f;
    name.numberOfLines = 2;
    name.hidden = YES;
    name.textAlignment = NSTextAlignmentCenter;
    [top addSubview:name];
    
    /*
     UILabel* cta = [[UILabel alloc] initWithFrame:CGRectMake(110, 25, w-110, 17)];
     cta.font = [self lightFontWithSize:14];
     cta.textColor = ZEGODARKGREY;
     cta.text = [NSString stringWithFormat:@"Dai un voto a %@ per questo passaggio",request.driver.name];
     cta.textAlignment = NSTextAlignmentCenter;
     [top addSubview:cta];
     */
    starRatingView = [[HCSStarRatingView alloc] initWithFrame:CGRectMake(125, 25, w-135, 30)];
    starRatingView.maximumValue = 5;
    starRatingView.minimumValue = 1;
    starRatingView.value = 5;
    starRatingView.tintColor = ZEGOGREEN;
    [starRatingView addTarget:self action:@selector(didChangeValue:) forControlEvents:UIControlEventValueChanged];
    [top addSubview:starRatingView];
    [self addSubview:top];
    feedbackButton = [self buttonWithFrame:CGRectMake(0, 100, w, 50) selector:@selector(feedback) andTitle:NSLocalizedString(@"Invia Feedback",nil)];
    [self enableButton:feedbackButton];
    [self bringSubviewToFront:img];
}

-(void)didChangeValue:(id)sender {
    //CGFloat val = ((HCSStarRatingView*)sender).value;
}

-(void)feedback {
    Feedback* f = [[Feedback alloc] init];
    f.sender = request.did;
    f.rid = request.rid;
    f.uid = request.pid;
    f.rating = starRatingView.value;
    
    [self.delegate driverFeedbackViewSubmitFeedback:f];
}

-(id) initWithFrame:(CGRect)frame andRequest:(RideRequest*)req {
    self = [super initWithFrame:frame];
    if(self) {
        request = req;
        [self baseInit];
    }
    return self;
}

@end
