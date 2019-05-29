//
//  ZegoMappingFactory.m
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoMappingFactory.h"

@implementation ZegoMappingFactory


-(RKObjectMapping*) userMapping {
    RKObjectMapping* userMapping = [RKObjectMapping mappingForClass:[User class]];
    
    [userMapping addAttributeMappingsFromDictionary:@{@"id" : @"uid",
                                                      @"utype" : @"utype",
                                                      @"umode" : @"umode",
                                                      @"candrive" : @"candrive",
                                                      @"cashdue" : @"cashdue",
                                                      @"cardonly" : @"cardonly",
                                                      @"current" : @"current",
                                                      @"fname" : @"fname",
                                                      @"lname" : @"lname",
                                                      @"email" : @"email",
                                                      @"wemail" : @"wemail",
                                                      @"country" : @"country",
                                                      @"prefix" : @"prefix",
                                                      @"mobile" : @"mobile",
                                                      @"insdate" : @"insdate",
                                                      @"moddate" : @"moddate",
                                                      @"lastseen" : @"lastseen",
                                                      @"img" : @"img",
                                                      @"fbid" : @"fbid",
                                                      @"payok" : @"payok",
                                                      @"mobok" : @"mobok",
                                                      @"tcok" : @"tcok",
                                                      @"refcode" : @"refcode",
                                                      @"refuid" : @"refuid",
                                                      @"device" : @"device",
                                                      @"vos" : @"vos",
                                                      @"os" : @"os",
                                                      @"pushid" : @"pushid",
                                                      @"vapp" : @"vapp",
                                                      @"gender" : @"gender",
                                                      @"birthdate" : @"birthdate",
                                                      @"deviceid" : @"deviceid",
                                                      @"llat" : @"llat",
                                                      @"pavg" : @"pavg",
                                                      @"davg" : @"davg",
                                                      @"llon" : @"llon",
                                                      @"llocdate" : @"llocdate",
                                                      @"status" : @"status",
                                                      @"rtstatus" : @"rtstatus",
                                                      @"bitmask" : @"bitmask",
                                                      @"banexpdate" : @"banexpdate",
                                                      @"banreason" : @"banreason",
                                                      @"stripeid" : @"stripeid",
                                                      @"debt" : @"debt",
                                                      @"cashused" : @"cashused",
                                                      @"zegotoken" : @"zegotoken"}];
    return userMapping;
}

-(RKObjectMapping*) pinRequestMapping {
    RKObjectMapping* pinRequestMapping = [RKObjectMapping mappingForClass:[PinRequest class]];
    
    [pinRequestMapping addAttributeMappingsFromDictionary:@{@"uid" : @"uid",
                                                                    @"pin" : @"pin"}];
    return pinRequestMapping;

}

-(RKObjectMapping*) bootRequestMapping {
    RKObjectMapping* bootRequestMapping = [RKObjectMapping mappingForClass:[BootRequest class]];
    
    [bootRequestMapping addAttributeMappingsFromDictionary:@{@"fbid" : @"fbid",
                                                             @"country" : @"country",
                                                             @"prefix" : @"prefix",
                                                             @"mobile" : @"mobile",
                                                             @"device" : @"device",
                                                             @"os" : @"os",
                                                             @"pushid" : @"pushid",
                                                             @"uid" : @"uid",
                                                             @"vos" : @"vos",
                                                             @"vapp" : @"vapp",
                                                             @"deviceId" : @"deviceId"}];
    return bootRequestMapping;
}

-(RKObjectMapping*) referralRequestMapping {
    RKObjectMapping* referralRequestMapping = [RKObjectMapping mappingForClass:[ReferralRequest class]];
    
    [referralRequestMapping addAttributeMappingsFromDictionary:@{@"uid" : @"uid",
                                                                 @"referral" : @"referral"}];
    return referralRequestMapping;
}

-(RKObjectMapping*) stripeSaveRequestMapping {
    RKObjectMapping* pinRequestMappingResponse = [RKObjectMapping mappingForClass:[StripeSaveRequest class]];
    
    [pinRequestMappingResponse addAttributeMappingsFromDictionary:@{@"uid" : @"uid",
                                                                    @"token" : @"token"}];
    return pinRequestMappingResponse;
}

