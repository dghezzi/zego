//
//  CenterMapViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 16/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "CenterMapViewController.h"
#import "EditAddressViewController.h"
#import "StripeAddCardViewController.h"
#import "CreditCardViewController.h"

@interface CenterMapViewController ()

@end

@implementation CenterMapViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    lastState = -1;
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(reachabilityChanged:)
                                                 name:kReachabilityChangedNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(gpsNo:)
                                                 name:gpsAuthorizationDenied
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(gpsYes:)
                                                 name:gpsAuthorizationGranted
                                               object:nil];
    _error.layer.cornerRadius = 20;
    _error.clipsToBounds = YES;
    _method = @"card";
    _error.alpha = 0.8;
    markers = [[NSMutableDictionary alloc] init];
    _map.delegate = self;
    _map.settings.allowScrollGesturesDuringRotateOrZoom = NO;
    NSLog(@"USER called MAPPA");
    u = [self loggedUser];
    [self loadCards];       
    manager = [LocationManager sharedManager];
    [manager registerObserver:self];    
    
    _userImg.contentMode = UIViewContentModeScaleAspectFit;
    _userImg.layer.cornerRadius = 20;
    _userImg.layer.masksToBounds = YES;
    if(u.img) {
        [_userImg setImageWithURL:[NSURL URLWithString:u.img] placeholderImage:[UIImage imageNamed:@"userplaceholder"]];
    }
    [_userImg addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(showMenu:)]];
    _box.hidden = YES;
    _centerOnMe.userInteractionEnabled = YES;
    [_centerOnMe addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(centerMe:)]];
    programmaticMove = NO;
    _error.hidden = YES;
    
    _userImg.isAccessibilityElement = YES;
    _userImg.accessibilityLabel = @"Menu";
    _userImg.accessibilityHint = @"Menu";
    _userImg.accessibilityTraits = UIAccessibilityTraitButton;
    currentLevel = 0;
    followUser = YES;
}

-(void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [manager registerObserver:self];
    
}
-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self checkRevalidateTCDialog];
    self.navigationController.interactivePopGestureRecognizer.enabled = NO;
    self.navigationController.navigationItem.backBarButtonItem.enabled = NO;
    [self sidePanelController].recognizesPanGesture = NO;
    if(u.rtstatus % 100 != 0 && u.current) {
        [self startLoading];
        [[ZegoAPIManager sharedManager] getRequest:^(RideRequest *resp) {
            if(resp) {
                current = resp;
                [self setupMapForState:u.rtstatus];
                [self stopLoading];
            }
        } failure:^(RKObjectRequestOperation *operation, NSError *error) {
            [self stopLoading];
        } withId:[self loggedUser].current];
    } else {
        [self setupMapForState:u.rtstatus];
    }
    

    
}

-(void)centerMe:(id)sender{
    
    if(coord.latitude*coord.longitude != 0) {
        if(!sender) {
             [_map animateToCameraPosition:[GMSCameraPosition cameraWithLatitude:coord.latitude longitude:coord.longitude zoom:16]];
        } else {
            [_map animateToLocation:coord];
        }
       
           } else {
        [_map animateToCameraPosition:[GMSCameraPosition cameraWithLatitude:u.llat longitude:u.llon zoom:16]];
    }
    [_map setMyLocationEnabled:YES];
    followUser = YES;
}

