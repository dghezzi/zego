//
//  Address.h
//  Letzgo
//
//  Created by Luca Adamo on 22/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Address : NSObject
{
    
}

@property(nonatomic,strong) NSString* address;
@property(nonatomic,strong) NSString* type;
@property(nonatomic,assign) NSInteger uid;
@property(nonatomic,assign) double lat;
@property(nonatomic,assign) double lng;

-(NSString*) addressForComponents:(NSInteger)com;

@end