-(RKObjectMapping*) errorResponseMapping {
    RKObjectMapping* errorResponseMapping = [RKObjectMapping mappingForClass:[ErrorResponse class]];
    
    [errorResponseMapping addAttributeMappingsFromDictionary:@{@"code" : @"code",
                                                                 @"title" : @"title",
                                                                 @"msg" : @"msg"}];
    return errorResponseMapping;
}

-(RKObjectMapping*) pinResendMapping
{
    RKObjectMapping* pinResendMapping = [RKObjectMapping mappingForClass:[PinResendRequest class]];
    
    [pinResendMapping addAttributeMappingsFromDictionary:@{@"uid" : @"uid"}];
    return pinResendMapping;

}

-(RKObjectMapping*) confMapping
{
    RKObjectMapping* confMapping = [RKObjectMapping mappingForClass:[Conf class]];
    
    [confMapping addAttributeMappingsFromDictionary:@{@"k" : @"k", @"val" : @"val", @"descr" : @"descr"}];
    return confMapping;
    
}

-(RKObjectMapping*) autocompleteRequestMapping {
    RKObjectMapping* autocompleteRequestMapping = [RKObjectMapping mappingForClass:[AutocompleteRequest class]];
    [autocompleteRequestMapping addAttributeMappingsFromDictionary:@{@"term" : @"term",@"radius" : @"radius", @"lat" : @"lat", @"lng" : @"lng"}];
    return autocompleteRequestMapping;
}

-(RKObjectMapping*) autocompleteResponseMapping  {
    RKObjectMapping* autocompleteResponseMapping = [RKObjectMapping mappingForClass:[AutocompleteResponse class]];
    
    RKRelationshipMapping* predictionMapping = [RKRelationshipMapping relationshipMappingFromKeyPath:@"predictions"
                                                                                    toKeyPath:@"predictions"
                                                                                  withMapping:[self predictionsMapping]];
    
    [autocompleteResponseMapping addPropertyMapping:predictionMapping];
    return autocompleteResponseMapping;
}

-(RKObjectMapping*) predictionsMapping  {
    RKObjectMapping* predictionsMapping = [RKObjectMapping mappingForClass:[GooglePlacePrediction class]];
    [predictionsMapping addAttributeMappingsFromDictionary:@{@"description" : @"descr", @"place_id" : @"place_id", @"types" : @"types", @"reference" : @"reference"}];
    return predictionsMapping;
}

-(RKObjectMapping*) addressMapping  {
    RKObjectMapping* addressMapping = [RKObjectMapping mappingForClass:[Address class]];
    [addressMapping addAttributeMappingsFromDictionary:@{@"address" : @"address", @"uid" : @"uid", @"type" : @"type", @"lat" : @"lat", @"lng" : @"lng"}];
    return addressMapping;
}

-(RKObjectMapping*) geoCodeRequestMapping {
    RKObjectMapping* geoCodeRequestMapping = [RKObjectMapping mappingForClass:[GeoCodeRequest class]];
    [geoCodeRequestMapping addAttributeMappingsFromDictionary:@{@"address" : @"address", @"placeid" : @"placeid"}];
    return geoCodeRequestMapping;
}

-(RKObjectMapping*) geoCodeResponseMapping {
    RKObjectMapping* geoCodeResponseMapping = [RKObjectMapping mappingForClass:[GeoCodeResponse class]];
    [geoCodeResponseMapping addAttributeMappingsFromDictionary:@{@"address" : @"address", @"partial" : @"partial", @"lat" : @"lat", @"lng" : @"lng"}];
    return geoCodeResponseMapping;
}

-(RKObjectMapping*) serviceMapping {
    RKObjectMapping* serviceMapping = [RKObjectMapping mappingForClass:[Service class]];
    [serviceMapping addAttributeMappingsFromDictionary:@{@"name" : @"name", @"details" : @"details",@"nameen" : @"nameen", @"detailsen" : @"detailsen", @"level" :@"level",@"insdate" : @"insdate", @"id" : @"sid"}];
    return serviceMapping;
}

-(RKObjectMapping*) compactDriverMapping {
    RKObjectMapping* compactDriverMapping = [RKObjectMapping mappingForClass:[CompactDriver class]];
    [compactDriverMapping addAttributeMappingsFromDictionary:@{@"current" : @"current",
                                                               @"rtstatus" : @"rtstatus",
                                                               @"did" : @"did",
                                                               @"lat" : @"lat",
                                                               @"lng" : @"lng",
                                                               @"name" : @"name",
                                                               @"brand" : @"brand",
                                                               @"model" : @"model",
                                                               @"year" : @"year",
                                                               @"color" : @"color",
                                                               @"carimg" : @"carimg",
                                                               @"userimg" : @"userimg",
                                                               @"rating" : @"rating"}];
    return compactDriverMapping;
}