-(IBAction)showMenu:(id)sender {
    [[self sidePanelController] showLeftPanelAnimated:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

-(void)mapView:(GMSMapView *)mapView idleAtCameraPosition:(GMSCameraPosition *)position {
    
    if(!programmaticMove) {
    if(internal && [internal isKindOfClass:[PassengerIdleView class]]) {
        PassengerIdleView* intern = ((PassengerIdleView*)internal);
        if(intern.state == ViewStateSelectingPickup) {
            [self performEtaForPosition:position];
        } else if(intern.state == ViewStateSelectingDropoff){
            [self performPriceForPosition:position];
        }
    } else if (lastState == DriverStatusIdle) {
        [self performDriverETAForPosition:position];
    }}
    programmaticMove = NO;
    
}

-(void) performEtaForPosition:(GMSCameraPosition *)position {
    EtaRequest* ereq = [[EtaRequest alloc] init];
    ereq.lat = position.target.latitude;
    ereq.lng = position.target.longitude;
    ereq.level = currentLevel;
    
    
    [[ZegoAPIManager sharedManager] eta:^(EtaResponse *resp) {
        if(resp) {
            [((PassengerIdleView*)internal) updateViewWithEta:resp fromRequest:ereq];
            [_map clear];
            [markers removeAllObjects];
            for(CompactDriver* cd in resp.drivers) {
                [self addMarker:CLLocationCoordinate2DMake(cd.lat, cd.lng) forCompatDriver:cd];
            }
            if(lastEditedAddress) {
                [((PassengerIdleView*)internal) updatePickupAddress:lastEditedAddress.address];
                lastEditedAddress = nil;
            }
            
            lastResponse = resp;
            [self handleServiceUpdate];
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            [((PassengerIdleView*)internal) updateViewWithEta:nil fromRequest:nil];

            ErrorResponse* re = [self handleError:error];
            if(re) {
                [self showError:re severity:100];
            }
        }
        lastResponse = nil;
        [self handleServiceUpdate];
    } withRequest:ereq];
}

-(void) handleServiceUpdate {
    BOOL opened = false;
    if(serviceSelector) {
        opened = !serviceSelector.hidden;
        [serviceSelector removeFromSuperview];
    }
    
    if([lastResponse.services count] > 0) {
        
        CGRect pframe = _box.frame;
        NSInteger serct = 1+[lastResponse.services count];
        
        serviceSelector = [[UIView alloc] initWithFrame:CGRectMake(10, pframe.origin.y-1*(5+serct*60), 240, serct*60)];
        for(Service* sv in lastResponse.services) {
            if(sv.name) {
                [self addService:sv atPosition:1+[lastResponse.services indexOfObject:sv] outOf:serct-1];
            }
        }
        
        [self addService:nil atPosition:0 outOf:serct-1];
        
        serviceSelector.backgroundColor = [UIColor whiteColor];
        serviceSelector.layer.borderColor = [UIColor colorWithWhite:0.5 alpha:1].CGColor;
        serviceSelector.layer.masksToBounds = YES;
        serviceSelector.layer.borderWidth = 0.75;
        serviceSelector.layer.cornerRadius = 4;
        
        serviceSelector.userInteractionEnabled = YES;
        serviceSelector.hidden = !opened;
        [self.view addSubview:serviceSelector];
    } else {
        serviceSelector = nil;
        currentLevel = 0;
        [((PassengerIdleView*)internal) selectedService:nil];
    }
}

-(void) addService:(Service*)s atPosition:(NSInteger)pos outOf:(NSInteger)max {
    NSString * language = [[NSLocale preferredLanguages] firstObject];
    BOOL ita = language && [[language lowercaseString] hasPrefix:@"it"];
    UIView* main = [[UIView alloc] initWithFrame:CGRectMake(0, (max-pos)*60, 240, 60)];
    main.userInteractionEnabled = YES;
    main.tag = s ? s.level : 0;
    [main addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(serviceTapped:)]];
    
    UIImageView* im = [[UIImageView alloc] initWithFrame:CGRectMake(12, 12, 36, 36)];
    if(!s) {
        im.image = [UIImage imageNamed:@"dr_standard_icon"];
    } else if ([[s.name lowercaseString] containsString:@"rosa"]) {
        im.image = [UIImage imageNamed:@"pink_driver_icon"];
    } else if ([[s.name lowercaseString] containsString:@"control"]) {
        im.image = [UIImage imageNamed:@"control"];
    }
    [main addSubview:im];
    
    UILabel* topSL = [[UILabel alloc] initWithFrame:CGRectMake(70, 10, 170, 24)];
    if(ita){
        topSL.text = s ? s.name : @"Zego";
    } else {
        topSL.text = s ? s.nameen : @"Zego";
    }
    if(!s) {
        topSL.textColor = ZEGOGREEN;
    } else if ([[s.name lowercaseString] containsString:@"rosa"]) {
        topSL.textColor = ZEGOPINK;
    } else if ([[s.name lowercaseString] containsString:@"control"]) {
        topSL.textColor = ZEGOCONTROL;
    }
    topSL.font = [UIFont systemFontOfSize:16];
    
    UILabel* botSL = [[UILabel alloc] initWithFrame:CGRectMake(70, 30, 170, 18)];
    if(ita) {
        botSL.text = s ? s.details : @"";
    } else {
        botSL.text = s ? s.detailsen : @"";
    }
    botSL.textColor = ZEGODARKGREY;
    botSL.font = [UIFont systemFontOfSize:12];
    
    UIView* line = [[UIView alloc] initWithFrame:CGRectMake(7, 59.5, 226, 0.5)];
    line.backgroundColor = [UIColor colorWithWhite:0.7 alpha:1];
    [main addSubview:line];
    [main addSubview:topSL];
    [main addSubview:botSL];
    [serviceSelector addSubview:main];
    
    
}


-(void)serviceTapped:(UIGestureRecognizer*)sender {
    
    NSInteger level = sender.view.tag;
    currentLevel = (int)level;
    if(level == 0) {
        NSLog(@"Selezionato Servizio Standard");
        [((PassengerIdleView*)internal) selectedService:nil];
    } else {
        for(Service* s in lastResponse.services) {
            if(s.level == level) {
                [((PassengerIdleView*)internal) selectedService:s];
            }
            
        }
    }
    serviceSelector.hidden = YES;
}

-(void) performDriverETAForPosition:(GMSCameraPosition *)position {
    EtaRequest* ereq = [[EtaRequest alloc] init];
    ereq.lat = position.target.latitude;
    ereq.lng = position.target.longitude;
    ereq.level = 0;

    
    [[ZegoAPIManager sharedManager] eta:^(EtaResponse *resp) {
        if(resp) {
            [_map clear];
            [markers removeAllObjects];
            for(CompactDriver* cd in resp.drivers) {
                [self addMarker:CLLocationCoordinate2DMake(cd.lat, cd.lng) forCompatDriver:cd];
            }
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            [((PassengerIdleView*)internal) updateViewWithEta:nil fromRequest:nil];
            
            ErrorResponse* re = [self handleError:error];
            if(re) {
                [self showError:re severity:100];
            }
        }
    } withRequest:ereq];
}

-(void)passengerIdleViewServiceTapped:(PassengerIdleView*)v {
    BOOL alreadyhidden = serviceSelector.hidden;
    BOOL canshow = lastResponse && [lastResponse.services count] > 0;
    if(!alreadyhidden) {
        serviceSelector.hidden = YES;
    } else{
            serviceSelector.hidden = !canshow;
    }

}
-(void) performPriceForPosition:(GMSCameraPosition *)position {
    PriceRequest* ereq = [[PriceRequest alloc] init];
    PassengerIdleView* intern = ((PassengerIdleView*)internal);
    ereq.dolat = position.target.latitude;
    ereq.dolng = position.target.longitude;
    ereq.pulat = intern.request.pulat;
    ereq.pulng = intern.request.pulng;
    ereq.level = 0;
    
    [[ZegoAPIManager sharedManager] price:^(PriceResponse *resp) {
        if(resp) {
            [((PassengerIdleView*)internal) updateViewWithPrice:resp fromRequest:ereq];
            if(lastEditedAddress) {
                [((PassengerIdleView*)internal) updateDropoffAddress:lastEditedAddress.address];
                lastEditedAddress = nil;
            }
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            ErrorResponse* re = [self handleError:error];
            if(re) {
                
            }
        }
    } withRequest:ereq];
}


-(void)setupMapForState:(NSInteger)state {
    lastState = state;
    [[UIApplication sharedApplication] setIdleTimerDisabled:lastState>=200];
    if(state == PassengerStatusIdle && [internal isKindOfClass:[PassengerIdleView class]]) {
        
    } else {
     
    _box.hidden = YES;
    [[_box subviews] makeObjectsPerformSelector:@selector(removeFromSuperview)];
        [self centerMe:nil];

    [self.view layoutIfNeeded];
    // driver states
        
    if(state>= 200) {
        _pointer.hidden = YES;
        serviceSelector.hidden = YES;
        switch (state) {
            case DriverStatusIdle:
                [self stopLoading];
                [self clearMap];
                _boxHeight.constant = 0;
                [self.view layoutIfNeeded];
                internal = nil;
                [manager changePollingMode:PollingModeUser withId:[self loggedUser].uid];
                [_box addSubview:internal];
                [self centerMe:nil];
                break;
            case DriverStatusAnswering:
                [self clearMap];                
                [self loadRequest];
                
                break;
            case DriverStatusPickingUp:
                [self clearMap];
                _boxHeight.constant = 320;
                [self.view layoutIfNeeded];
                internal = [[DriverPickingUpView alloc] initWithFrame:_box.bounds andRequest:current];
                ((DriverPickingUpView*)internal).delegate = self;
                [_box addSubview:internal];
                [self drawRequestAsDriver];
                _pointer.hidden = YES;
                [self autozoom];
                break;
            case DriverStatusOnRide:
                [self clearMap];
                _boxHeight.constant = 305;
                [self.view layoutIfNeeded];
                internal = [[DriverOnRideView alloc] initWithFrame:_box.bounds andRequest:current];
                ((DriverOnRideView*)internal).delegate = self;
                [_box addSubview:internal];
                [self drawRequestAsDriver];
                _pointer.hidden = YES;
                [self autozoom];
                break;
            case DriverStatusFeedbackDue:
                [self clearMap];
                _boxHeight.constant = 150;
                [self.view layoutIfNeeded];
                internal = [[DriverFeedbackView alloc] initWithFrame:_box.bounds andRequest:current];                ((DriverFeedbackView*)internal).delegate = self;
                [_box addSubview:internal];
                [self drawRequestAsDriver];
                _pointer.hidden = YES;
                [self autozoom];
                break;
            default:
                break;
        }
    }
    // passenger states
    else {
        _pointer.hidden = NO;
        switch (state) {
            case PassengerStatusIdle:
                [manager changePollingMode:PollingModeUser withId:[self loggedUser].uid];
                _boxHeight.constant = 115;
                [self.view layoutIfNeeded];
                internal = [[PassengerIdleView alloc] initWithFrame:_box.bounds];
                ((PassengerIdleView*)internal).user = [self loggedUser];
                ((PassengerIdleView*)internal).delegate = self;
                [_box addSubview:internal];
                [self centerMe:nil];
                [self.view layoutIfNeeded];
                break;
            case PassengerStatusWaitingForAnswer:
                [self clearMap];
                _boxHeight.constant = 110;
                [self.view layoutIfNeeded];
                internal = [[PassengerWaitingAnswerView alloc] initWithFrame:_box.bounds andRequest:current];
                ((PassengerWaitingAnswerView*)internal).delegate = self;
                [_box addSubview:internal];
                [self drawRequestAsRider];
                _pointer.hidden = YES;
                [self autozoom];
                break;
            case PassengerStatusWaitingForDriver:
                [self loadUserRequest];
                break;
            case PassengerStatusOnRide:
                [self clearMap];
                _boxHeight.constant = 160;
                [self.view layoutIfNeeded];
                internal = [[PassengerOnRideView alloc] initWithFrame:_box.bounds andRequest:current];                ((PassengerOnRideView*)internal).delegate = self;
                [_box addSubview:internal];
                [self drawRequestAsRider];
                _pointer.hidden = YES;
                [self autozoom];
                break;
            case PassengerStatusPaymentDue:
                break;
            case PassengerStatusFeedbackDue:
                [self clearMap];
                _boxHeight.constant = 150;
                [self.view layoutIfNeeded];
                internal = [[PassengerFeedbackView alloc] initWithFrame:_box.bounds andRequest:current];                ((PassengerFeedbackView*)internal).delegate = self;
                [_box addSubview:internal];
                [self drawRequestAsRider];
                _pointer.hidden = YES;
                [self autozoom];
                break;
            default:
                break;
        }
    }
    
    _box.hidden = NO;
    _box.userInteractionEnabled = YES;
    [self.view bringSubviewToFront:_box];
    }
    
}

-(void)loadRequest {
    [[ZegoAPIManager sharedManager] getRequest:^(RideRequest *resp) {
        if(resp) {
            current = resp;
            [manager changePollingMode:PollingModeRequest withId:[self loggedUser].current];
            _boxHeight.constant = 320;
            [self.view layoutIfNeeded];
            internal = [[DriverAnsweringView alloc] initWithFrame:_box.bounds andRequest:current];
            ((PassengerWaitingAnswerView*)internal).delegate = self;
            [_box addSubview:internal];
            [self drawRequestAsRider];
            _pointer.hidden = YES;
            [self autozoom];
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        // TODO
    } withId:[self loggedUser].current];
}

-(void)loadUserRequest {
    
    if(!current || !current.driver) {
        [[ZegoAPIManager sharedManager] getUser:^(User *resp) {
            if(resp) {
                NSLog(@"USER called loadUserRequest");
                [self updateUser:resp];
                [self innerLoadUserRequest:resp.current];
            }
        } failure:^(RKObjectRequestOperation *operation, NSError *error) {
            if(error) {
                // do nothing
            }
        } withId:[self loggedUser].uid];
    } else {
        [self innerLoadUserRequest:current.rid];
    }
}

-(void) innerLoadUserRequest:(NSInteger)rid {
    [[ZegoAPIManager sharedManager] getRequest:^(RideRequest *resp) {
        if(resp) {
            current = resp;
            [manager changePollingMode:PollingModeRequest withId:rid];
            [self clearMap];
            _boxHeight.constant = 260;
            [self.view layoutIfNeeded];
            internal = [[PassengerWaitingDriverView alloc] initWithFrame:_box.bounds andRequest:current];                ((PassengerWaitingDriverView*)internal).delegate = self;
            [_box addSubview:internal];
            [self drawRequestAsRider];
            _pointer.hidden = YES;
            [self autozoom];
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            
        }
    } withId:rid];
}
-(void)passengerFeedbackViewSubmitFeedback:(Feedback*)feedback {
    
    if(feedback.rating < 3) {
        NSString* sub = NSLocalizedString(@"Cosa non ti è piaciuto?",nil);
        
        ReasonSelectorView* rea = [[ReasonSelectorView alloc] initWithFrame:self.view.bounds title:@"" sub:sub andOptions:[self arrayWithType:@"rider.feedback.reason"]];
        rea.delegate = self;
        rea.data = @"feedback";
        rea.feedback = feedback;
        [self.view addSubview:rea];
    }
    else {
    [self startLoading];
    [[ZegoAPIManager sharedManager] feedback:^(Feedback *resp) {
        if(resp) {
            [self clearMap];
            [manager changePollingMode:PollingModeUser withId:[self loggedUser].uid];
            current = nil;
            [self setupMapForState:PassengerStatusIdle];
        }
        [self stopLoading];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        ErrorResponse* re = [self handleError:error];
        if(re) {
            [self showError:re severity:100];
        }
        [self stopLoading];
    } withFeedback:feedback];
    }
}

-(void)driverFeedbackViewSubmitFeedback:(Feedback*)feedback {
    [self startLoading];
    [[ZegoAPIManager sharedManager] feedback:^(Feedback *resp) {
        if(resp) {
            [self clearMap];
            [manager changePollingMode:PollingModeUser withId:[self loggedUser].uid];
            current = nil;
            [self setupMapForState:DriverStatusIdle];
        }
        [self stopLoading];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        ErrorResponse* re = [self handleError:error];
        if(re) {
            [self showError:re severity:100];
        }
    } withFeedback:feedback];
}


-(void)passengerWaitingAnswerView:(PassengerWaitingAnswerView *)view canceledRequest:(RideRequest *)req
{
    [self clearMap];
    [manager changePollingMode:PollingModeUser withId:[self loggedUser].uid];
    RideRequestAction* act = [[RideRequestAction alloc] init];
    act.rid = !req? current.rid :req.rid;
    act.uid = (int)[self loggedUser].uid;
    act.action = @"ridercancel";
    
    [[ZegoAPIManager sharedManager] rideAction:^(RideRequest *resp) {
        current = nil;
        [self setupMapForState:PassengerStatusIdle];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            ErrorResponse* re = [self handleError:error];
            [self showError:re severity:100];
        }
    } withAction:act];
}

-(void)passengerIdleView:(PassengerIdleView *)v changedStateTo:(NSUInteger)st withRequest:(RideRequest *)req {
    _centerOnMe.hidden = NO;
    switch (st) {
        case ViewStateReadyToSubmit:
            [self refreshUserCards];
            [self removeMarkerOfType:1];
            [self removeMarkerOfType:0];
            [self addMarkerAt:CLLocationCoordinate2DMake(req.pulat, req.pulng) ofType:0];
            [self addMarkerAt:CLLocationCoordinate2DMake(req.dolat, req.dolng) ofType:1];
             _boxHeight.constant = 300;
            
            _centerOnMe.hidden = YES;
            [self autozoom];
            break;
        case ViewStateSelectingDropoff:
            [self removeMarkerOfType:1];
            [self removeMarkerOfType:0];
            [self addMarkerAt:CLLocationCoordinate2DMake(req.pulat, req.pulng) ofType:0];
            _boxHeight.constant = 165;
            break;
        case ViewStateSelectingPickup:
            [self removeMarkerOfType:0];
            [self removeMarkerOfType:1];
            _boxHeight.constant = 115;
            break;
        default:
            break;
    }
    internal = v;
    [self.view layoutIfNeeded];
    self.navigationController.interactivePopGestureRecognizer.enabled = NO;
    [_box bringSubviewToFront:v];


}

-(void)passengerIdleView:(PassengerIdleView *)v completeAddressWithType:(NSString *)type withRequest:(RideRequest *)req{
    [self startLoading];
    
    [[ZegoAPIManager sharedManager] lastAddress:^(NSArray *resp) {
        [self stopLoading];
        if(resp) {
            UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
            EditAddressViewController * vc = (EditAddressViewController*)[storyboard instantiateViewControllerWithIdentifier:@"editAddressVCID"];
            vc.recent = [NSMutableArray arrayWithArray:resp];
            vc.type = type;
            vc.map = self;
            [self.navigationController pushViewController:vc animated:YES];
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        [self stopLoading];
    } ofType:@"all" forUserWithId:[self loggedUser].uid];
    
    
}

-(void)passengerIdleView:(PassengerIdleView *)v sendRequest:(RideRequest *)request {
    
    request.method = _method;
    
    if(([self loggedUser].payok == 0 || !card) && !([_method isEqualToString:@"cash"] && [self loggedUser].debt == 0)) {
        [self showMissingPaymentDialog];
    } else {
        NSString* cashusedsaved = [[NSUserDefaults standardUserDefaults] valueForKey:@"cashusedsaved"];
        if([request.method isEqualToString:@"cash"] && (![self loggedUser].cashused || [self loggedUser].cashused == 0) && cashusedsaved != nil) {
            SCLAlertView *alertPict = [[SCLAlertView alloc] init];
            alertPict.shouldDismissOnTapOutside = YES;
            
            
            [alertPict addButton:@"Ok" actionBlock:^(void) {
                [self checkDiscountAndSend:request];
            }];
            
            [alertPict showCustom:self image:[UIImage imageNamed:@"icon-update-w"] color:ZEGOGREEN title:@"" subTitle:NSLocalizedString(@"Pagherai il rimborso in contanti direttamente al driver. In alternativa inserisci adesso una carta di credito o prepagata",nil) closeButtonTitle:NSLocalizedString(@"Annulla",nil) duration:0.0f]; // Custom
            
            
            //Using Block
            [alertPict alertIsDismissed:^{
                
            }];
            
            NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
            [def setValue:@"YES" forKey:@"cashusedsaved"];
            [def synchronize];
        }
        else if(![self loggedUser].img || [[self loggedUser].img isEqualToString:@""]) {
            SCLAlertView *alertPict = [[SCLAlertView alloc] init];
            alertPict.shouldDismissOnTapOutside = YES;
            
            
            [alertPict addButton:@"Ok" actionBlock:^(void) {
                [self checkDiscountAndSend:request];
            }];
            
            [alertPict showCustom:self image:[UIImage imageNamed:@"icon-update-w"] color:ZEGOGREEN title:@"" subTitle:NSLocalizedString(@"Aggiungi subito la tua foto al profilo: avrai più probabilità di trovare un passaggio!",nil) closeButtonTitle:NSLocalizedString(@"Annulla",nil) duration:0.0f]; // Custom
            
            
            //Using Block
            [alertPict alertIsDismissed:^{
                
            }];
        }        
        
        else {
            [self checkDiscountAndSend:request];
        }
        
        
    }
}

-(void)checkDiscountAndSend:(RideRequest*)request {
    if(request.discount > 0) {
        SCLAlertView *alert = [[SCLAlertView alloc] init];
        alert.shouldDismissOnTapOutside = YES;
        
        
        if(request.method && [request.method isEqualToString:@"cash"]) {
            [alert addButton:@"Inserisci carta" actionBlock:^(void) {
                [self passengerIdleViewCardTapped:nil];
            }];
            
            [alert addButton:@"Avanti" actionBlock:^(void) {
                 [self innerSendRequest:request];
            }];
            
            [alert showCustom:self image:[UIImage imageNamed:@"icon-update-w"] color:ZEGOGREEN title:NSLocalizedString(@"Hai una promo attiva",nil) subTitle:[NSString stringWithFormat:NSLocalizedString(@"Hai un credito attivo di %.2f Euro, per utilizzarlo scegli la carta come modalità di pagamento",nil),(request.discount)/100.f] closeButtonTitle:NSLocalizedString(@"Annulla",nil) duration:0.0f]; // Custom
            
            
            //Using Block
            [alert alertIsDismissed:^{
                
            }];
        } else {
        
            [alert addButton:@"Ok" actionBlock:^(void) {
                [self innerSendRequest:request];
            }];
        
            [alert showCustom:self image:[UIImage imageNamed:@"icon-update-w"] color:ZEGOGREEN title:NSLocalizedString(@"Hai una promo attiva",nil) subTitle:[NSString stringWithFormat:NSLocalizedString(@"L'importo addebitato a fine passaggio sarà di %.2f Euro",nil),(request.passprice - request.discount)/100.f] closeButtonTitle:NSLocalizedString(@"Annulla",nil) duration:0.0f]; // Custom
        
        
            //Using Block
            [alert alertIsDismissed:^{
            
            }];
        }
    } else {
        [self innerSendRequest:request];
    }
}

-(void) innerSendRequest:(RideRequest*)request {
    [self startLoading];
    request.pid = (int)[self loggedUser].uid;
    request.method = _method;
    [[ZegoAPIManager sharedManager] createRequest:^(RideRequest *resp) {
        if(resp) {
            [self stopLoading];
            current = resp;
            [self setupMapForState:current.rider.rtstatus];
            if(current) {
                [manager changePollingMode:PollingModeRequest withId:current.rid];
            }
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            ErrorResponse* re = [self handleError:error];
            if(re) {
                if(re.code == 156) {
                    [self show156DialogWithMessage:re.msg];
                }
                else if(re.code == 121 || re.code == 139) {
                    [self showConfirmDialogWithRequest:request andMessage:re.msg];
                } else {
                    [self showError:re severity:500];
                }
            }
            [self stopLoading];
        }
    } withRequest:request];
}
-(void)passengerIdleViewCardTapped:(PassengerIdleView *)v {
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
    CreditCardViewController * vc = (CreditCardViewController*)[storyboard instantiateViewControllerWithIdentifier:@"cardListVCID"];
    vc.map = self;
    vc.method = _method;
    [self.navigationController pushViewController:vc animated:YES];
}
-(void)passengerViewRideCanceled:(PassengerWaitingDriverView *)view {
    NSString* sub = [[NSDate date] timeIntervalSince1970] - [current.assigndate integerValue] > 120 ? NSLocalizedString(@"Scegli un motivo per confermare la cancellazione. Confermando sarà addebitata la fee di utilizzo.",nil) : NSLocalizedString(@"Scegli un motivo per confermare la cancellazione",nil);
    
    ReasonSelectorView* rea = [[ReasonSelectorView alloc] initWithFrame:self.view.bounds title:NSLocalizedString(@"Perchè vuoi cancellare?",nil) sub:sub andOptions:[self arrayWithType:@"rider.abort.reason"]];
    rea.delegate = self;
    rea.data = @"riderabort";
    [self.view addSubview:rea];
}

-(void)passengerViewRideCallDriver:(PassengerWaitingDriverView *)view {
    SCLAlertView *alert = [[SCLAlertView alloc] init];
    alert.shouldDismissOnTapOutside = YES;
    
    
    [alert addButton:NSLocalizedString(@"Telefona",nil) actionBlock:^(void) {
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"tel://0283905112"]];
    }];
    
    NSString* msg = [NSString stringWithFormat:NSLocalizedString(@"Vuoi contattare %@?",nil),current.driver.name];
    [alert showCustom:self image:[UIImage imageNamed:@"cornetta"] color:ZEGOGREEN title:@"" subTitle:msg closeButtonTitle:NSLocalizedString(@"Annulla",nil) duration:0.0f]; // Custom
    [alert setLastButtonRed];
    
    
    //Using Block
    [alert alertIsDismissed:^{
        
    }];
}

