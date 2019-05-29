//
//  PriceRequest.h
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface PriceRequest : NSObject
{
    
}

@property(nonatomic,assign) double pulat;
@property(nonatomic,assign) double pulng;
@property(nonatomic,assign) double dolat;
@property(nonatomic,assign) double dolng;
@property(nonatomic,strong) NSString* puplaceid;
@property(nonatomic,strong) NSString* doplaceid;
@property(nonatomic,assign) int level;

@end
