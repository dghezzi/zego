//
//  DriverData.h
//  Letzgo
//
//  Created by Luca Adamo on 22/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DriverData : NSObject
{
    
}

@property(nonatomic,assign) NSInteger did;
@property(nonatomic,assign) NSInteger uid;
@property(nonatomic,strong) NSString* brand;
@property(nonatomic,strong) NSString* model;
@property(nonatomic,strong) NSString* color;
@property(nonatomic,strong) NSString* plate;
@property(nonatomic,assign) NSInteger seat;
@property(nonatomic,assign) NSInteger year;
@property(nonatomic,strong) NSString* carimg;
@property(nonatomic,strong) NSString* insdate;
@property(nonatomic,strong) NSString* expdate;
@property(nonatomic,strong) NSString* moddate;
@property(nonatomic,strong) NSString* insuranceimg;
@property(nonatomic,strong) NSString* insuranceexpdate;
@property(nonatomic,strong) NSString* docexpdate;
@property(nonatomic,strong) NSString* docimg;
@property(nonatomic,strong) NSString* birthcountry;
@property(nonatomic,strong) NSString* birthcity;
@property(nonatomic,strong) NSString* cf;
@property(nonatomic,strong) NSString* address;
@property(nonatomic,strong) NSString* city;
@property(nonatomic,strong) NSString* cap;
@property(nonatomic,strong) NSString* iban;
@property(nonatomic,strong) NSString* area;
@property(nonatomic,assign) NSInteger status;
@property(nonatomic,assign) NSInteger docok;
@end
