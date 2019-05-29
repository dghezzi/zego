//
//  TwoElementRideView.m
//  Letzgo
//
//  Created by Luca Adamo on 02/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "TwoElementRideView.h"

@implementation TwoElementRideView


-(id)initWithFrame:(CGRect)frame request:(RideRequest*)req {
    self = [super initWithFrame:frame];
    request = req;
    UIFont *font1 = [self regularFontWithSize:22];
    NSDictionary *dict1 = [NSDictionary dictionaryWithObject: font1 forKey:NSFontAttributeName];
    
    UIFont *font2 = [self lightFontWithSize:16];
    NSDictionary *dict2 = [NSDictionary dictionaryWithObject: font2 forKey:NSFontAttributeName];
    
    
    CGFloat w3 = w/3.f;
    self.backgroundColor = [UIColor whiteColor];
    
    UIView* b0 = [[UIView alloc] initWithFrame:CGRectMake(0, 0, w3, h)];
    UIImageView* method = [[UIImageView alloc] initWithFrame:CGRectMake(0, (h-30)/2, w3, 30)];
    method.contentMode = UIViewContentModeScaleAspectFit;
    method.image = [UIImage imageNamed:[req.method isEqualToString:@"cash" ] ?@"cashgreen" : @"cardgreen"];
    [b0 addSubview:method];
    
    UIView* b1 = [[UIView alloc] initWithFrame:CGRectMake(w3, 0, w3, h)];
    UILabel* lb1 = [[UILabel alloc] initWithFrame:CGRectMake(0, h-20, w3, 20)];
    lb1.textAlignment = NSTextAlignmentCenter;
    lb1.textColor = ZEGODARKGREY;
    lb1.font = [self lightFontWithSize:10];
    lb1.text = NSLocalizedString(@"Km passaggio",nil);
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
    
    
    
    
    UIView* b3 = [[UIView alloc] initWithFrame:CGRectMake(2*w3, 0, w3, h)];
    UILabel* lb3 = [[UILabel alloc] initWithFrame:CGRectMake(0, h-20, w3, 20)];
    lb3.textAlignment = NSTextAlignmentCenter;
    lb3.textColor = ZEGODARKGREY;
    lb3.font = [self lightFontWithSize:10];
    lb3.text = NSLocalizedString(@"Rimborso",nil);
    [b3 addSubview:lb3];
    
    // content 3
    tp3 = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, w3, 30)];
    tp3.textColor = ZEGOGREEN;
    tp3.textAlignment = NSTextAlignmentCenter;
    [self updatePriceTo:(req.driverfee+req.zegofee)];
    //
    
    UIView* l1 = [[UIView alloc] initWithFrame:CGRectMake(w3, 5, 0.5, h-10)];
    l1.backgroundColor = [UIColor lightGrayColor];
    
    UIView* l2 = [[UIView alloc] initWithFrame:CGRectMake(2*w3, 5, 0.5, h-10)];
    l2.backgroundColor = [UIColor lightGrayColor];
    
    
    [self addSubview:b1];
    [self addSubview:b0];
    [self addSubview:b3];
    [b3 addSubview:tp3];
    [b1 addSubview:tp1];
    [self addSubview:l1];
    [self addSubview:l2];

    b3.userInteractionEnabled = YES;
    [b3 addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(priceTapped:)]];
    self.layer.borderColor = [UIColor lightGrayColor].CGColor;
    self.layer.borderWidth = 0.5;
    return self;
}

-(void)priceTapped:(UIGestureRecognizer*)sender {
    [self.delegate twoElementRideViewPriceTapped:self];
}

-(void)updatePriceTo:(NSInteger)price {
    UIFont *font1 = [self regularFontWithSize:22];
    NSDictionary *dict1 = [NSDictionary dictionaryWithObject: font1 forKey:NSFontAttributeName];
    
    UIFont *font2 = [self lightFontWithSize:16];
    NSDictionary *dict2 = [NSDictionary dictionaryWithObject: font2 forKey:NSFontAttributeName];
    
    NSString* integ = [NSString stringWithFormat:@"%ld",(price/100)];
    NSInteger cent = (price%100);
    NSString* dec = [NSString stringWithFormat:cent < 10 ? @",0%ld €" :@",%ld €",(price%100)];
    NSMutableAttributedString *aAttrString1 = [[NSMutableAttributedString alloc] initWithString:integ attributes: dict1];
    
    NSMutableAttributedString *aAttrString2 = [[NSMutableAttributedString alloc] initWithString:dec attributes:dict2];
    
    [aAttrString1 appendAttributedString:aAttrString2];
    tp3.attributedText = aAttrString1;
}
@end
