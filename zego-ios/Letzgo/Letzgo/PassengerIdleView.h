//
//  PassengerIdleView.h
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ZegoBaseView.h"
#import "AddressField.h"
#import "ThreeElementIdleView.h"
#import "CustomUISlider.h"

@class PassengerIdleView;

enum PassengerIdleViewState: NSUInteger {
    ViewStateSelectingPickup = 1,
    ViewStateSelectingDropoff = 2,
    ViewStateReadyToSubmit = 3
};

@protocol PassengerIdleDelegate
-(void)passengerIdleView:(PassengerIdleView*)v changedStateTo:(NSUInteger)st withRequest:(RideRequest*)req;
-(void)passengerIdleView:(PassengerIdleView*)v completeAddressWithType:(NSString*)type withRequest:(RideRequest*)req;
-(void)passengerIdleView:(PassengerIdleView*)v sendRequest:(RideRequest*)request;
-(void)passengerIdleViewAlertMinimumPrice:(PassengerIdleView*)v;
-(void)passengerIdleViewCardTapped:(PassengerIdleView*)v;
-(void)passengerIdleViewServiceTapped:(PassengerIdleView*)v;
@end



@interface PassengerIdleView : ZegoBaseView <AddressFieldDelegate, ThreeElementIdleViewDelegate>
{
    NSArray* userCards;
    StripeCard* defCard;
    
    UIButton* bottomButton;
    AddressField* pickup;
    AddressField* dropoff;
    ThreeElementIdleView* top;
    UILabel* etaLabel;
    UIImageView* imtondo;
    UIView* sliderBox;
    CustomUISlider* slider;
    UILabel* sliderLabel;
    EtaResponse* lastResponse;
    Service* currentService;

}

-(void)selectedService:(Service*)service;
-(void)updatePickupAddress:(NSString*)ad;
-(void)updateDropoffAddress:(NSString*)ad;
-(void)updateViewWithCards:(NSArray*)cards;
-(void)selectCash;
-(void)updateViewWithEta:(EtaResponse*)eta fromRequest:(EtaRequest*)req;
-(void)updateViewWithPrice:(PriceResponse*)price fromRequest:(PriceRequest*)req;
-(void)updateViewWithAddress:(Address*)add andType:(NSString*)type;

@property (assign) id <PassengerIdleDelegate> delegate;
@property (nonatomic,readonly) RideRequest* request;
@property (nonatomic,readonly) NSUInteger state;
@property (nonatomic,strong) User* user;
@property (nonatomic,assign) NSUInteger selectedLevel;
@end
