//
//  GeoCodeResponse.h
//  Letzgo
//
//  Created by Luca Adamo on 22/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GeoCodeResponse : NSObject
{
    
}

@property(nonatomic,strong) NSString* address;
@property(nonatomic,assign) double lat;
@property(nonatomic,assign) double lng;
@property(nonatomic,assign) NSInteger partial;
@end
