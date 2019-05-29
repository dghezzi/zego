//
//  EtaResponse.h
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface EtaResponse : NSObject
{
    
}

@property(nonatomic,assign) int eta;
@property(nonatomic,assign) int dist;
@property(nonatomic,strong) NSString* address;
@property(nonatomic,strong) NSArray* drivers;
@property(nonatomic,strong) NSArray* services;

@end
