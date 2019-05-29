//
//  CenterMapViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 16/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"
#import <GoogleMaps/GoogleMaps.h>
#import "PassengerIdleView.h"
#import "PassengerWaitingAnswerView.h"
#import "PassengerWaitingDriverView.h"
#import "PassengerOnRideView.h"
#import "PassengerFeedbackView.h"
#import "ReasonSelectorView.h"
#import "DriverOnRideView.h"
#import "DriverFeedbackView.h"
#import "DriverAnsweringView.h"
#import "DriverPickingUpView.h"
#import "DriverFeedbackView.h"

@interface CenterMapViewController : ZegoViewController<GMSMapViewDelegate, PassengerIdleDelegate, PassengerWaitingAnswerViewDelegate, PassengerWaitingDriverDelegate, PassengerOnRideViewDelegate, PassengerFeedbackViewDelegate, ReasonSelectorDelegate, DriverAnsweringDelegate, DriverPickingUpDelegate, DriverOnRideViewDelegate, DriverFeedbackViewDelegate>
{
    NSMutableArray* cards;
    StripeCard* card;
    User* u;
    RideRequest* current;
    CLLocationCoordinate2D coord;
    NSMutableDictionary* markers;
    GMSMarker* pickup;
    GMSMarker* dropoff;
    GMSMarker* driver;
    GMSMarker* myselfDriving;
    UIView* internal;
    LocationManager* manager;
    BOOL programmaticMove;
    NSInteger lastState;
    Address* lastEditedAddress;
    EtaResponse* lastResponse;
    UIView* serviceSelector;
    int currentLevel;
    BOOL followUser;
}
@property (strong, nonatomic) NSString* method;
@property (strong, nonatomic) IBOutlet UILabel *errorMsg;
@property (strong, nonatomic) IBOutlet UIView *error;
@property (strong, nonatomic) IBOutlet UIImageView *errorIcon;

@property (strong, nonatomic) IBOutlet UIImageView *centerOnMe;
@property (strong, nonatomic) IBOutlet UIImageView *pointer;
@property (strong, nonatomic) IBOutlet UIImageView *userImg;
@property (strong, nonatomic) IBOutlet UIView *box;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *boxHeight;
@property (strong, nonatomic) IBOutlet GMSMapView *map;


-(IBAction)showMenu:(id)sender;
-(void)userUpdated:(User*)us;
-(void)locationUpdated:(CLLocationCoordinate2D)co;
-(void)requestUpdated:(RideRequest*)req;
-(void)addMarkerAt:(CLLocationCoordinate2D)c ofType:(NSInteger)t;
-(void)removeMarkerOfType:(NSInteger)t;
-(void)addressSelected:(Address*)a forType:(NSString*)ty;
-(void)refreshUserCards;
-(void)useCash;
@end
