//
//  PassengerIdleView.m
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "PassengerIdleView.h"

@implementation PassengerIdleView
@synthesize request;
@synthesize state;

-(void) bottomTapped:(id)sender {
    switch (state) {
        case ViewStateSelectingPickup:
            [self.delegate passengerIdleView:self changedStateTo:ViewStateSelectingDropoff withRequest:request];
            state = ViewStateSelectingDropoff;
            [[self subviews] makeObjectsPerformSelector:@selector(removeFromSuperview)];
            CGRect fr = self.frame;
            fr.size.height = 165;
            h = 165;
            [self setFrame:fr];
            [self layoutIfNeeded];
            [self updateSubviewsForState];
        
            break;
        case ViewStateSelectingDropoff:
            [self.delegate passengerIdleView:self changedStateTo:ViewStateReadyToSubmit withRequest:request];
            state = ViewStateReadyToSubmit;
            [[self subviews] makeObjectsPerformSelector:@selector(removeFromSuperview)];
            CGRect fre = self.frame;
            fre.size.height = 300;
            h = 300;
            [self setFrame:fre];
            [self layoutIfNeeded];
            [self updateSubviewsForState];
            break;
        case ViewStateReadyToSubmit:
            // ask delegate to send request
            if(currentService) {
                request.reqlevel = (int)currentService.level;
            } else {
                request.reqlevel = 0;
            }
            [self.delegate passengerIdleView:self sendRequest:request];
            break;
        default:
            break;
    }
}

-(void)selectCash {
    [top setToCash];
}
-(void)updateViewWithCards:(NSArray*)cards {
    if(!cards || [cards count] == 0) {
        defCard = nil;
         [top updateCard:nil];
    } else {
    
    userCards = cards;
    for(StripeCard* sc in cards) {
        if(sc.preferred == 1) {
            [top updateCard:sc];
            defCard = sc;
        }
    }
    }
}

-(void)updateViewWithEta:(EtaResponse*)eta fromRequest:(EtaRequest*)req {
    UIFont *font1 = [self regularFontWithSize:18];
    NSInteger etamin = (1+eta.eta/60);
    NSString* em = [NSString stringWithFormat:etamin > 30 ? @"%d+" : @"%d", MIN(30,etamin)];
    NSDictionary *dict1 = [NSDictionary dictionaryWithObject: font1 forKey:NSFontAttributeName];
    NSMutableAttributedString *aAttrString1 = [[NSMutableAttributedString alloc] initWithString:em attributes: dict1];
    
    UIFont *font2 = [self lightFontWithSize:8];
    NSDictionary *dict2 = [NSDictionary dictionaryWithObject: font2 forKey:NSFontAttributeName];
    NSMutableAttributedString *aAttrString2 = [[NSMutableAttributedString alloc] initWithString:@" min" attributes:dict2];
    
    [aAttrString1 appendAttributedString:aAttrString2];
    etaLabel.attributedText = aAttrString1;
   
    if(req.level == 0) {
        etaLabel.textColor = ZEGOGREEN;
    } else if (req.level == 2) {
        etaLabel.textColor = ZEGOPINK;
    } else if (req.level == 4) {
        etaLabel.textColor = ZEGOCONTROL;
    }
    
    etaLabel.textAlignment = NSTextAlignmentCenter;
    
    if(pickup) {
        Address* fake = [[Address alloc] init];
        fake.address = eta.address;
        [pickup updateWithAddress:fake];
        [self enableButton:bottomButton];
    }
    
    if(req) {
        request.puaddr = eta.address;
        request.pulat = req.lat;
        request.pulng = req.lng;
    }
    
    pickup.im.hidden = !eta;
    
    bottomButton.enabled = eta != nil;
    bottomButton.backgroundColor = eta != nil? ZEGOGREEN : ZEGODARKGREY;
    etaLabel.hidden = !eta;
    
    lastResponse = eta;
    
    BOOL found = false;
    for(Service* s in lastResponse.services) {
        if(currentService && s.sid == currentService.sid) {
            found = true;
            break;
        }
    }
    
    if(!found) {
        [self selectedService:nil];
    }
   
}


-(void)updateViewWithPrice:(PriceResponse*)price fromRequest:(PriceRequest*)req{
    if(dropoff) {
        Address* fake = [[Address alloc] init];
        fake.address = price.dropoff;
        [dropoff updateWithAddress:fake];
        [self enableButton:bottomButton];
    }
    
    request.extprice = price.price;
    request.zegofee = price.zegofee;
    request.driverfee = price.driverfee;
    request.passprice = price.price;
    request.discount = price.discount;
    request.numpass = 0;
    
    if(req) {
        request.dolat = req.dolat;
        request.dolng = req.dolng;
        request.doaddr = price.dropoff;
    }
}