-(void)passengerOnRideReportIssue:(PassengerOnRideView*)view {
    
    SCLAlertView *alert = [[SCLAlertView alloc] init];
    alert.shouldDismissOnTapOutside = YES;
    
    
    [alert addButton:NSLocalizedString(@"Termina passaggio",nil) actionBlock:^(void) {
        [self performAction:@"riderterminate" andText:nil];
    }];
    
    [alert showCustom:self image:[UIImage imageNamed:@"icon-update-w"] color:ZEGOGREEN title:NSLocalizedString(@"Hai avuto un problema?", nil) subTitle:NSLocalizedString(@"Se hai avuto un problema clicca su Interrompi per fermare il passaggio e scrivi a support@zegoapp.com per segnalarci il problema",nil) closeButtonTitle:NSLocalizedString(@"Annulla",nil) duration:0.0f]; // Custom
    
    
    //Using Block
    [alert alertIsDismissed:^{
        
    }];
    
    
}

-(void)driverAnsweringView:(DriverAnsweringView *)view acceptedRequest:(BOOL)accept {
    [self performAction:accept ? @"driveraccpet" : @"driverreject" andText:nil];
}


-(void)passengerOnRideShareRide:(PassengerOnRideView*)view {
    NSString* code = [NSString stringWithFormat:NSLocalizedString(@"Ciao, segui il mio passaggio con Zego cliccando qui http://share.zegoapp.com/%@",nil),current.shid];
    NSArray *items   = [NSArray arrayWithObjects:code, nil];
    
    UIActivityViewController *shareChooser = [[UIActivityViewController alloc]
                                              initWithActivityItems:items applicationActivities:nil];
    [shareChooser setExcludedActivityTypes: @[UIActivityTypeAssignToContact,
                                              UIActivityTypePrint,
                                              UIActivityTypeSaveToCameraRoll]];
    [self presentViewController:shareChooser animated:YES completion:nil];

}

