//
//  DriverOnRideView.m
//  Letzgo
//
//  Created by Luca Adamo on 02/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "DriverOnRideView.h"

@implementation DriverOnRideView

-(void)baseInit {
    Address* fake = [[Address alloc] init];
    fake.address = request.puaddr;
    pickup = [[AddressField alloc] initWithFrame:CGRectMake(0, 145, w, 50)
                                            type:@"pickup" address:fake nav:NO editable:NO];
    pickup.delegate = self;
    [self addSubview:pickup];
    
    Address* fake2 = [[Address alloc] init];
    fake2.address = request.doaddr;
    dropoff = [[AddressField alloc] initWithFrame:CGRectMake(0, 195, w, 50)
                                             type:@"dropoff" address:fake2 nav:YES editable:NO];
    dropoff.delegate = self;
    [self addSubview:dropoff];
    
    two = [[TwoElementRideView alloc] initWithFrame:CGRectMake(0, 95, w, 50) request:request];
    two.delegate = self;
    
    [self addSubview:two];
    
    bottomButton = [self buttonWithFrame:CGRectMake(0, h-50, w, 50)
                                selector:@selector(bottomTapped:) andTitle:NSLocalizedString(@"Termina",nil)];
    [self enableButton:bottomButton];
    [self addSeparatorAtQuota:145 full:YES];
    [self addSeparatorAtQuota:195 full:NO];
    [self addSeparatorAtQuota:245 full:YES];
    [self verticalConnectorAtQuota:176 lenght:38];
}

-(id)initWithFrame:(CGRect)frame andRequest:(RideRequest*)req {
    self = [super initWithFrame:frame];
    if(self) {
        request = req;
        [self baseInit];
        driverPrice = (request.driverfee+request.zegofee);
        sliderPrice = nil;
    }
    return self;
}

-(void) twoElementRideViewPriceTapped:(TwoElementRideView *)view {
    if(sliderBox) {
        [sliderBox removeFromSuperview];
        sliderBox = nil;
    } else {
        sliderBox = [[UIView alloc] initWithFrame:CGRectMake(10, 0, self.frame.size.width-20, 80)];
        sliderBox.backgroundColor = [UIColor whiteColor];
        sliderBox.layer.cornerRadius = 5;
        sliderBox.layer.masksToBounds = YES;
        sliderBox.layer.borderColor = [UIColor lightGrayColor].CGColor;
        sliderBox.layer.borderWidth = 0.75;
        slider = [[CustomUISlider alloc] initWithFrame:CGRectMake(20, 20, self.frame.size.width-60, 40)];
        [slider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
       
        slider.backgroundColor = [UIColor clearColor];
        slider.minimumTrackTintColor = ZEGOGREEN;
        slider.maximumTrackTintColor = ZEGODARKGREY;
        slider.minimumValue = 100;
        slider.maximumValue = (request.driverfee+request.zegofee)*1.3;
        slider.value = (request.driverfee+request.zegofee);
        sliderLabel = [[UILabel alloc] initWithFrame:CGRectMake(20, 45, self.frame.size.width-60, 35)];
        UIFont *font1 = [self boldFontWithSize:20];
        NSDictionary *dict1 = @{ NSForegroundColorAttributeName : ZEGOGREEN, NSFontAttributeName : font1};
        NSMutableAttributedString *aAttrString1 = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%.2f €",slider.value/100.f]  attributes: dict1];
        

        sliderLabel.attributedText = aAttrString1;
        sliderLabel.textAlignment = NSTextAlignmentCenter;
        [slider setThumbImage:[UIImage imageNamed:@"thumb_tip"] forState:UIControlStateNormal];
        [sliderBox addSubview:slider];
        [sliderBox addSubview:sliderLabel];
        [self addSubview:sliderBox];
    }
}


- (IBAction)sliderValueChanged:(UISlider *)_slider {
    int rounded = ((int)((_slider.value + 50) / 100) * 100);
    
    if(rounded>(request.driverfee+request.zegofee)){
        rounded = (request.driverfee+request.zegofee);
    }
    
    if(rounded==0) {
        rounded = 0;
        [self.delegate driverOnRideMinimumPrice:self];
        
    }
    driverPrice = rounded;
    [two updatePriceTo:rounded];
    [_slider setValue:rounded animated:NO];
    request.passprice = rounded;
    UIFont *font1 = [self boldFontWithSize:20];
    NSDictionary *dict1 = @{ NSForegroundColorAttributeName : ZEGOGREEN, NSFontAttributeName : font1};
    NSMutableAttributedString *aAttrString1 = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%.2f €",rounded/100.0f] attributes: dict1];
    
    sliderLabel.attributedText = aAttrString1;
    sliderPrice = driverPrice;
}



-(void)navSelectedForAddressField:(AddressField *)field {
    if([field isEqual:dropoff]) {
        [self.delegate driverOnRideNavigateToDropoff:self];
    }
}

-(void)cancelSelectedForAddressField:(AddressField *)field {
    
}

-(void)editSelectedForAddressField:(AddressField *)field{
    
}

-(void)bottomTapped:(id)sender {
    [self.delegate driverOnRideView:self terminatedRideWithPrice:sliderPrice];
}
@end
