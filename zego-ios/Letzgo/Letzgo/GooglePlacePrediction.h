//
//  GooglePlacePrediction.h
//  Letzgo
//
//  Created by Luca Adamo on 22/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GooglePlacePrediction : NSObject
{
    
}

@property(nonatomic,strong) NSString* descr;
@property(nonatomic,strong) NSString* place_id;
@property(nonatomic,strong) NSString* reference;
@property(nonatomic,strong) NSMutableArray* types;

@end
