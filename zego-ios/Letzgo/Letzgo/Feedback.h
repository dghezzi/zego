//
//  Feedback.h
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Feedback : NSObject
{
    
}

@property(nonatomic,assign) int rid;
@property(nonatomic,assign) int uid;
@property(nonatomic,assign) int sender;
@property(nonatomic,strong) NSString* insdate;
@property(nonatomic,assign) int rating;
@property(nonatomic,strong) NSString* reason;

@end