-(void)driverPikcingUpViewNavigateToDropoff:(DriverPickingUpView *)view {
    [self navigateTo:CLLocationCoordinate2DMake(current.pulat, current.pulng)];
}

-(void)driverPikcingUpViewCalled:(DriverPickingUpView *)view {
    SCLAlertView *alert = [[SCLAlertView alloc] init];
    alert.shouldDismissOnTapOutside = YES;
    
    
    [alert addButton:NSLocalizedString(@"Telefona",nil) actionBlock:^(void) {
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"tel://0283905112"]];
    }];
    
    [alert addButton:NSLocalizedString(@"Avvisa che sei arrivato",nil) actionBlock:^(void) {
        [self performAction:@"iamhere" andText:nil];
    }];
    NSString* msg = [NSString stringWithFormat:NSLocalizedString(@"Vuoi contattare %@?",nil),current.rider.name];
    [alert showCustom:self image:[UIImage imageNamed:@"cornetta"] color:ZEGOGREEN title:@"" subTitle:msg closeButtonTitle:NSLocalizedString(@"Annulla",nil) duration:0.0f]; // Custom
    [alert setLastButtonRed];
    
    //Using Block
    [alert alertIsDismissed:^{
        
    }];
}

-(void)driverPikcingUpViewAbortedRide:(DriverPickingUpView *)view {
    NSString* sub = NSLocalizedString(@"Scegli un motivo per confermare la cancellazione",nil);
    
    ReasonSelectorView* rea = [[ReasonSelectorView alloc] initWithFrame:self.view.bounds title:NSLocalizedString(@"Perchè vuoi cancellare?",nil) sub:sub andOptions:[self arrayWithType:@"driver.abort.reason"]];
    rea.delegate = self;
    rea.data = @"driverabort";
    [self.view addSubview:rea];
}

