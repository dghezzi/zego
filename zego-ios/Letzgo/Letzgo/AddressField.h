//
//  AddressField.h
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ZegoViewController.h"

@class AddressField;

@protocol AddressFieldDelegate

-(void)cancelSelectedForAddressField:(AddressField*)field;
-(void)editSelectedForAddressField:(AddressField*)field;
-(void)navSelectedForAddressField:(AddressField*)field;

@end


@interface AddressField : UIView
{
    BOOL nav;
    BOOL editable;

    Address* add;
    UIView* right;
}

@property (assign) id <AddressFieldDelegate> delegate;
@property (nonatomic,strong) UIImageView* im;
@property (nonatomic,strong) NSString* atype;
@property (nonatomic,strong) UILabel* lab;

-(id)initWithFrame:(CGRect)frame type:(NSString*)type address:(Address*)addr nav:(BOOL)nav editable:(BOOL)edit;
-(void)updateWithAddress:(Address*)addr;

@end