-(void)updateViewWithAddress:(Address*)add andType:(NSString*)type {
    if([type isEqualToString:@""]) {
        request.pulat = add.lat;
        request.pulng = add.lng;
        request.puaddr = add.address;
        [pickup updateWithAddress:add];
    } else {
        request.dolat = add.lat;
        request.dolng = add.lng;
        request.doaddr = add.address;
        [dropoff updateWithAddress:add];
    }
        
}

-(void) createPickupSubviews {
    // frame is intended to be 115
    if(!_selectedLevel) {
        _selectedLevel = 0;
    }
    
    pickup = [[AddressField alloc] initWithFrame:CGRectMake(0, 5, w, 50)
                                                          type:@"pickup" address:nil nav:NO editable:YES];
    pickup.delegate = self;
    
    [self addSubview:pickup];
    
    // add eta box
    imtondo = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 60, 60)];
    imtondo.contentMode = UIViewContentModeScaleAspectFit;
    imtondo.image = [UIImage imageNamed:@"tondo_standard"];
    imtondo.userInteractionEnabled = YES;
    [imtondo addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tondoTap:)]];
    etaLabel = [[UILabel alloc] initWithFrame:CGRectMake(60*0.11f, 60*(10/20.f), 60*0.78f, 60*(6/20.f))];
    [imtondo addSubview:etaLabel];
    [self addSubview:imtondo];
    
    bottomButton = [self buttonWithFrame:CGRectMake(0, h-50, w, 50)
                                selector:@selector(bottomTapped:) andTitle:NSLocalizedString(@"Conferma partenza",nil)];
    bottomButton.userInteractionEnabled = YES;
    
    [self disableButton:bottomButton];
    
    [self updateViewWithEta:nil fromRequest:nil]; // init
    
    
}

-(void)tondoTap:(UIGestureRecognizer*)sender {
    [self.delegate passengerIdleViewServiceTapped:self];
}

-(void)selectedService:(Service *)s {
    
    currentService = s;
    if(!s) {
        etaLabel.textColor = ZEGOGREEN;
        imtondo.image = [UIImage imageNamed:@"tondo_standard"];
    } else if ([[s.name lowercaseString] containsString:@"rosa"]) {
        etaLabel.textColor = ZEGOPINK;
        imtondo.image = [UIImage imageNamed:@"tondo_pink"];
    } else if ([[s.name lowercaseString] containsString:@"control"]) {
        etaLabel.textColor = ZEGOCONTROL;
        imtondo.image = [UIImage imageNamed:@"tondo_control"];
    }
    
}

-(void) createDropoffSubviews {
    // frame is intended to be 165
    bottomButton = [self buttonWithFrame:CGRectMake(0, h-50, w, 50)
                                selector:@selector(bottomTapped:) andTitle:NSLocalizedString(@"Conferma arrivo",nil)];
    [self disableButton:bottomButton];
    
    Address* fake = [[Address alloc] init];
    fake.address = request.puaddr;
    pickup = [[AddressField alloc] initWithFrame:CGRectMake(0, 5, w, 50)
                                            type:@"pickup" address:fake nav:NO editable:YES];
    pickup.delegate = self;
    [self addSubview:pickup];
    
    dropoff = [[AddressField alloc] initWithFrame:CGRectMake(0, 55, w, 50)
                                            type:@"dropoff" address:nil nav:NO editable:YES];
    dropoff.delegate = self;
    [self addSubview:dropoff];

}

-(void) createReadyToSubmitSubviews {
    bottomButton = [self buttonWithFrame:CGRectMake(0, h-50, w, 50)
                                selector:@selector(bottomTapped:) andTitle:NSLocalizedString(@"Chiedi passaggio",nil)];
    [self enableButton:bottomButton];
    
    Address* fake = [[Address alloc] init];
    fake.address = request.puaddr;
    pickup = [[AddressField alloc] initWithFrame:CGRectMake(0, 145, w, 50)
                                            type:@"pickup" address:fake nav:NO editable:YES];
    pickup.delegate = self;
    [self addSubview:pickup];
    
    Address* fake2 = [[Address alloc] init];
    fake2.address = request.doaddr;
    dropoff = [[AddressField alloc] initWithFrame:CGRectMake(0, 195, w, 50)
                                             type:@"dropoff" address:fake2 nav:NO editable:YES];
    dropoff.delegate = self;
    [self addSubview:dropoff];
    
    top = [[ThreeElementIdleView alloc] initWithFrame:CGRectMake(0, 95, w, 50) request:request stripeCard:defCard];
    top.delegate = self;
    
    [self addSubview:top];
    [self addSeparatorAtQuota:145 full:YES];
    [self addSeparatorAtQuota:195 full:NO];
    [self addSeparatorAtQuota:245 full:YES];
    [self verticalConnectorAtQuota:176 lenght:38];
}

