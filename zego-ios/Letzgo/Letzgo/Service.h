//
//  Service.h
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Service : NSObject
{
    
}
@property(nonatomic,assign) int sid;
@property(nonatomic,strong) NSString* name;
@property(nonatomic,strong) NSString* nameen;
@property(nonatomic,assign) NSInteger level;
@property(nonatomic,strong) NSString* details;
@property(nonatomic,strong) NSString* detailsen;
@property(nonatomic,strong) NSString* insdate;
@end
