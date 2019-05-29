//
//  CompactDriver.h
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CompactDriver : NSObject
{
    
}

@property(nonatomic,assign) int current;
@property(nonatomic,assign) int rtstatus;
@property(nonatomic,assign) int did;
@property(nonatomic,assign) double lat;
@property(nonatomic,assign) double lng;
@property(nonatomic,strong) NSString* name;
@property(nonatomic,strong) NSString* brand;
@property(nonatomic,strong) NSString* model;
@property(nonatomic,assign) int year;
@property(nonatomic,strong) NSString* color;
@property(nonatomic,strong) NSString* carimg;
@property(nonatomic,strong) NSString* userimg;
@property(nonatomic,assign) double rating;
@end