-(void) updateSubviewsForState {
    
    [[self subviews] makeObjectsPerformSelector:@selector(removeFromSuperview)];
    switch (state) {        
        case ViewStateSelectingPickup:
            [self createPickupSubviews];
            break;
        case ViewStateSelectingDropoff:
            [self createDropoffSubviews];
            break;
        case ViewStateReadyToSubmit:
            [self createReadyToSubmitSubviews];
            break;
        default:
            break;
    }
}

-(void) baseInit {
    state = ViewStateSelectingPickup;
    request = [[RideRequest alloc] init];
    
    [self updateSubviewsForState];
}

-(id) initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if(self) {
        [self baseInit];
    }
    return self;
}

-(id) initWithFrame:(CGRect)frame request:(RideRequest*)req andState:(NSUInteger)s {
    self = [super initWithFrame:frame];
    if(self) {
        state = s;
        request = req;
        [self updateSubviewsForState];
    }
    return self;
}

-(void)editSelectedForAddressField:(AddressField *)field {
    if([field.atype isEqualToString:@"dropoff"] &&  state == ViewStateReadyToSubmit) {
        [self cancelSelectedForAddressField:field];
    }
    
    [self.delegate passengerIdleView:self completeAddressWithType:field.atype withRequest:request];
}

-(void)navSelectedForAddressField:(AddressField *)field {
    
}

-(void)updatePickupAddress:(NSString*)ad
{
    pickup.lab.text = ad;
    request.puaddr = ad;
}

-(void)updateDropoffAddress:(NSString*)ad
{
    dropoff.lab.text = ad;
    request.doaddr = ad;
}

-(void)cancelSelectedForAddressField:(AddressField *)field {
    if([field isEqual:pickup]) {
        [[self subviews] makeObjectsPerformSelector:@selector(removeFromSuperview)];
        CGRect fr = self.frame;
        fr.size.height = 115;
        h = 115;
        [self setFrame:fr];
        [self layoutIfNeeded];
        [self.delegate passengerIdleView:self changedStateTo:ViewStateSelectingPickup withRequest:request];
        [self baseInit];
    } else {
        [self.delegate passengerIdleView:self changedStateTo:ViewStateSelectingDropoff withRequest:request];
        state = ViewStateSelectingDropoff;
        [[self subviews] makeObjectsPerformSelector:@selector(removeFromSuperview)];
        CGRect fr = self.frame;
        fr.size.height = 165;
        h = 165;
        [self setFrame:fr];
        [self layoutIfNeeded];
        [self updateSubviewsForState];
    }
}

-(void)elementViewCardSelected:(ThreeElementIdleView *)view {
    [self.delegate passengerIdleViewCardTapped:self];
}

