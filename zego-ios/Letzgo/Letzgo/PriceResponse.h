//
//  PriceResponse.h
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface PriceResponse : NSObject
{
    
}

@property(nonatomic,strong) NSString* dropoff;
@property(nonatomic,assign) int price;
@property(nonatomic,assign) int seconds;
@property(nonatomic,assign) int meters;
@property(nonatomic,assign) int zegofee;
@property(nonatomic,assign) int driverfee;
@property(nonatomic,assign) int discount;
@property(nonatomic,assign) NSString* promocode;

@end
