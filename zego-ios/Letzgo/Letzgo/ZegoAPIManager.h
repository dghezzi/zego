//
//  ZegoAPIManager.h
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <RestKit/RestKit.h>
#import "ZegoMappingFactory.h"
#import "NetworkMonitor.h"

#define SYSTEM_VERSION_GRATERTHAN_OR_EQUALTO(v)  ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] != NSOrderedAscending)
#define TEST 0

@interface ZegoAPIManager : RKObjectManager
{
    
}

@property(nonatomic,strong) ZegoMappingFactory* factory;

+ (id) sharedManager;
- (void) setupWithMappingFactory: (ZegoMappingFactory*)factory;
- (void) setAccessToken:(NSString*)accessToken;

- (void) globalConf:(void (^)(NSMutableArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(BootRequest *)br;
- (void) login:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(BootRequest *)br;
- (void) silentLogin:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(BootRequest *)br;
- (void) signup:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(BootRequest *)br;
- (void) complete:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(User *)u;
- (void) kill:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(User *)u;
- (void) update:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(User *)u;
- (void) paydebt:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(User *)u;
- (void) validatePin:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(PinRequest *)br;
- (void) resendPin:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(PinResendRequest *)br;
- (void) validateReferral:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(ReferralRequest *)br;
- (void) addStripeCard:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(StripeSaveRequest *)br;
- (void) getUser:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withId:(NSInteger )uid;
- (void) getRequest:(void (^)(RideRequest *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withId:(NSInteger )uid;
- (void) autocomplete:(void (^)(AutocompleteResponse *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(AutocompleteRequest*)req;
- (void) saveAddress:(void (^)(Address *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withAddress:(Address *)u;
- (void) getAddressesForUser:(void (^)(NSMutableArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withUid:(NSInteger)uid;
- (void) geocode:(void (^)(GeoCodeResponse *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(GeoCodeRequest *)u;
- (void) eta:(void (^)(EtaResponse *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(EtaRequest *)req;
- (void) price:(void (^)(PriceResponse *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(PriceRequest *)req;
- (void) createRequest:(void (^)(RideRequest *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(RideRequest *)req;
- (void) forceRequest:(void (^)(RideRequest *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(RideRequest *)req;
- (void) rideAction:(void (^)(RideRequest *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withAction:(RideRequestAction *)req;
- (void) feedback:(void (^)(Feedback *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withFeedback:(Feedback *)req;
- (void) userCards:(void (^)(NSArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure forUserWithId:(NSUInteger)uid;
- (void) lastAddress:(void (^)(NSArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure ofType:(NSString*)type forUserWithId:(NSUInteger)uid;
- (void) postLocation:(void (^)(Location *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure loc:(Location*)l;
- (void) makeCardDefault:(void (^)(NSArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure forUser:(NSInteger)uid withData:(StripeCard*)card;
- (void) deleteUserCard:(void (^)(NSArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure forUser:(NSInteger)uid withData:(StripeCard*)card;
- (void) userPromo:(void (^)(NSArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure forUserWithId:(NSUInteger)uid;
- (void) redeemPromo:(void (^)(NSArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withData:(RedeemRequest*)data;
- (void) userRides:(void (^)(NSArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withMode:(NSString*)mode forUserWithId:(NSUInteger)uid;
- (void) userChangeMode:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure to:(NSString*)mode withUserData:(User*)user;
- (void) getAllAreas:(void (^)(NSArray *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure;
- (void) updatePut:(void (^)(User *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(User *)ur;
- (void) getUserDriverData:(void (^)(DriverData *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure forUserId:(NSInteger)uid;
- (void) createDriverData:(void (^)(DriverData *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withData:(DriverData*)data;
- (void) updateRequestPrice:(void (^)(RideRequest *resp))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure withRequest:(RideRequest *)req;
@end