-(void)driverOnRideView:(DriverOnRideView *)view terminatedRideWithPrice:(NSInteger)upPrice {
    
    
    SCLAlertView *alert = [[SCLAlertView alloc] init];
    alert.shouldDismissOnTapOutside = YES;
    
    
    [alert addButton:NSLocalizedString(@"Confermo",nil) actionBlock:^(void) {
        RideRequestAction* action = [[RideRequestAction alloc] init];
        action.uid = (int)[self loggedUser].uid;
        action.rid = current.rid;
        action.text = nil;
        action.action = @"driverend";
        action.priceupdate = (int)upPrice;
        [self sendAction:action];
    }];
    
    
    
    NSString* msg = [NSString stringWithFormat:NSLocalizedString(@"Confermi di essere arrivato a destinazione?",nil),current.rider.name];
    [alert showCustom:self image:[UIImage imageNamed:@"icon-update-w"] color:ZEGOGREEN title:@"" subTitle:msg closeButtonTitle:NSLocalizedString(@"Annulla",nil) duration:0.0f]; // Custom
    
    
    //Using Block
    [alert alertIsDismissed:^{
        
    }];
    
    
}

-(void)driverOnRideNavigateToDropoff:(DriverOnRideView *)view {
    [self navigateTo:CLLocationCoordinate2DMake(current.dolat, current.dolng)];
}

