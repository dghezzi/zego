//
//  CompactRider.h
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CompactRider : NSObject
{
    
}

@property(nonatomic,assign) int current;
@property(nonatomic,assign) int did;
@property(nonatomic,assign) double lat;
@property(nonatomic,assign) double lng;
@property(nonatomic,strong) NSString* name;
@property(nonatomic,assign) int rtstatus;
@property(nonatomic,strong) NSString* userimg;
@property(nonatomic,assign) double rating;

@end
