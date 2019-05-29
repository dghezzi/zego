//
//  Promo.h
//  Letzgo
//
//  Created by Luca Adamo on 17/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Promo : NSObject
{
    
}

@property(nonatomic,strong) NSString* code;
@property(nonatomic,strong) NSString* promotitle;
@property(nonatomic,strong) NSString* promodesc;
@property(nonatomic,strong) NSString* enablestart;
@property(nonatomic,strong) NSString* enablestop;
@property(nonatomic,strong) NSString* validfrom;
@property(nonatomic,strong) NSString* validto;
@property(nonatomic,strong) NSString* type;
@property(nonatomic,assign) NSInteger feeonly;
@property(nonatomic,assign) NSInteger maxusages;
@property(nonatomic,assign) NSInteger currentusages;
@property(nonatomic,assign) NSInteger maxperuser;
@property(nonatomic,strong) NSString* insdate;
@property(nonatomic,strong) NSString* moddate;
@property(nonatomic,strong) NSString* note;
@property(nonatomic,assign) NSInteger value;

@end
