//
//  EtaRequest.h
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface EtaRequest : NSObject
{
    
}

@property(nonatomic,assign) double lat;
@property(nonatomic,assign) double lng;
@property(nonatomic,strong) NSString* placeid;
@property(nonatomic,assign) int level;
@end