-(RKObjectMapping*) comapactRiderMapping {
    RKObjectMapping* comapactRiderMapping = [RKObjectMapping mappingForClass:[CompactRider class]];
    [comapactRiderMapping addAttributeMappingsFromDictionary:@{@"current" : @"current",
                                                               @"rtstatus" : @"rtstatus",
                                                               @"did" : @"did",
                                                               @"lat" : @"lat",
                                                               @"lng" : @"lng",
                                                               @"name" : @"name",
                                                               @"userimg" : @"userimg",
                                                               @"rating" : @"rating"}];
    return comapactRiderMapping;
}

-(RKObjectMapping*) etaRequestMapping {
    RKObjectMapping* etaRequestMapping = [RKObjectMapping mappingForClass:[EtaRequest class]];
    [etaRequestMapping addAttributeMappingsFromDictionary:@{@"level" : @"level",
                                                            @"placeid" : @"placeid",
                                                            @"lat" : @"lat",
                                                            @"lng" : @"lng"}];
    return etaRequestMapping;
}

-(RKObjectMapping*) etaResponseMapping{
    RKObjectMapping* etaResponseMapping = [RKObjectMapping mappingForClass:[EtaResponse class]];
    [etaResponseMapping addAttributeMappingsFromDictionary:@{@"address" : @"address",
                                                             @"eta" : @"eta",
                                                             @"dist" : @"dist"}];
    
    RKRelationshipMapping* driversMapping = [RKRelationshipMapping relationshipMappingFromKeyPath:@"drivers"
                                                                                           toKeyPath:@"drivers"
                                                                                         withMapping:[self compactDriverMapping]];
    
    RKRelationshipMapping* serviceMapping = [RKRelationshipMapping relationshipMappingFromKeyPath:@"services"
                                                                                        toKeyPath:@"services"
                                                                                      withMapping:[self serviceMapping]];
    
    [etaResponseMapping addPropertyMapping:driversMapping];
    [etaResponseMapping addPropertyMapping:serviceMapping];
    
    return etaResponseMapping;
}
-(RKObjectMapping*) priceRequestMapping{
    RKObjectMapping* priceRequestMapping = [RKObjectMapping mappingForClass:[PriceRequest class]];
    [priceRequestMapping addAttributeMappingsFromDictionary:@{@"pulat" : @"pulat",
                                                              @"pulng" : @"pulng",
                                                              @"dolat" : @"dolat",
                                                              @"dolng" : @"dolng",
                                                              @"puplaceid" : @"puplaceid",
                                                              @"doplaceid" : @"doplaceid",
                                                              @"level" : @"level"}];
    return priceRequestMapping;
}
-(RKObjectMapping*) priceResponseMapping{
    RKObjectMapping* priceResponseMapping = [RKObjectMapping mappingForClass:[PriceResponse class]];
    [priceResponseMapping addAttributeMappingsFromDictionary:@{@"dropoff" : @"dropoff",
                                                               @"price" : @"price",
                                                               @"seconds" : @"seconds",
                                                               @"meters" : @"meters",
                                                               @"discount" : @"discount",
                                                               @"promocode" : @"promocode",
                                                               @"zegofee" : @"zegofee",
                                                               @"driverfee" : @"driverfee"}];
    return priceResponseMapping;
}
-(RKObjectMapping*) rideRequestMapping{
    RKObjectMapping* rideRequestMapping = [RKObjectMapping mappingForClass:[RideRequest class]];
    [rideRequestMapping addAttributeMappingsFromDictionary:@{@"id" : @"rid",
                                                             @"pid" : @"pid",
                                                             @"did" : @"did",
                                                             @"status" : @"status",
                                                             @"method" : @"method",
                                                             @"pulat" : @"pulat",
                                                             @"pulng" : @"pulng",
                                                             @"puaddr" : @"puaddr",
                                                             @"dolat" : @"dolat",
                                                             @"dolng" : @"dolng",
                                                             @"doaddr" : @"doaddr",
                                                             @"reqdate" : @"reqdate",
                                                             @"assigndate" : @"assigndate",
                                                             @"canceldate" : @"canceldate",
                                                             @"abortdate" : @"abortdate",
                                                             @"startdate" : @"startdate",
                                                             @"enddate" : @"enddate",
                                                             @"extmeters" : @"extmeters",
                                                             @"reqlevel" : @"reqlevel",
                                                             @"extsecond" : @"extsecond",
                                                             @"extshort" : @"extshort",
                                                             @"drivereta" : @"drivereta",
                                                             @"realpulat" : @"realpulat",
                                                             @"realpulng" : @"realpulng",
                                                             @"realpuaddr" : @"realpuaddr",
                                                             @"shortid" : @"shortid",
                                                             @"realdolat" : @"realdolat",
                                                             @"realdolng" : @"realdolng",
                                                             @"realdoaddr" : @"realdoaddr",
                                                             @"extprice" : @"extprice",
                                                             @"driverfee" : @"driverfee",
                                                             @"zegofee" : @"zegofee",
                                                             @"stripedriverfee" : @"stripedriverfee",
                                                             @"stripezegofee" : @"stripezegofee",
                                                             @"passprice" : @"passprice",
                                                             @"numpass" : @"numpass",
                                                             @"options" : @"options",
                                                             @"passrating" : @"passrating",
                                                             @"drivrating" : @"drivrating",
                                                             @"discount" : @"discount",
                                                             @"promoid" : @"promoid",
                                                             @"shid" : @"shid",
                                                             @"abortreason" : @"abortreason",
                                                             @"cancelreason" : @"cancelreason",
                                                             }];
    
    RKRelationshipMapping* driverMapping = [RKRelationshipMapping relationshipMappingFromKeyPath:@"driver"
                                                                                        toKeyPath:@"driver"
                                                                                      withMapping:[self compactDriverMapping]];
    
    RKRelationshipMapping* riderMapping = [RKRelationshipMapping relationshipMappingFromKeyPath:@"rider"
                                                                                        toKeyPath:@"rider"
                                                                                      withMapping:[self comapactRiderMapping]];
        [rideRequestMapping addPropertyMapping:driverMapping];
        [rideRequestMapping addPropertyMapping:riderMapping];
    return rideRequestMapping;
}
-(RKObjectMapping*) actionMapping{
    RKObjectMapping* actionMapping = [RKObjectMapping mappingForClass:[RideRequestAction class]];
    [actionMapping addAttributeMappingsFromDictionary:@{@"action" : @"action",
                                                        @"uid" : @"uid",
                                                        @"rid" : @"rid",
                                                        @"text" : @"text",
                                                        @"capture" : @"capture",
                                                        @"priceupdate" : @"priceupdate"}];
    return actionMapping;
}
-(RKObjectMapping*) feedbackMapping{
    RKObjectMapping* feedbackMapping = [RKObjectMapping mappingForClass:[Feedback class]];
    [feedbackMapping addAttributeMappingsFromDictionary:@{@"rid" : @"rid",
                                                          @"uid" : @"uid",
                                                          @"sender" : @"sender",
                                                          @"rating" : @"rating",
                                                          @"reason" : @"reason",
                                                          @"insdate" : @"insdate"}];
    return feedbackMapping;
}


