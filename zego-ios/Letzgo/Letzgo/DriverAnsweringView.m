//
//  DriverAnsweringView.m
//  Letzgo
//
//  Created by Luca Adamo on 02/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "DriverAnsweringView.h"
#import "ThreeElementRideViewDriver.h"
#import "AddressField.h"

@implementation DriverAnsweringView

-(void)baseInit {
    [self addSubview:[[ThreeElementRideViewDriver alloc] initWithFrame:CGRectMake(0, 0, w, 50) request:request]];
    
    UIView* line = [[UIView alloc] initWithFrame:CGRectMake(0, 60, w, 80)];
    line.backgroundColor = [UIColor whiteColor];
    line.layer.borderColor = [UIColor lightGrayColor].CGColor;
    line.layer.borderWidth = 0.5;
    [self addSubview:line];
    
    UIImageView* userimg = [[UIImageView alloc] initWithFrame:CGRectMake(5, 55, 90, 90)];
    userimg.contentMode = UIViewContentModeScaleAspectFit;
    [userimg setImageWithURL:[NSURL URLWithString:request.rider.userimg] placeholderImage:[UIImage imageNamed:@"userplaceholder"]];
    
    userimg.layer.cornerRadius = 45;
    userimg.layer.masksToBounds = YES;
    [self addSubview:userimg];
    
    
    UILabel* name = [[UILabel alloc] initWithFrame:CGRectMake(100, 10, w-180, 25)];
    name.font = [self boldFontWithSize:18];
    name.text = request.rider.name;
    [line addSubview:name];
    
    UIImageView* rating = [[UIImageView alloc] initWithFrame:CGRectMake(100, 45, 100, 25)];
    rating.contentMode = UIViewContentModeScaleAspectFit;
    int s = ceil(request.rider.rating);
    if(s==0){
        s= 5;
    }
    rating.image = [UIImage imageNamed:[NSString stringWithFormat:@"s%d",s]];
    [line addSubview:rating];
    
    UIImageView* tp2 = [[UIImageView alloc] initWithFrame:CGRectMake(w-80, 15, 70, 50)];
    tp2.contentMode = UIViewContentModeScaleAspectFit;
    tp2.image = [UIImage imageNamed:[NSString stringWithFormat:@"p%d",request.numpass]];
    UIView* lev = [[UIView alloc] initWithFrame:CGRectMake(w-80, 65, 70, 10)];
    if(request.reqlevel > 0) {
        lev.backgroundColor = request.reqlevel == 2 ? [UIColor colorWithRed:202/255.f green:104/255.f blue:180/255.f alpha:1] : [UIColor colorWithRed:223/255.f green:112/255.f blue:48/255.f alpha:1];
        [line addSubview:lev];
    }
    [line addSubview:tp2];
    
    
    Address* fake = [[Address alloc] init];
    fake.address = request.puaddr;
    
    Address* fake2 = [[Address alloc] init];
    fake2.address = request.doaddr;
    [self addSubview:[[AddressField alloc] initWithFrame:CGRectMake(0, 160, w, 50) type:@"pickup" address:fake nav:NO editable:NO]];
   
    [self addSubview:[[AddressField alloc] initWithFrame:CGRectMake(0, 210, w, 50) type:@"dropoff" address:fake2 nav:NO editable:NO]];

    
    UIView* bottom = [[UIView alloc] initWithFrame:CGRectMake(0, 270, w*0.3f, 50)];
    bottom.backgroundColor = [UIColor whiteColor];
    reject = [UIButton buttonWithType:UIButtonTypeCustom];
    [reject addTarget:self
               action:@selector(rejectAction)
     forControlEvents:UIControlEventTouchUpInside];
    [reject setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
    [reject setTitle:NSLocalizedString(@"Rifiuta",nil) forState:UIControlStateNormal];
    reject.frame = CGRectMake(0, 0, w*0.3f, 50);
    [bottom addSubview:reject];
    [self addSubview:bottom];
    
    accept = [self buttonWithFrame:CGRectMake(w*0.3f, 270, w*0.7f, 50) selector:@selector(acceptAction) andTitle:NSLocalizedString(@"Accetta",nil)];
    [self enableButton:accept];
    
    [self bringSubviewToFront:userimg];
    [self addSeparatorAtQuota:160 full:YES];
    [self addSeparatorAtQuota:210 full:NO];
    [self addSeparatorAtQuota:260 full:YES];
     [self verticalConnectorAtQuota:191 lenght:38];
}

-(id)initWithFrame:(CGRect)frame andRequest:(RideRequest*)req {
    self = [super initWithFrame:frame];
    if(self) {
        request = req;
        [self baseInit];
    }
    return self;
}

-(void)acceptAction {
    [self.delegate driverAnsweringView:self acceptedRequest:YES];
}


-(void)rejectAction {
    [self.delegate driverAnsweringView:self acceptedRequest:NO];
}

@end
