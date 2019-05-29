//
//  AutocompleteRequest.h
//  Letzgo
//
//  Created by Luca Adamo on 22/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AutocompleteRequest : NSObject
{
    
}

@property(nonatomic,strong) NSString* term;
@property(nonatomic,assign) NSInteger radius;
@property(nonatomic,assign) double lat;
@property(nonatomic,assign) double lng;
@end
