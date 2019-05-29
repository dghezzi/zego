//
//  ErrorResponse.h
//  Letzgo
//
//  Created by Luca Adamo on 15/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ErrorResponse : NSObject
{
    
}

@property(nonatomic,strong) NSString* title;
@property(nonatomic,strong) NSString* msg;
@property(nonatomic,assign) NSInteger code;
@end