-(void)driverOnRideMinimumPrice:(DriverOnRideView *)view {
    _errorMsg.text = NSLocalizedString(@"Nessun rimborso driver",nil);
    _error.backgroundColor = ZEGODARKGREY;
    _errorIcon.hidden = YES;
    _error.hidden = NO;
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        _error.hidden = YES;
    });
}


-(void)driverPikcingUpViewStartedRide:(DriverPickingUpView *)view {
    
    SCLAlertView *alert = [[SCLAlertView alloc] init];
    alert.shouldDismissOnTapOutside = YES;
    
    
    [alert addButton:NSLocalizedString(@"Confermo",nil) actionBlock:^(void) {
        [self performAction:@"driverstart" andText:nil];
    }];
    
    
    
    NSString* msg = [NSString stringWithFormat:NSLocalizedString(@"Confermi che %@ è a bordo?",nil),current.rider.name];
    [alert showCustom:self image:[UIImage imageNamed:@"icon-update-w"] color:ZEGOGREEN title:@"" subTitle:msg closeButtonTitle:NSLocalizedString(@"Annulla",nil) duration:0.0f]; // Custom
    
    
    //Using Block
    [alert alertIsDismissed:^{
        
    }];
    
    
}
-(void) reasonSelector:(ReasonSelectorView *)selector selected:(NSString *)reason atIdx:(NSInteger)idx {
    [selector removeFromSuperview];
    if([selector.data isEqualToString:@"driverabort"]) {
        RideRequestAction* action = [[RideRequestAction alloc] init];
        action.uid = (int)[self loggedUser].uid;
        action.rid = current.rid;
        action.text = reason;
        action.action = @"driverabort";
        action.capture = idx == 3 ? 1 : 0;
        [self sendAction:action];
    } else {
        [self performAction:selector.data andText:reason];
    }
}

-(void) reasonSelector:(ReasonSelectorView *)selector sendFeedback:(Feedback *)fe {
    [selector removeFromSuperview];
    [self startLoading];
    [[ZegoAPIManager sharedManager] feedback:^(Feedback *resp) {
        if(resp) {
            [self clearMap];
            [manager changePollingMode:PollingModeUser withId:[self loggedUser].uid];
            current = nil;
            [self setupMapForState:PassengerStatusIdle];
        }
        [self stopLoading];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        ErrorResponse* re = [self handleError:error];
        if(re) {
            [self showError:re severity:100];
        }
    } withFeedback:fe];
}
-(void) reasonSelectorCanceled:(ReasonSelectorView *)selector {
    [selector removeFromSuperview];
}
-(void)locationUpdated:(CLLocationCoordinate2D)co {
    coord = co;
    if(lastState >= 200) {
        if(!myselfDriving) {
            myselfDriving = [GMSMarker markerWithPosition:co];
            myselfDriving.map = _map;
            myselfDriving.icon = [UIImage imageNamed:@"car_pop_up"];
        } else {
            myselfDriving.position = co;
        }
        if(followUser) {
            [_map animateToLocation:coord];
        }
    }
}
-(void)userUpdated:(User*)us {
    NSInteger oldstat = 0;
    oldstat = u.rtstatus;
    u = us;
    NSLog(@"USER called userUpdated");
    [self updateUser:u];
    
    if(u && oldstat != u.rtstatus) {
        [self setupMapForState:u.rtstatus];
        if(u.rtstatus == DriverStatusIdle) {
            if(current.status == RideRequestStatusPassengerCanceled || current.status == RideRequestStatusPassengerAborted ) {
                [self showBackToIdleDialog:NSLocalizedString(@"Il passeggero ha cancellato la richiesta.",nil)];
            } else if(current.status == RideRequestStatusSystemCanceled) {
                [self showBackToIdleDialog:NSLocalizedString(@"Richiesta cancellata.",nil)];
            } else if(current.status == RideRequestStatusDriverAnswered) {
                [self showBackToIdleDialog:NSLocalizedString(@"Richiesta accettata da un altro driver.",nil)];
            }
        }
    }
    
}

-(void)requestUpdated:(RideRequest*)req {
    if([self.loggedUser.umode isEqualToString:@"rider"]) {
        
        if(!current) {
            current = req;
            [self drawRequestAsRider];
        } else {
            NSInteger oldstat = current.rider.rtstatus;
            current = req;
            NSInteger newstat = current.rider.rtstatus;
            if(newstat == 0) {
                newstat = PassengerStatusIdle;
            }
            if(oldstat != newstat) {
                [self setupMapForState:newstat];
                if(newstat == PassengerStatusIdle) {
                    if(current.status == RideRequestStatusDriverAborted) {
                        [self showBackToIdleDialog:NSLocalizedString(@"Il driver ha cancellato la richiesta.",nil)];
                    } else if(current.status == RideRequestStatusSystemCanceled) {
                        [self showBackToIdleDialog:NSLocalizedString(@"Nessun driver ha accettato la tua richiesta.",nil)];
                    }
                }
            } else {
                
                [self addOrUpdateRequestDriver];
                if(newstat == PassengerStatusWaitingForDriver && [internal isKindOfClass:[PassengerWaitingDriverView class]]) {
                    [((PassengerWaitingDriverView*)internal) updateEtaTo:current.drivereta];
                }
            }
        }
    } else {
        if(!current) {
            current = req;
            [self drawRequestAsDriver];
        } else {
            NSInteger oldstat = current.driver.rtstatus;
            current = req;
            NSInteger newstat = current.driver.rtstatus;
            if(newstat == 0) {
                newstat = DriverStatusIdle;
            }
            
            if(current.status >= RideRequestStatusDriverAnswered) {
                if(current.did != [self loggedUser].uid) {
                    [self setupMapForState:DriverStatusIdle];
                }
            }
            
            if(oldstat != newstat) {
                [self setupMapForState:newstat];
                if(current.rider.rtstatus == PassengerStatusIdle) {
                    if(current.status == RideRequestStatusPassengerCanceled || current.status == RideRequestStatusPassengerAborted) {
                        [self showBackToIdleDialog:NSLocalizedString(@"Il passeggero ha cancellato la richiesta.",nil)];
                    } else if (current.status == RideRequestStatusPassengerTerminated) {
                        [self showBackToIdleDialog:NSLocalizedString(@"Il passeggero ha terminato la richiesta.",nil)];
                    } else if (current.status == RideRequestStatusSystemCanceled) {
                       [self showBackToIdleDialog:NSLocalizedString(@"Richiesta cancellata.",nil)];
                    }
                }
            } else {
                
            }
        }
    }
    
}

