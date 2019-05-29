//
//  Location.h
//  Letzgo
//
//  Created by Luca Adamo on 29/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Location : NSObject
{
    
}

@property(nonatomic,assign) NSInteger uid;
@property(nonatomic,assign) double lat;
@property(nonatomic,assign) double lng;
@property(nonatomic,assign) double accuracy;
@property(nonatomic,strong) NSString* ldate;
@end
