//
//  ZegoErrorResponse.h
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ZegoErrorResponse : NSObject
{
    
}
@property(nonatomic,strong) NSString* msg;
@property(nonatomic,strong) NSString* title;
@property(nonatomic,assign) NSInteger code;
@end