-(void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [manager unregisterObserver:self];

    self.navigationController.interactivePopGestureRecognizer.enabled = YES;
    self.navigationController.navigationItem.backBarButtonItem.enabled = YES;
}

-(void)addMarker:(CLLocationCoordinate2D)c forCompatDriver:(CompactDriver*)d {
    
    GMSMarker *dmark = [GMSMarker markerWithPosition:c];
    dmark.map = _map;
    dmark.userData = d;
    dmark.icon = d.did == [self loggedUser].uid ? [UIImage imageNamed:@"dr_standard_icon_hand"] : [UIImage imageNamed:@"driver_blu_hand"];
    [markers setObject:dmark forKey:[NSNumber numberWithInt:d.did]];
    
    if( d.did == [self loggedUser].uid ){
        myselfDriving = dmark;
    }
    
}


-(void)addOrUpdateRequestDriver
{
    if(driver) {
        driver.position = CLLocationCoordinate2DMake(current.driver.lat, current.driver.lng);
    } else {
        driver = [GMSMarker markerWithPosition:CLLocationCoordinate2DMake(current.driver.lat, current.driver.lng)];
        driver.map = _map;
        driver.icon = [UIImage imageNamed:@"dr_standard_icon"];
    }
    
}



-(void)addMarkerAt:(CLLocationCoordinate2D)c ofType:(NSInteger)t{
    if(t == 0){
        pickup = [GMSMarker markerWithPosition:c];
        pickup.map = _map;
        pickup.icon = [UIImage imageNamed:@"grayflag"];
    } else {
        dropoff = [GMSMarker markerWithPosition:c];
        dropoff.map = _map;
        dropoff.icon = [UIImage imageNamed:@"flag_icon"];
    }
}

-(void)removeMarkerOfType:(NSInteger)t {
    if(t == 0) {
        if(pickup){
            pickup.map = nil;
        }
    } else {
        if(dropoff) {
            dropoff.map = nil;
        }        
    }
}
-(void) loadCards {
    [self refreshUserCards];
}


-(void) showError:(ErrorResponse*)er severity:(NSInteger)severity {
    
    _errorMsg.text = er.msg;
    _errorMsg.numberOfLines = 0;
    _error.backgroundColor = severity > 0 ? ZEGOGREEN70 : ZEGODARKGREY;
    _errorIcon.hidden = YES;
    _error.hidden = NO;
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(4 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        _error.hidden = YES;
    });
}

-(void) showGPSError {
    _error.backgroundColor = ZEGODARKGREY;
    _errorMsg.text = NSLocalizedString(@"Segnale GPS assente",nil);
    _errorIcon.image = [UIImage imageNamed:@"nogps"];
    _errorIcon.hidden = NO;
    _error.hidden = NO;
}


-(void) showNetworkError {
    _error.backgroundColor = ZEGODARKGREY;
    _errorMsg.text = NSLocalizedString(@"Connessione Assente",nil);
    _errorIcon.image = [UIImage imageNamed:@"connessione_icon"];
    _errorIcon.hidden = NO;
    _error.hidden = NO;
}

-(void)reachabilityChanged:(NSNotification*)reach {
    if(reach) {
        Reachability* r = reach.object;
        BOOL connected = [r isReachable];
        if(!connected) {
            [self showNetworkError];
        } else {
            _error.hidden = YES;
        }
    }
}

-(void)gpsNo:(NSNotification*)reach {
    [self showGPSError];
}

-(void)gpsYes:(NSNotification*)reach {
     _error.hidden = YES;
}

-(void)addressSelected:(Address*)a forType:(NSString*)ty {
    lastEditedAddress = a;
    [_map animateToLocation:CLLocationCoordinate2DMake(a.lat, a.lng)];
    if([internal isKindOfClass:[PassengerIdleView class]]) {
        [((PassengerIdleView*)internal) updateViewWithAddress:a andType:ty];
    }
    
}

-(void)passengerIdleViewAlertMinimumPrice:(PassengerIdleView*)v {
    _errorMsg.text = NSLocalizedString(@"Nessun rimborso driver",nil);
    _error.backgroundColor = ZEGODARKGREY;
    _errorIcon.hidden = YES;
    _error.hidden = NO;
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        _error.hidden = YES;
    });
}

-(void)showConfirmDialogWithRequest:(RideRequest*)req  andMessage:(NSString*)msg{

    SCLAlertView *alert = [[SCLAlertView alloc] init];
    alert.shouldDismissOnTapOutside = YES;
    
    
    [alert addButton:NSLocalizedString(@"Conferma",nil) actionBlock:^(void) {
        [[ZegoAPIManager sharedManager] forceRequest:^(RideRequest *resp) {
            if(resp) {
                current = resp;
                [self setupMapForState:current.rider.rtstatus];
            }
        } failure:^(RKObjectRequestOperation *operation, NSError *error) {
            if(error) {
                ErrorResponse* re = [self handleError:error];
                if(re) {
                    [self showError:re severity:500];
                }
            }
        } withRequest:req];
    }];
    
    [alert showCustom:self image:[UIImage imageNamed:@"icon-update-w"] color:ZEGOGREEN title:NSLocalizedString(@"Attenzione",nil) subTitle:msg closeButtonTitle:NSLocalizedString(@"Annulla",nil) duration:0.0f]; // Custom
    
    
    //Using Block
    [alert alertIsDismissed:^{
        
    }];
    
}

-(void)show156DialogWithMessage:(NSString*)msg{
    
    SCLAlertView *alert = [[SCLAlertView alloc] init];
    alert.shouldDismissOnTapOutside = YES;
    
    
    [alert addButton:NSLocalizedString(@"Conferma",nil) actionBlock:^(void) {
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
        StripeAddCardViewController * vc = (StripeAddCardViewController*)[storyboard instantiateViewControllerWithIdentifier:@"cardListVCID"];
        vc.map = self;
        [self.navigationController pushViewController:vc animated:YES];
    }];
    
    [alert showCustom:self image:[UIImage imageNamed:@"icon-update-w"] color:ZEGOGREEN title:NSLocalizedString(@"Attenzione",nil) subTitle:msg closeButtonTitle:NSLocalizedString(@"Annulla",nil) duration:0.0f]; // Custom
    
    
    //Using Block
    [alert alertIsDismissed:^{
        
    }];
    
}

