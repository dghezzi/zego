//
//  ZegoAPIManager.m
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoAPIManager.h"
#import "LocationManager.h"

@implementation ZegoAPIManager

static ZegoAPIManager *sharedManager = nil;


// GCD based singleton implementation
+ (id)sharedManager {
    if(sharedManager == nil)
    {
        NSString* urlStr = TEST == 0 ? @"https://v2.sharethecity.net" : @"http://test.zegoapp.com:8080";
        NSURL *url = [NSURL URLWithString:urlStr];        
        sharedManager  = [self managerWithBaseURL:url];
        sharedManager.requestSerializationMIMEType = RKMIMETypeJSON;
        Reachability* reach = [Reachability reachabilityWithHostname:@"v2.sharethecity.net"];
        [reach startNotifier];
    }
    
    
    
    return sharedManager;
}

- (void) setAccessToken:(NSString*)accessToken
{
    [sharedManager.HTTPClient setDefaultHeader:@"zegotoken" value:accessToken];
}

- (void) setupWithMappingFactory: (ZegoMappingFactory*)factory
{
    _factory = factory;
    // once the manager is created set up all the descriptors
    [sharedManager setupRequestDescriptors];
    [sharedManager setupResponseDescriptors];
}

- (void) globalConf:(void (^)(NSMutableArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(BootRequest *)br {
    [self postObject:br path:@"/zego/v1/conf/global" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {           
            success([NSMutableArray arrayWithArray:mappingResult.array]);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];

}


- (void) login:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(BootRequest *)br {
    
    [self postObject:br path:@"/zego/v1/user/login" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            User *u = (User *)[mappingResult.array firstObject];
            if(u && u.zegotoken)
            {
                [self setAccessToken:u.zegotoken];
            }
            if(u) {
                [[LocationManager sharedManager] configureForMode:u.umode];
            }
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) silentLogin:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(BootRequest *)br {
    
    [self postObject:br path:@"/zego/v1/user/silent" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            User *u = (User *)[mappingResult.array firstObject];
            if(u && u.zegotoken)
            {
                [self setAccessToken:u.zegotoken];
            }
            if(u) {
                [[LocationManager sharedManager] configureForMode:u.umode];
            }
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
    
}

- (void) signup:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(BootRequest *)br {
    
    [self postObject:br path:@"/zego/v1/user/signup" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            User *u = (User *)[mappingResult.array firstObject];
            if(u && u.zegotoken)
            {
                [self setAccessToken:u.zegotoken];
            }
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
    
}

- (void) validatePin:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(PinRequest *)br {
    
    [self postObject:br path:@"/zego/v1/user/validate" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            User *u = (User *)[mappingResult.array firstObject];
            if(u && u.zegotoken)
            {
                [self setAccessToken:u.zegotoken];
            }
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
    
}

- (void) validateReferral:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(ReferralRequest *)br {
    
    [self postObject:br path:@"/zego/v1/user/referral" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            User *u = (User *)[mappingResult.array firstObject];
            if(u && u.zegotoken)
            {
                [self setAccessToken:u.zegotoken];
            }
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];

    
}

- (void) addStripeCard:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(StripeSaveRequest *)br {
    
    
    [self postObject:br path:@"/zego/v1/user/stripe" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            User *u = (User *)[mappingResult.array firstObject];
            if(u && u.zegotoken)
            {
                [self setAccessToken:u.zegotoken];
            }
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];

    
}

- (void) complete:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(User *)ur {
    [self postObject:ur path:@"/zego/v1/user/complete" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            User *u = (User *)[mappingResult.array firstObject];
            if(u && u.zegotoken)
            {
                [self setAccessToken:u.zegotoken];
            }
            if(u) {
                [[LocationManager sharedManager] configureForMode:u.umode];
            }
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];

}


- (void) kill:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(User *)u {
    [self postObject:u path:@"/zego/v1/user/kill" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            User *ur = (User *)[mappingResult.array firstObject];
            success(ur);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) update:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(User *)ur {
    [self postObject:ur path:@"/zego/v1/user" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            User *u = (User *)[mappingResult.array firstObject];
            if(u && u.zegotoken)
            {
                [self setAccessToken:u.zegotoken];
            }
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
    
}

- (void) paydebt:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(User *)u {
    [self postObject:u path:@"/zego/v1/user/paydebt" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            User *u = (User *)[mappingResult.array firstObject];
            if(u && u.zegotoken)
            {
                [self setAccessToken:u.zegotoken];
            }
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}


- (void) updatePut:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(User *)ur {
    [self putObject:ur path:@"/zego/v1/user" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            User *u = (User *)[mappingResult.array firstObject];
            if(u && u.zegotoken)
            {
                [self setAccessToken:u.zegotoken];
            }
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
    
}

- (void) resendPin:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(PinResendRequest *)br {
    [self postObject:br path:@"/zego/v1/user/resend" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            User *u = (User *)[mappingResult.array firstObject];
            if(u && u.zegotoken)
            {
                [self setAccessToken:u.zegotoken];
            }
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) getUser:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withId:(NSInteger )uid
{
    [self getObject:nil path:[NSString stringWithFormat:@"/zego/v1/user/%ld",uid] parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            User *u = (User *)[mappingResult.array firstObject];
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}


- (void) autocomplete:(void (^)(AutocompleteResponse *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(AutocompleteRequest*)req {
    [self postObject:req path:@"/zego/v1/google/geocomplete" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            AutocompleteResponse *u = (AutocompleteResponse *)[mappingResult.array firstObject];            
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) saveAddress:(void (^)(Address *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withAddress:(Address *)u {
    [self postObject:u path:@"/zego/v1/address" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            Address *ad = (Address *)[mappingResult.array firstObject];
            success(ad);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) getAddressesForUser:(void (^)(NSMutableArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withUid:(NSInteger)uid {
    [self getObject:nil path:[NSString stringWithFormat:@"/zego/v1/address/filter/uid/%ld/range/0/100",uid] parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            NSMutableArray *narr = [NSMutableArray arrayWithArray:[mappingResult array]];
            success(narr);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) geocode:(void (^)(GeoCodeResponse *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(GeoCodeRequest *)u {
    [self postObject:u path:@"/zego/v1/google/geocode" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            GeoCodeResponse *ad = (GeoCodeResponse *)[mappingResult.array firstObject];
            success(ad);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}


- (void) eta:(void (^)(EtaResponse *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(EtaRequest *)req {
    [self postObject:req path:@"/zego/v1/riderequest/eta" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            EtaResponse *ad = (EtaResponse *)[mappingResult.array firstObject];
            success(ad);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) price:(void (^)(PriceResponse *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(PriceRequest *)req {
    [self postObject:req path:@"/zego/v1/riderequest/price" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            PriceResponse *ad = (PriceResponse *)[mappingResult.array firstObject];
            success(ad);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) rideAction:(void (^)(RideRequest *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withAction:(RideRequestAction *)req {
    [self postObject:req path:@"/zego/v1/riderequest/action" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            RideRequest *ad = (RideRequest *)[mappingResult.array firstObject];
            success(ad);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) feedback:(void (^)(Feedback *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withFeedback:(Feedback *)req {
    [self postObject:req path:@"/zego/v1/feedback" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            Feedback *fe = (Feedback *)[mappingResult.array firstObject];
            success(fe);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) userCards:(void (^)(NSArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure forUserWithId:(NSUInteger)uid {
    [self getObject:nil path:[NSString stringWithFormat:@"/zego/v1/user/cards/%ld",uid] parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            NSMutableArray *narr = [NSMutableArray arrayWithArray:[mappingResult array]];
            success(narr);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) lastAddress:(void (^)(NSArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure ofType:(NSString*)type forUserWithId:(NSUInteger)uid {
    [self getObject:nil path:[NSString stringWithFormat:@"/zego/v1/address/%ld/last/%@",uid,type] parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            NSMutableArray *narr = [NSMutableArray arrayWithArray:[mappingResult array]];
            success(narr);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) createRequest:(void (^)(RideRequest *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(RideRequest *)req {
    [self postObject:req path:@"/zego/v1/riderequest" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            RideRequest *resp = (RideRequest *)[mappingResult.array firstObject];
            success(resp);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) updateRequestPrice:(void (^)(RideRequest *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(RideRequest *)req {
    [self postObject:req path:@"/zego/v1/riderequest/priceupdate" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            RideRequest *resp = (RideRequest *)[mappingResult.array firstObject];
            success(resp);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}
- (void) forceRequest:(void (^)(RideRequest *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(RideRequest *)req {
    [self postObject:req path:@"/zego/v1/riderequest/force" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            RideRequest *resp = (RideRequest *)[mappingResult.array firstObject];
            success(resp);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) getRequest:(void (^)(RideRequest *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withId:(NSInteger )uid {
    [self getObject:nil path:[NSString stringWithFormat:@"/zego/v1/riderequest/%ld",uid] parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            RideRequest *u = (RideRequest *)[mappingResult.array firstObject];
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) postLocation:(void (^)(Location *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure loc:(Location*)l {
    [self postObject:l path:@"/zego/v1/location" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            Location *resp = (Location *)[mappingResult.array firstObject];
            success(resp);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) makeCardDefault:(void (^)(NSArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure forUser:(NSInteger)uid withData:(StripeCard*)card {
    [self postObject:card path:[NSString stringWithFormat:@"/zego/v1/user/%ld/card/makedefault",uid] parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            NSArray *resp = [mappingResult array];
            success(resp);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) deleteUserCard:(void (^)(NSArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure forUser:(NSInteger)uid withData:(StripeCard*)card {
    [self postObject:card path:[NSString stringWithFormat:@"/zego/v1/user/%ld/card/delete",uid] parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            NSArray *resp = [mappingResult array];
            success(resp);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) userPromo:(void (^)(NSArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure forUserWithId:(NSUInteger)uid {
    [self getObject:nil path:[NSString stringWithFormat:@"/zego/v1/userpromo/uid/%ld",uid] parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            NSArray *u = mappingResult.array;
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) redeemPromo:(void (^)(NSArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withData:(RedeemRequest*)data {
    [self postObject:data path:[NSString stringWithFormat:@"/zego/v1/promo/redeem"] parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            NSArray *resp = [mappingResult array];
            success(resp);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) userRides:(void (^)(NSArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withMode:(NSString*)mode forUserWithId:(NSUInteger)uid {
    [self getObject:nil path:[NSString stringWithFormat:@"/zego/v1/riderequest/history/%ld/%@/0/200",uid,mode] parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            NSArray *u = mappingResult.array;
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) userChangeMode:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure to:(NSString*)mode withUserData:(User*)user {
    [self postObject:user path:[NSString stringWithFormat:@"/zego/v1/user/changemodeto/%@",mode] parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            User *resp = [mappingResult array].firstObject;
            [[LocationManager sharedManager] configureForMode:resp.umode];
            success(resp);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}


- (void) getAllAreas:(void (^)(NSArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure {
    
    [self getObject:nil path:[NSString stringWithFormat:@"/zego/v1/area"] parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            NSArray *u = mappingResult.array;
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}
- (void) getUserDriverData:(void (^)(DriverData *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure forUserId:(NSInteger)uid {
    [self getObject:nil path:[NSString stringWithFormat:@"/zego/v1/driverdata/user/%ld",uid] parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            DriverData *u = [mappingResult.array firstObject];
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

- (void) createDriverData:(void (^)(DriverData *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withData:(DriverData*)data {
    [self postObject:data path:[NSString stringWithFormat:@"/zego/v1/driverdata"] parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        if (success) {
            DriverData *u = [mappingResult.array firstObject];
            success(u);
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if (failure) {
            failure(operation, error);
        }
    }];
}

#pragma mark - Setup Helpers

- (void) setupRequestDescriptors
{
    RKRequestDescriptor *bootRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory bootRequestMapping] inverseMapping] objectClass:[BootRequest class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:bootRequestDescriptor];
    
    RKRequestDescriptor *userRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory userMapping] inverseMapping] objectClass:[User class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:userRequestDescriptor];
    
    RKRequestDescriptor *userPutRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory userMapping] inverseMapping] objectClass:[User class] rootKeyPath:nil method:RKRequestMethodPUT];
    [self addRequestDescriptor:userPutRequestDescriptor];
    
    RKRequestDescriptor *pinRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory pinRequestMapping] inverseMapping] objectClass:[PinRequest class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:pinRequestDescriptor];
    
    RKRequestDescriptor *pinResendReqDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory pinResendMapping] inverseMapping] objectClass:[PinResendRequest class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:pinResendReqDescriptor];
    
    RKRequestDescriptor *confReqDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory confMapping] inverseMapping] objectClass:[Conf class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:confReqDescriptor];
    
    RKRequestDescriptor *referralRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory referralRequestMapping] inverseMapping] objectClass:[ReferralRequest class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:referralRequestDescriptor];
    
    RKRequestDescriptor *stripeRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory stripeSaveRequestMapping] inverseMapping] objectClass:[StripeSaveRequest class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:stripeRequestDescriptor];
    
    RKRequestDescriptor *autocompleteRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory autocompleteRequestMapping] inverseMapping] objectClass:[AutocompleteRequest class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:autocompleteRequestDescriptor];    
    
    RKRequestDescriptor *addressRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory addressMapping] inverseMapping] objectClass:[Address class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:addressRequestDescriptor];
    
    RKRequestDescriptor *geoRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory geoCodeRequestMapping] inverseMapping] objectClass:[GeoCodeRequest class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:geoRequestDescriptor];
    
    RKRequestDescriptor *etaRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory etaRequestMapping] inverseMapping] objectClass:[EtaRequest class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:etaRequestDescriptor];
    
    RKRequestDescriptor *priceRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory priceRequestMapping] inverseMapping] objectClass:[PriceRequest class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:priceRequestDescriptor];
    
    RKRequestDescriptor *actionRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory actionMapping] inverseMapping] objectClass:[RideRequestAction class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:actionRequestDescriptor];
    
    RKRequestDescriptor *feedbackRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory feedbackMapping] inverseMapping] objectClass:[Feedback class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:feedbackRequestDescriptor];
    
    RKRequestDescriptor *rideRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory rideRequestMapping] inverseMapping] objectClass:[RideRequest class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:rideRequestDescriptor];
    
    RKRequestDescriptor *locationRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory locationMapping] inverseMapping] objectClass:[Location class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:locationRequestDescriptor];
    
    RKRequestDescriptor *cardRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory cardMapping] inverseMapping] objectClass:[StripeCard class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:cardRequestDescriptor];
    
    RKRequestDescriptor *redeemRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory redeemRequestMapping] inverseMapping] objectClass:[RedeemRequest class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:redeemRequestDescriptor];
    
    RKRequestDescriptor *ddataRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory driverDataMapping] inverseMapping] objectClass:[DriverData class] rootKeyPath:nil method:RKRequestMethodPOST];
    [self addRequestDescriptor:ddataRequestDescriptor];
    
    RKRequestDescriptor *ddataPutRequestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:[[_factory driverDataMapping] inverseMapping] objectClass:[DriverData class] rootKeyPath:nil method:RKRequestMethodPUT];
    [self addRequestDescriptor:ddataPutRequestDescriptor];
    
}

- (void) setupResponseDescriptors
{
    RKResponseDescriptor *historyResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory compatRequestMapping] method:RKRequestMethodGET pathPattern:@"/zego/v1/riderequest/history/:uid/:mode/0/200" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:historyResponseDescriptor];
    
    RKResponseDescriptor *loginResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory userMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/user/login" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:loginResponseDescriptor];
    
    RKResponseDescriptor *signupResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory userMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/user/signup" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:signupResponseDescriptor];
    
    RKResponseDescriptor *silentResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory userMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/user/silent" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:silentResponseDescriptor];
    
    RKResponseDescriptor *completeResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory userMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/user/complete" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:completeResponseDescriptor];
    
    RKResponseDescriptor *killResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory userMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/user/kill" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:killResponseDescriptor];
    
    RKResponseDescriptor *updateResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory userMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/user" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:updateResponseDescriptor];
    
    RKResponseDescriptor *paydebtResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory userMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/user/paydebt" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:paydebtResponseDescriptor];
    
    RKResponseDescriptor *updatePutResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory userMapping] method:RKRequestMethodPUT pathPattern:@"/zego/v1/user" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:updatePutResponseDescriptor];
    
    RKResponseDescriptor *getResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory userMapping] method:RKRequestMethodGET pathPattern:@"/zego/v1/user/:uid" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:getResponseDescriptor];
    
    RKResponseDescriptor *referralResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory userMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/user/referral" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:referralResponseDescriptor];
    
    RKResponseDescriptor *validateResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory userMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/user/validate" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:validateResponseDescriptor];
    
    RKResponseDescriptor *stripeResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory userMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/user/stripe" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:stripeResponseDescriptor];
    
    RKResponseDescriptor *resendResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory userMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/user/resend" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:resendResponseDescriptor];
    
    RKResponseDescriptor *globalConfResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory confMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/conf/global" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:globalConfResponseDescriptor];
    
    RKResponseDescriptor *saveAddressResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory addressMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/address" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:saveAddressResponseDescriptor];
    
    RKResponseDescriptor *getUserAddressResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory addressMapping] method:RKRequestMethodGET pathPattern:@"/zego/v1/address/filter/uid/:uid/range/0/100" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:getUserAddressResponseDescriptor];
    
    RKResponseDescriptor *geocompleteResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory autocompleteResponseMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/google/geocomplete" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:geocompleteResponseDescriptor];
    
    RKResponseDescriptor *geocodeResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory geoCodeResponseMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/google/geocode" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:geocodeResponseDescriptor];
    
    RKResponseDescriptor *etaResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory etaResponseMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/riderequest/eta" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:etaResponseDescriptor];
    
    RKResponseDescriptor *priceResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory priceResponseMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/riderequest/price" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:priceResponseDescriptor];
    
    RKResponseDescriptor *actionsResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory rideRequestMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/riderequest/action" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:actionsResponseDescriptor];
    
    RKResponseDescriptor *requestDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory rideRequestMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/riderequest" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:requestDescriptor];
    
    RKResponseDescriptor *requestPriceUpdateDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory rideRequestMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/riderequest/priceupdate" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:requestPriceUpdateDescriptor];
    
    RKResponseDescriptor *forceRequestDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory rideRequestMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/riderequest/force" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:forceRequestDescriptor];
    
    RKResponseDescriptor *requestGetDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory rideRequestMapping] method:RKRequestMethodGET pathPattern:@"/zego/v1/riderequest/:rid" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:requestGetDescriptor];
    
    RKResponseDescriptor *feedbackResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory feedbackMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/feedback" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:feedbackResponseDescriptor];
    
    RKResponseDescriptor *locationResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory locationMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/location" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:locationResponseDescriptor];
    
    
    RKResponseDescriptor *riderequestResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory rideRequestMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/riderequest" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:riderequestResponseDescriptor];
    
    RKResponseDescriptor *usercardResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory cardMapping] method:RKRequestMethodGET pathPattern:@"/zego/v1/user/cards/:uid" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:usercardResponseDescriptor];
    
    RKResponseDescriptor *lastAddressResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory addressMapping] method:RKRequestMethodGET pathPattern:@"/zego/v1/address/:uid/last/:type" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:lastAddressResponseDescriptor];
    
    RKResponseDescriptor *defaultCardResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory cardMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/user/:uid/card/makedefault" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:defaultCardResponseDescriptor];

    RKResponseDescriptor *deleteCardResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory cardMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/user/:uid/card/delete" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:deleteCardResponseDescriptor];
    
    RKResponseDescriptor *redeemPromoResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory userPromoMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/promo/redeem" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:redeemPromoResponseDescriptor];
    
    RKResponseDescriptor *userPromoResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory userPromoMapping] method:RKRequestMethodGET pathPattern:@"/zego/v1/userpromo/uid/:uid" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:userPromoResponseDescriptor];
    
    RKResponseDescriptor *changeModeResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory userMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/user/changemodeto/:mode" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:changeModeResponseDescriptor];
    
    RKResponseDescriptor *areaResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory areaMapping] method:RKRequestMethodGET pathPattern:@"/zego/v1/area" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:areaResponseDescriptor];
    
    RKResponseDescriptor *getDriverDataResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory driverDataMapping] method:RKRequestMethodGET pathPattern:@"/zego/v1/driverdata/user/:uid" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:getDriverDataResponseDescriptor];
    
    RKResponseDescriptor *createDriverDataResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory driverDataMapping] method:RKRequestMethodPOST pathPattern:@"/zego/v1/driverdata" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:createDriverDataResponseDescriptor];
    
    RKResponseDescriptor *editDriverDataResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory driverDataMapping] method:RKRequestMethodPUT pathPattern:@"/zego/v1/driverdata" keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [self addResponseDescriptor:editDriverDataResponseDescriptor];
    
    RKResponseDescriptor *errorResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory errorResponseMapping] method:RKRequestMethodAny pathPattern:nil keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassServerError)];
    [self addResponseDescriptor:errorResponseDescriptor];
    
    RKResponseDescriptor *errorResponse401Descriptor = [RKResponseDescriptor responseDescriptorWithMapping:[_factory errorResponseMapping] method:RKRequestMethodAny pathPattern:nil keyPath:nil statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassClientError)];
    [self addResponseDescriptor:errorResponse401Descriptor];
}

- (NSMutableURLRequest *)requestWithObject:(id)object
                                    method:(RKRequestMethod)method
                                      path:(NSString *)path
                                parameters:(NSDictionary *)parameters {
   NSMutableURLRequest * req =  [super requestWithObject:object method:method path:path parameters:parameters];
   [req setTimeoutInterval:10.0];
    return req;
}

@end
