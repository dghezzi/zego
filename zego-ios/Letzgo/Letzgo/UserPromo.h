//
//  UserPromo.h
//  Letzgo
//
//  Created by Luca Adamo on 17/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Promo.h"
@interface UserPromo : NSObject
{
    
}

@property(nonatomic,assign) NSInteger pid;
@property(nonatomic,assign) NSInteger uid;
@property(nonatomic,strong) NSString* redeemdate;
@property(nonatomic,strong) NSString* usagedate;
@property(nonatomic,strong) NSString* expirydate;
@property(nonatomic,assign) NSInteger rideid;
@property(nonatomic,assign) NSInteger valueleft;
@property(nonatomic,strong) NSString* ridelist;
@property(nonatomic,strong) Promo* promo;
@end