-(RKObjectMapping*) cardMapping{
    RKObjectMapping* cardMapping = [RKObjectMapping mappingForClass:[StripeCard class]];
    [cardMapping addAttributeMappingsFromDictionary:@{@"card" : @"card",
                                                      @"customer" : @"customer",
                                                      @"brand" : @"brand",
                                                      @"lastdigit" : @"lastdigit",
                                                      @"expmonth" : @"expmonth",
                                                      @"expyear" : @"expyear",
                                                      @"preferred" : @"preferred"}];
    return cardMapping;
}


-(RKObjectMapping*) locationMapping {
    RKObjectMapping* locationMapping = [RKObjectMapping mappingForClass:[Location class]];
    [locationMapping addAttributeMappingsFromDictionary:@{@"uid" : @"uid",
                                                      @"lat" : @"lat",
                                                      @"lng" : @"lng",
                                                      @"accuracy" : @"accuracy",
                                                      @"ldate" : @"ldate"}];
    return locationMapping;

}

-(RKObjectMapping*) promoMapping {
    RKObjectMapping* promoMapping = [RKObjectMapping mappingForClass:[Promo class]];
    [promoMapping addAttributeMappingsFromDictionary:@{@"code":@"code",
                                                       @"promotitle":@"promotitle",
                                                       @"promodesc":@"promodesc",
                                                       @"enablestart":@"enablestart",
                                                       @"enablestop":@"enablestop",
                                                       @"validfrom":@"validfrom",
                                                       @"validto":@"validto",
                                                       @"type":@"type",
                                                       @"feeonly":@"feeonly",
                                                       @"maxusages":@"maxusages",
                                                       @"currentusages":@"currentusages",
                                                       @"maxperuser":@"maxperuser",
                                                       @"insdate":@"insdate",
                                                       @"moddate":@"moddate",
                                                       @"note":@"note",
                                                       @"value":@"value"}];
    return promoMapping;
}