-(void)elementViewPriceSelected:(ThreeElementIdleView *)view {
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
        [slider addTarget:self
                   action:@selector(sliderDidEndSliding:)
         forControlEvents:(UIControlEventTouchUpInside | UIControlEventTouchUpOutside)];
        slider.backgroundColor = [UIColor clearColor];
        slider.minimumTrackTintColor = ZEGOGREEN;
        slider.maximumTrackTintColor = ZEGODARKGREY;
        slider.minimumValue = 0;
        
        NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
        NSString* maxprice = [def valueForKey:@"pricing.maximum.fee"];
        NSInteger maximumslider = 7050;

        if(maxprice) {
            maximumslider = (int)(([maxprice integerValue]*117.5f)/100);
            maximumslider = ((int)((maximumslider + 50) / 100) * 100);

        }
        slider.maximumValue = MIN(maximumslider,request.extprice*2);
        slider.value = request.passprice;
        sliderLabel = [[UILabel alloc] initWithFrame:CGRectMake(20, 45, self.frame.size.width-60, 35)];
        UIFont *font1 = [self boldFontWithSize:20];
        NSDictionary *dict1 = @{ NSForegroundColorAttributeName : ZEGOGREEN, NSFontAttributeName : font1};
        NSMutableAttributedString *aAttrString1 = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%.2f €",slider.value/100.f]  attributes: dict1];
        
        UIFont *font2 = [self lightFontWithSize:13];
        NSDictionary *dict2 = @{ NSForegroundColorAttributeName : ZEGODARKGREY, NSFontAttributeName : font2};
        NSMutableAttributedString *aAttrString2 = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:NSLocalizedString(@" Inclusa fee di utilizzo",nil),request.driverfee/100.f] attributes:dict2];
        
        [aAttrString1 appendAttributedString:aAttrString2];
        sliderLabel.attributedText = aAttrString1;
        sliderLabel.textAlignment = NSTextAlignmentCenter;
        sliderLabel.adjustsFontSizeToFitWidth = YES;
        sliderLabel.minimumScaleFactor = 0.5;
        etaLabel.textAlignment = NSTextAlignmentCenter;
        [slider setThumbImage:[UIImage imageNamed:@"thumb_tip"] forState:UIControlStateNormal];
        [sliderBox addSubview:slider];
        [sliderBox addSubview:sliderLabel];
        [self addSubview:sliderBox];
    }
}

-(void)elementView:(ThreeElementIdleView *)view passnumUpdatedTo:(NSInteger)p {
    request.numpass = (int)p;
}

- (void)sliderDidEndSliding:(NSNotification *)notification {
    request.pid = (int)_user.uid;
    [[ZegoAPIManager sharedManager] updateRequestPrice:^(RideRequest *resp) {
        if(resp) {
            request.driverfee = resp.driverfee;
            request.zegofee = resp.zegofee;
            request.driverprice = resp.driverprice;
            request.stripezegofee = resp.stripezegofee;
            request.stripedriverfee = resp.stripedriverfee;
            request.passprice = resp.passprice;
            request.discount = resp.discount;
            
            int rounded = ((int)((slider.value + 50) / 100) * 100);
            UIFont *font1 = [self boldFontWithSize:20];
            NSDictionary *dict1 = @{ NSForegroundColorAttributeName : ZEGOGREEN, NSFontAttributeName : font1};
            NSMutableAttributedString *aAttrString1 = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%.2f €",rounded/100.f]  attributes: dict1];
            
            UIFont *font2 = [self lightFontWithSize:13];
            NSDictionary *dict2 = @{ NSForegroundColorAttributeName : ZEGODARKGREY, NSFontAttributeName : font2};
            NSMutableAttributedString *aAttrString2 = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:NSLocalizedString(@" Inclusa fee di utilizzo",nil),request.driverfee/100.f] attributes:dict2];
            
            [aAttrString1 appendAttributedString:aAttrString2];
            sliderLabel.attributedText = aAttrString1;
            sliderLabel.adjustsFontSizeToFitWidth = YES;
            sliderLabel.minimumScaleFactor = 0.5;
            
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            NSLog(@"Error updating price request");
        }
    } withRequest:request];
}

- (IBAction)sliderValueChanged:(UISlider *)_slider {
    int rounded = ((int)((_slider.value + 50) / 100) * 100);
    if(rounded<=100) {
        rounded = 100;
        [self.delegate passengerIdleViewAlertMinimumPrice:self];
        
    }
    [top updatePrice:rounded];
    [_slider setValue:rounded animated:NO];
    request.passprice = rounded;
    UIFont *font1 = [self boldFontWithSize:20];
    NSDictionary *dict1 = @{ NSForegroundColorAttributeName : ZEGOGREEN, NSFontAttributeName : font1};
    NSMutableAttributedString *aAttrString1 = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%.2f €",rounded/100.0f] attributes: dict1];
    
    UIFont *font2 = [self lightFontWithSize:13];
    NSDictionary *dict2 = @{ NSForegroundColorAttributeName : ZEGODARKGREY, NSFontAttributeName : font2};
    NSMutableAttributedString *aAttrString2 = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:NSLocalizedString(@" Inclusa fee di utilizzo",nil),request.driverfee/100.f] attributes:dict2];
    
    [aAttrString1 appendAttributedString:aAttrString2];
    
    //[aAttrString1 appendAttributedString:aAttrString2];
    sliderLabel.attributedText = aAttrString1;
    sliderLabel.adjustsFontSizeToFitWidth = YES;
    sliderLabel.minimumScaleFactor = 0.5;
}
@end
