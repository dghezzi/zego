//
//  StripeCard.h
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface StripeCard : NSObject {
    
}

@property(nonatomic,strong) NSString* card;
@property(nonatomic,strong) NSString* customer;
@property(nonatomic,strong) NSString* brand;
@property(nonatomic,strong) NSString* lastdigit;
@property(nonatomic,assign) int expmonth;
@property(nonatomic,assign) int expyear;
@property(nonatomic,assign) int preferred;

@end
