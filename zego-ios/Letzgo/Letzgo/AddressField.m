
//
//  AddressField.m
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "AddressField.h"
#define DISABLED_GREY [UIColor colorWithWhite:0.5 alpha:1]
@implementation AddressField
@synthesize im, atype, lab;

-(void)updateWithAddress:(Address*)addr {
    add = addr;
    [self checkAddr];
}

-(void)baseInit {
    
    CGFloat w = self.frame.size.width;
    CGFloat h = self.frame.size.height;
    
    self.backgroundColor = [UIColor whiteColor];

    
    // add pallino
    UIImageView* dot = [[UIImageView alloc] initWithFrame:CGRectMake(12, (h-12)/2, 12, 12)];
    dot.contentMode = UIViewContentModeScaleAspectFit;
    dot.image = [UIImage imageNamed:[atype isEqualToString:@"pickup"] ? @"puntogrigio" : @"puntoverde"];
    [self addSubview:dot];
    
    // add label
    CGFloat leftMargin = 36;
    // handle exception, eta state, balloon on the left
    if([atype isEqualToString:@"pickup"] && !add && editable) {
        leftMargin = 70;
    }
    lab = [[UILabel alloc] initWithFrame:CGRectMake(leftMargin, 0, w-(h+leftMargin), h)];
    lab.adjustsFontSizeToFitWidth = YES;
    lab.font = [self regularFontWithSize:16];
    lab.minimumScaleFactor = 0.5f;
    lab.userInteractionEnabled = YES;
    [lab addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(labelTapped:)]];
    [self addSubview:lab];
    
    
    // add right cta
    right = [[UIView alloc] initWithFrame:CGRectMake(w-h, 0, h, h)];
    im = [[UIImageView alloc] initWithFrame:CGRectMake((h-30)/2, (h-30)/2, 30, 30)];
    [right addSubview:im];
    [self addSubview:right];
    [self checkAddr];
    
    [right addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(ctaTapped:)]];
    
}

-(void)checkAddr {
    if(!add || !add.address) {
        lab.textColor = DISABLED_GREY;
        lab.text = [atype isEqualToString:@"pickup"] ? NSLocalizedString(@"Inserisci partenza",nil) : NSLocalizedString(@"Inserisci destinazione",nil);
        lab.hidden = NO;
    } else {
        lab.textColor = [UIColor blackColor];
        lab.text = add.address;
    }
    
    if(editable) {
        if(nav){
            im.image = [UIImage imageNamed:@"nav.png"];
            im.isAccessibilityElement = YES;
            im.accessibilityLabel = @"Avvia navigatore";
            im.accessibilityHint = @"Avvia navigatore";
            im.accessibilityTraits = UIAccessibilityTraitButton;
        } else if(add) {
            im.image = [UIImage imageNamed:@"cancel_x"];
            im.isAccessibilityElement = YES;
            im.accessibilityLabel = @"Cancella indirizzo";
            im.accessibilityHint = @"Cancella indirizzo";
            im.accessibilityTraits = UIAccessibilityTraitButton;
        } else {
            im.image = [UIImage imageNamed:@"search"];
        }
    } else {
        if(nav){
            im.image = [UIImage imageNamed:@"nav.png"];
            im.isAccessibilityElement = YES;
            im.accessibilityLabel = @"Avvia navigatore";
            im.accessibilityHint = @"Avvia navigatore";
            im.accessibilityTraits = UIAccessibilityTraitButton;
        } else {
            im.hidden = YES;
        }
    }
}

-(void) labelTapped:(id)sender {
    [self.delegate editSelectedForAddressField:self];
}

-(void) ctaTapped:(id)sender {
    if(editable) {
        if(add) {
            add = nil;
            [self.delegate cancelSelectedForAddressField:self];
            [self checkAddr];
        }
    }
    
    if(nav) {
        [self.delegate navSelectedForAddressField:self];
    }
}

-(id)initWithFrame:(CGRect)frame type:(NSString *)type address:(Address *)addr nav:(BOOL)navEn editable:(BOOL)edit
{
    self = [super initWithFrame:frame];
    if(self) {
        add = addr;
        atype = type;
        editable = edit;
        nav = navEn;
        [self baseInit];
    }
    return self;
}

-(UIFont*)lightFontWithSize:(CGFloat)sz {
    return [UIFont fontWithName:@"Raleway-Light" size:sz];
}

-(UIFont*)boldFontWithSize:(CGFloat)sz {
    return [UIFont fontWithName:@"Raleway-SemiBold" size:sz];
}

-(UIFont*)regularFontWithSize:(CGFloat)sz {
    return [UIFont fontWithName:@"Raleway-Regular" size:sz];
}

@end