-(void)showMissingPaymentDialog {
    
    SCLAlertView *alert = [[SCLAlertView alloc] init];
    alert.shouldDismissOnTapOutside = YES;
    
    
    [alert addButton:NSLocalizedString(@"Inserisci",nil) actionBlock:^(void) {
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
        StripeAddCardViewController * vc = (StripeAddCardViewController*)[storyboard instantiateViewControllerWithIdentifier:@"stripeVCID"];
        vc.map = self;
        [self.navigationController pushViewController:vc animated:YES];
    }];
    
    [alert showCustom:self image:[UIImage imageNamed:@"icon-update-w"] color:ZEGOGREEN title:NSLocalizedString(@"Attenzione",nil) subTitle:NSLocalizedString(@"Per chiedere un passaggio devi avere un metodo di pagamento valido.",nil) closeButtonTitle:NSLocalizedString(@"Chiudi",nil) duration:0.0f]; // Custom
    
    
    //Using Block
    [alert alertIsDismissed:^{
        
    }];
    
}

-(void)showBackToIdleDialog:(NSString*)msg {
    
    SCLAlertView *alert = [[SCLAlertView alloc] init];
    alert.shouldDismissOnTapOutside = YES;
    
    BOOL notitle = [msg isEqualToString:NSLocalizedString(@"Richiesta cancellata.",nil)] || [msg isEqualToString:NSLocalizedString(@"Nessun driver ha accettato la tua richiesta.",nil)];
    
    [alert showCustom:self image:[UIImage imageNamed:@"icon-update-w"] color:ZEGOGREEN title:notitle ? @"" : NSLocalizedString(@"Attenzione",nil) subTitle:msg closeButtonTitle:NSLocalizedString(@"Chiudi",nil) duration:0.0f]; // Custom
    
    //Using Block
    [alert alertIsDismissed:^{
        
    }];
    
}

-(void)useCash {
    if(internal && [internal isKindOfClass:[PassengerIdleView class]]) {
        _method = @"cash";
        [((PassengerIdleView*)internal) selectCash];
    }
}

-(void)refreshUserCards {
    _method = @"card";
    [[ZegoAPIManager sharedManager] userCards:^(NSArray *resp) {
        cards = [[NSMutableArray alloc] initWithArray:resp];
        for(StripeCard* sc in cards) {
            if(sc.preferred == 1) {
                card = sc;
            }
        }
        if(!cards || [cards count] == 0) {
            _method = @"cash";
        }
        if(internal && [internal isKindOfClass:[PassengerIdleView class]]) {
            [((PassengerIdleView*)internal) updateViewWithCards:cards];
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
    } forUserWithId:u.uid];
}


-(void)autozoom {
    
    if(coord.latitude*coord.longitude != 0) {
    CGFloat h = self.view.frame.size.height;
    CGFloat b = _boxHeight.constant;
    CGFloat maxlat = coord.latitude;
    CGFloat minlat = coord.latitude;
    
    
    GMSCoordinateBounds *bounds = [[GMSCoordinateBounds alloc] initWithCoordinate:coord coordinate:coord];
    
    if(pickup) {
        bounds = [bounds includingCoordinate:pickup.position];
        maxlat = MAX(maxlat, pickup.position.latitude);
        minlat = MIN(minlat,pickup.position.latitude);
    }
    
    if(dropoff) {
        bounds = [bounds includingCoordinate:dropoff.position];
        maxlat = MAX(maxlat, dropoff.position.latitude);
        minlat = MIN(minlat,dropoff.position.latitude);
    }
    
    if(driver) {
        bounds = [bounds includingCoordinate:driver.position];
        maxlat = MAX(maxlat, driver.position.latitude);
        minlat = MIN(minlat,driver.position.latitude);
    }
    
    if(maxlat!=minlat) {
        CLLocationCoordinate2D delta = CLLocationCoordinate2DMake(minlat - (maxlat-minlat)*(2*b/h), coord.longitude);
        bounds = [bounds includingCoordinate:delta];
        
        CLLocationCoordinate2D deltaTop = CLLocationCoordinate2DMake(maxlat + (maxlat-minlat)*0.1, coord.longitude);
        bounds = [bounds includingCoordinate:deltaTop];
    }
    
    
    [_map animateWithCameraUpdate:[GMSCameraUpdate fitBounds:bounds withPadding:35.0f]];
    }
}

-(void) drawRequestAsRider {
    if(current) {
        
        [self addMarkerAt:CLLocationCoordinate2DMake(current.pulat, current.pulng) ofType:0];
        [self addMarkerAt:CLLocationCoordinate2DMake(current.dolat, current.dolng) ofType:1];
        if(current.status >= RideRequestStatusDriverAnswered) {
            [self addOrUpdateRequestDriver];
        }
    }
    [self autozoom];
}

-(void) drawRequestAsDriver {
    if(current) {
        
        [self addMarkerAt:CLLocationCoordinate2DMake(current.pulat, current.pulng) ofType:0];
        [self addMarkerAt:CLLocationCoordinate2DMake(current.dolat, current.dolng) ofType:1];
        
    }
    [self autozoom];
}

-(void) clearMap {
    [_map clear];
    pickup = nil;
    dropoff = nil;
    driver = nil;
    markers = [[NSMutableDictionary alloc] init];
    _centerOnMe.hidden = NO;
}

-(void)performAction:(NSString*)act andText:(NSString*)text {
    RideRequestAction* action = [[RideRequestAction alloc] init];
    action.uid = (int)[self loggedUser].uid;
    action.rid = current.rid;
    action.text = text;
    action.action = act;
    [self sendAction:action];
}

-(void)sendAction:(RideRequestAction*)action {
    [self startLoading];
    [[ZegoAPIManager sharedManager] rideAction:^(RideRequest *resp) {
        if(resp) {
            current = resp;
            // generalized transaction here
            if([u.umode isEqualToString:@"rider"]) {
                [self setupMapForState:resp.rider.rtstatus];
            } else {
                [self setupMapForState:resp.driver.rtstatus];
            }
        }
        [self stopLoading];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        ErrorResponse* re = [self handleError:error];
        if(re) {
            [self showError:re severity:100];
        }
        [self stopLoading];
    } withAction:action];
}

- (void)mapView:(GMSMapView *)mapView willMove:(BOOL)gesture
{
    if(gesture) {
        followUser = NO;
    }
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
