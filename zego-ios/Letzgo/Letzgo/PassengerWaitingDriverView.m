//
//  PassengerWaitingDriverView.m
//  Letzgo
//
//  Created by Luca Adamo on 19/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "PassengerWaitingDriverView.h"

@implementation PassengerWaitingDriverView

-(void)baseInit {
    
    
    
}

-(id)initWithFrame:(CGRect)frame andRequest:(RideRequest *)req {
    self = [super initWithFrame:frame];
    if(self) {
        request = req;
        // line 1
        UIView* f1 = [[UIView alloc] initWithFrame:CGRectMake(0, 0, w, 50)];
        f1.backgroundColor = [UIColor whiteColor];
        
        
        NSString* ss = [NSString stringWithFormat:NSLocalizedString(@"%@ arriverà in ",nil),req.driver.name];
        UIFont *font3 = [self boldFontWithSize:18];
        NSDictionary *dict3 = [NSDictionary dictionaryWithObject: font3 forKey:NSFontAttributeName];
        NSMutableAttributedString *topAttriString = [[NSMutableAttributedString alloc] initWithString:ss attributes:dict3];
        
        UIFont *font2 = [self boldFontWithSize:20];
        NSDictionary *dict2 = [NSDictionary dictionaryWithObject: font2 forKey:NSFontAttributeName];
        NSMutableAttributedString *aAttrString2 = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%d min",(req.drivereta/60)+1] attributes:dict2];
        [aAttrString2 addAttribute:NSForegroundColorAttributeName value:ZEGOGREEN range:NSMakeRange(0, aAttrString2.string.length)];
        [topAttriString appendAttributedString:aAttrString2];
        [self addSubview:f1];
        
        te = [[UILabel alloc] initWithFrame:CGRectMake(15, 0, w-160, 30)];
        te.minimumScaleFactor = 0.7;
        te.adjustsFontSizeToFitWidth = YES;
        te.attributedText = topAttriString;
        [f1 addSubview:te];
        
        te2min = [[UILabel alloc] initWithFrame:CGRectMake(15, 30, w-100, 20)];
        NSInteger deltasec = [[NSDate date] timeIntervalSince1970] - [req.assigndate integerValue];
        te2min.text = [NSString stringWithFormat:NSLocalizedString(@"Puoi cancellare la tua richiesta senza costi in %d:%02d.",nil),(int)(deltasec/60),(int)(deltasec%60)];
        te2min.font = [self lightFontWithSize:12];
        te2min.minimumScaleFactor = 0.7;
        te2min.adjustsFontSizeToFitWidth = YES;
        [f1 addSubview:te2min];
        
        
        
        
        // line 2
        UIView* f2 = [[UIView alloc] initWithFrame:CGRectMake(0, 60, w, 80)];
        f2.layer.borderColor = [UIColor lightGrayColor].CGColor;
        f2.layer.borderWidth = 0.5;
        f2.backgroundColor = [UIColor whiteColor];
        
        UIImageView* userimg = [[UIImageView alloc] initWithFrame:CGRectMake(5, 55, 90, 90)];
        userimg.contentMode = UIViewContentModeScaleAspectFit;
        [userimg setImageWithURL:[NSURL URLWithString:req.driver.userimg] placeholderImage:[UIImage imageNamed:@"userplaceholder"]];
        userimg.layer.borderColor = ZEGOGREEN70.CGColor;
        userimg.layer.borderWidth = 0.5;
        userimg.layer.cornerRadius = 45;
        userimg.layer.masksToBounds = YES;
        [self addSubview:userimg];
        
        
        UIImageView* carimg = [[UIImageView alloc] initWithFrame:CGRectMake(w-95, 55, 90, 90)];
        carimg.contentMode = UIViewContentModeScaleAspectFit;
        UIImage* fsa = [UIImage imageNamed:@"carplaceholder"];
        [carimg setImageWithURL:[NSURL URLWithString:req.driver.carimg] placeholderImage:fsa];
        
        carimg.layer.cornerRadius = 45;
        carimg.layer.masksToBounds = YES;
        carimg.layer.borderColor = ZEGOGREEN70.CGColor;
        carimg.layer.borderWidth = 0.5;
        [self addSubview:carimg];
       
        /*
        UILabel* f2l1 = [[UILabel alloc] initWithFrame:CGRectMake(100, 0, w-200, 80/3.0f)];
        f2l1.font = [self boldFontWithSize:18];
        f2l1.text = req.driver.name;
        [f2 addSubview:f2l1];
        */
        
        UIImageView* f2l2 = [[UIImageView alloc] initWithFrame:CGRectMake(100, 80/6.0f, w-200, 80/3.0f)];
        f2l2.contentMode = UIViewContentModeScaleAspectFit;
        int s = ceil(req.driver.rating);
        f2l2.image = [UIImage imageNamed:[NSString stringWithFormat:@"s%d",s]];
        [f2 addSubview:f2l2];
        
        UILabel* f2l3 = [[UILabel alloc] initWithFrame:CGRectMake(100, 3.5f*80/6.0f, w-200, 80/3.0f)];
        f2l3.font = [self boldFontWithSize:15];
        f2l3.textColor = ZEGODARKGREY;
        f2l3.adjustsFontSizeToFitWidth = YES;
        f2l3.minimumScaleFactor = 0.7f;
        f2l3.textAlignment = NSTextAlignmentCenter;
        f2l3.text = [NSString stringWithFormat:@"%@ %@ %@",
                     req.driver.brand, req.driver.model, req.driver.color];
        [f2 addSubview:f2l3];
        
        [self addSubview:f2];
        
        // line 3
        Address* fake = [[Address alloc] init];
        fake.address = req.puaddr;
        AddressField* pickup = [[AddressField alloc] initWithFrame:CGRectMake(0, 150, w, 50) type:@"pickup" address:fake nav:NO editable:NO];
        [self addSubview:pickup];
        
        // line 4
        UIView* bottom = [[UIView alloc] initWithFrame:CGRectMake(0, 210, w*0.3f, 50)];
        bottom.backgroundColor = [UIColor whiteColor];
        UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
        [button addTarget:self
                   action:@selector(cancel)
         forControlEvents:UIControlEventTouchUpInside];
        [button setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
        [button setTitle:NSLocalizedString(@"Cancella",nil) forState:UIControlStateNormal];
        button.frame = CGRectMake(0, 0, w*0.3f, 50);
        [bottom addSubview:button];
        [self addSubview:bottom];

        call = [self buttonWithFrame:CGRectMake(w*0.3f, 210, w*0.7f, 50) selector:@selector(call) andTitle:NSLocalizedString(@"Chiama",nil)];
        [self enableButton:call];
        [self bringSubviewToFront:userimg];
        [self bringSubviewToFront:carimg];
        [self addSeparatorAtQuota:150 full:YES];
        [self addSeparatorAtQuota:200 full:YES];
        
        _timer = [NSTimer scheduledTimerWithTimeInterval:1.0f target:self selector:@selector(tick:) userInfo:nil repeats:YES];
    }
    return self;
}

-(void) cancel {
    [self.delegate passengerViewRideCanceled:self];
}

-(void) call {
    [self.delegate passengerViewRideCallDriver:self];
}

-(void)updateEtaTo:(NSInteger)eta {
    NSString* ss = [NSString stringWithFormat:NSLocalizedString(@"%@ arriverà in ",nil),request.driver.name];
    UIFont *font3 = [self boldFontWithSize:18];
    NSDictionary *dict3 = [NSDictionary dictionaryWithObject: font3 forKey:NSFontAttributeName];
    NSMutableAttributedString *topAttriString = [[NSMutableAttributedString alloc] initWithString:ss attributes:dict3];
    
    UIFont *font2 = [self boldFontWithSize:20];
    NSDictionary *dict2 = [NSDictionary dictionaryWithObject: font2 forKey:NSFontAttributeName];
    NSMutableAttributedString *aAttrString2 = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%ld min",(eta/60)+1] attributes:dict2];
    [aAttrString2 addAttribute:NSForegroundColorAttributeName value:ZEGOGREEN range:NSMakeRange(0, aAttrString2.string.length)];
    [topAttriString appendAttributedString:aAttrString2];
    
    te.attributedText = topAttriString;

}
-(void)tick:(NSTimer*)timer {
    NSInteger deltasec = (120+[request.assigndate integerValue]) - [[NSDate date] timeIntervalSince1970];
    if(deltasec < 0) {
        te2min.hidden = YES;
        if(_timer) {
            [_timer invalidate];
        }
    } else {
        te2min.text = [NSString stringWithFormat:NSLocalizedString(@"Hai %d:%02d minuti per annullare la tua richiesta senza costi.",nil),(int)(deltasec/60),(int)(deltasec%60)];
    }
}
@end
