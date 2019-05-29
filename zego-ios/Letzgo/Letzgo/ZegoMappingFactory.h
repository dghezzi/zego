//
//  ZegoMappingFactory.h
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <RestKit/RestKit.h>

#import "User.h"
#import "PinRequest.h"
#import "StripeSaveRequest.h"
#import "ReferralRequest.h"
#import "BootRequest.h"
#import "ErrorResponse.h"
#import "PinResendRequest.h"
#import "Conf.h"
#import "AutocompleteRequest.h"
#import "AutocompleteResponse.h"
#import "GooglePlacePrediction.h"
#import "Address.h"
#import "GeoCodeRequest.h"
#import "GeoCodeResponse.h"
#import "EtaRequest.h"
#import "EtaResponse.h"
#import "PriceRequest.h"
#import "PriceResponse.h"
#import "RideRequestAction.h"
#import "RideRequest.h"
#import "Feedback.h"
#import "Service.h"
#import "StripeCard.h"
#import "Location.h"
#import "UserPromo.h"
#import "Promo.h"
#import "RedeemRequest.h"
#import "CompatRidrequest.h"
#import "Area.h"
#import "DriverData.h"

@interface ZegoMappingFactory : RKObjectManager

-(RKObjectMapping*) userMapping;
-(RKObjectMapping*) pinRequestMapping;
-(RKObjectMapping*) pinResendMapping;
-(RKObjectMapping*) bootRequestMapping;
-(RKObjectMapping*) referralRequestMapping;
-(RKObjectMapping*) stripeSaveRequestMapping;
-(RKObjectMapping*) errorResponseMapping;
-(RKObjectMapping*) confMapping;
-(RKObjectMapping*) autocompleteRequestMapping;
-(RKObjectMapping*) autocompleteResponseMapping;
-(RKObjectMapping*) predictionsMapping;
-(RKObjectMapping*) addressMapping;
-(RKObjectMapping*) geoCodeRequestMapping;
-(RKObjectMapping*) geoCodeResponseMapping;
-(RKObjectMapping*) etaRequestMapping;
-(RKObjectMapping*) etaResponseMapping;
-(RKObjectMapping*) priceRequestMapping;
-(RKObjectMapping*) priceResponseMapping;
-(RKObjectMapping*) rideRequestMapping;
-(RKObjectMapping*) actionMapping;
-(RKObjectMapping*) feedbackMapping;
-(RKObjectMapping*) cardMapping;
-(RKObjectMapping*) locationMapping;
-(RKObjectMapping*) userPromoMapping;
-(RKObjectMapping*) redeemRequestMapping;
-(RKObjectMapping*) compatRequestMapping;
-(RKObjectMapping*) driverDataMapping;
-(RKObjectMapping*) areaMapping;
@end