-(RKObjectMapping*) userPromoMapping {
    RKObjectMapping* userPromoMapping = [RKObjectMapping mappingForClass:[UserPromo class]];
    [userPromoMapping addAttributeMappingsFromDictionary:@{@"pid":@"pid",
                                                           @"uid":@"uid",
                                                           @"redeemdate":@"redeemdate",
                                                           @"usagedate":@"usagedate",
                                                           @"expirydate":@"expirydate",
                                                           @"rideid":@"rideid",
                                                           @"valueleft":@"valueleft",
                                                           @"ridelist":@"ridelist"}];
    
    RKRelationshipMapping* promoMapping = [RKRelationshipMapping relationshipMappingFromKeyPath:@"promo"
                                                                                      toKeyPath:@"promo"
                                                                                    withMapping:[self promoMapping]];
    [userPromoMapping addPropertyMapping:promoMapping];

    return userPromoMapping;
}


-(RKObjectMapping*) redeemRequestMapping {
    RKObjectMapping* redeemRequestMapping = [RKObjectMapping mappingForClass:[RedeemRequest class]];
    [redeemRequestMapping addAttributeMappingsFromDictionary:@{@"code":@"code",
                                                           @"uid":@"uid"}];
        
    return redeemRequestMapping;
}

-(RKObjectMapping*) compatRequestMapping {
    RKObjectMapping* compatRequestMapping = [RKObjectMapping mappingForClass:[CompatRidrequest class]];
    [compatRequestMapping addAttributeMappingsFromDictionary:@{@"rid" : @"rid",
                                                          @"shortid" : @"shortid",
                                                          @"drivprice" : @"drivprice",
                                                          @"passprice" : @"passprice",
                                                               @"lev" : @"lev",
                                                               @"method" : @"method",
                                                               @"status" : @"status",
                                                               @"discount" : @"discount",
                                                          @"driverprice" : @"driverprice",
                                                          @"pickup" : @"pickup",
                                                          @"dropoff" : @"dropoff",
                                                          @"reqdate" : @"reqdate",
                                                          @"shid" : @"shid"}];
    return compatRequestMapping;
}

-(RKObjectMapping*) areaMapping {
    RKObjectMapping* areaMapping = [RKObjectMapping mappingForClass:[Area class]];
    [areaMapping addAttributeMappingsFromDictionary:@{@"name":@"name",                                                               @"id":@"aid"}];
    return areaMapping;
}

-(RKObjectMapping*) driverDataMapping {
    RKObjectMapping* driverDataMapping = [RKObjectMapping mappingForClass:[DriverData class]];
    [driverDataMapping addAttributeMappingsFromDictionary:@{@"id" : @"did",
                                                            @"uid" : @"uid",
                                                            @"brand" : @"brand",
                                                            @"model" : @"model",
                                                            @"color" : @"color",
                                                            @"plate" : @"plate",
                                                            @"seat" : @"seat",
                                                            @"year" : @"year",
                                                            @"carimg" : @"carimg",
                                                            @"insdate" : @"insdate",
                                                            @"expdate" : @"expdate",
                                                            @"moddate" : @"moddate",
                                                            @"insuranceimg" : @"insuranceimg",
                                                            @"insuranceexpdate" : @"insuranceexpdate",
                                                            @"docexpdate" : @"docexpdate",
                                                            @"docimg" : @"docimg",
                                                            @"birthcountry" : @"birthcountry",
                                                            @"birthcity" : @"birthcity",
                                                            @"cf" : @"cf",
                                                            @"address" : @"address",
                                                            @"city" : @"city",
                                                            @"cap" : @"cap",
                                                            @"iban" : @"iban",
                                                            @"area" : @"area",
                                                            @"status" : @"status",    
                                                            @"docok" : @"docok"}];
    return driverDataMapping;
    
}
@end
