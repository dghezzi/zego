//
//  ThreeElementRideView.m
//  Letzgo
//
//  Created by Luca Adamo on 19/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ThreeElementRideView.h"

@implementation ThreeElementRideView


-(id)initWithFrame:(CGRect)frame request:(RideRequest*)req {
    self = [super initWithFrame:frame];
    
    UIFont *font1 = [self regularFontWithSize:22];
    NSDictionary *dict1 = [NSDictionary dictionaryWithObject: font1 forKey:NSFontAttributeName];
    
    UIFont *font2 = [self lightFontWithSize:16];
    NSDictionary *dict2 = [NSDictionary dictionaryWithObject: font2 forKey:NSFontAttributeName];
    
    
    CGFloat w3 = w/3.f;
    self.backgroundColor = [UIColor whiteColor];
    
    UIView* b1 = [[UIView alloc] initWithFrame:CGRectMake(0, 0, w3, h)];
    UILabel* lb1 = [[UILabel alloc] initWithFrame:CGRectMake(0, h-20, w3, 20)];
    lb1.textAlignment = NSTextAlignmentCenter;
    lb1.textColor = ZEGODARKGREY;
    lb1.font = [self lightFontWithSize:10];
    lb1.text = NSLocalizedString(@"Distanza ride",nil);
    [b1 addSubview:lb1];
    
    // content 3
    UILabel* tp1 = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, w3, 30)];
    
    NSString* dist = [NSString stringWithFormat:@"%.1f",(req.extmeters/1000.f)];
    NSMutableAttributedString *tp1a = [[NSMutableAttributedString alloc] initWithString:dist attributes: dict1];
    NSMutableAttributedString *tp1a2 = [[NSMutableAttributedString alloc] initWithString:@" km" attributes:dict2];
    [tp1a appendAttributedString:tp1a2];
    tp1.attributedText = tp1a;
    tp1.textColor = ZEGOGREEN;
    tp1.textAlignment = NSTextAlignmentCenter;
    //
    
    
    
    UIView* b2 = [[UIView alloc] initWithFrame:CGRectMake(w3, 0, w3, h)];
    UILabel* lb2 = [[UILabel alloc] initWithFrame:CGRectMake(0, h-20, w3, 20)];
    lb2.textAlignment = NSTextAlignmentCenter;
    lb2.textColor = ZEGODARKGREY;
    lb2.font = [self lightFontWithSize:10];
    lb2.text = NSLocalizedString(@"Durata stimata",nil);
    [b2 addSubview:lb2];

    // content 3
    UILabel* tp2 = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, w3, 30)];
    
    NSString* min = [NSString stringWithFormat:@"%d",(req.drivereta/60)+1];
    NSMutableAttributedString *tp2a = [[NSMutableAttributedString alloc] initWithString:min attributes: dict1];
    NSMutableAttributedString *tp2a2 = [[NSMutableAttributedString alloc] initWithString:@" min" attributes:dict2];
    [tp2a appendAttributedString:tp2a2];
    tp2.attributedText = tp2a;
    tp2.textColor = ZEGOGREEN;
    tp2.textAlignment = NSTextAlignmentCenter;
    //
    
    
    UIView* b3 = [[UIView alloc] initWithFrame:CGRectMake(2*w3, 0, w3, h)];
    UILabel* lb3 = [[UILabel alloc] initWithFrame:CGRectMake(0, h-20, w3, 20)];
    lb3.textAlignment = NSTextAlignmentCenter;
    lb3.textColor = ZEGODARKGREY;
    lb3.font = [self lightFontWithSize:10];
    lb3.text = NSLocalizedString(@"Prezzo selezionato",nil);
    [b3 addSubview:lb3];

    // content 3
    UILabel* tp3 = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, w3, 30)];

    NSString* integ = [NSString stringWithFormat:@"%d",((req.zegofee+req.driverfee-req.discount)/100)];
    NSInteger cent = (req.passprice%100);
    NSString* dec = [NSString stringWithFormat:cent < 10 ? @",0%d €" :@",%d €",((req.zegofee+req.driverfee)%100)];
    NSMutableAttributedString *aAttrString1 = [[NSMutableAttributedString alloc] initWithString:integ attributes: dict1];

    NSMutableAttributedString *aAttrString2 = [[NSMutableAttributedString alloc] initWithString:dec attributes:dict2];
    
    [aAttrString1 appendAttributedString:aAttrString2];
    tp3.attributedText = aAttrString1;
    tp3.textColor = ZEGOGREEN;
    tp3.textAlignment = NSTextAlignmentCenter;
    //
    
    UIView* l1 = [[UIView alloc] initWithFrame:CGRectMake(w3, 5, 0.5, h-10)];
    l1.backgroundColor = [UIColor lightGrayColor];
    UIView* l2 = [[UIView alloc] initWithFrame:CGRectMake(2*w3, 5, 0.5, h-10)];
    l2.backgroundColor = [UIColor lightGrayColor];
    
    [self addSubview:b1];
    [self addSubview:b2];
    [self addSubview:b3];
    [b3 addSubview:tp3];
    [b2 addSubview:tp2];
    [b1 addSubview:tp1];
    [self addSubview:l1];
    [self addSubview:l2];
    
    self.layer.borderColor = [UIColor lightGrayColor].CGColor;
    self.layer.borderWidth = 0.5;
    return self;
}
@end
