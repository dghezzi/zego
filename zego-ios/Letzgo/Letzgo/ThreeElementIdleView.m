//
//  ThreeElementIdleView.m
//  Letzgo
//
//  Created by Luca Adamo on 18/12/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ThreeElementIdleView.h"
#import "ZegoViewController.h"

@implementation ThreeElementIdleView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

-(id)initWithFrame:(CGRect)frame request:(RideRequest*)req stripeCard:(StripeCard*)card {
    
    self = [super initWithFrame:frame];
    
    passNum = 0;
    method = @"card";
    CGFloat w3 = w/3.f;
    self.backgroundColor = [UIColor whiteColor];
    
    UIView* b1 = [[UIView alloc] initWithFrame:CGRectMake(0, 0, w3, h)];
    UILabel* lb1 = [[UILabel alloc] initWithFrame:CGRectMake(0, h-20, w3, 20)];
    lb1.textAlignment = NSTextAlignmentCenter;
    lb1.textColor = ZEGODARKGREY;
    lb1.font = [self lightFontWithSize:10];
    lb1.text = NSLocalizedString(@"Pagamento",nil);
    lb1.hidden = YES;
    [b1 addSubview:lb1];
    [b1 addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(cardTapped:)]];
    
    b1.isAccessibilityElement = YES;
    b1.accessibilityLabel = @"Modifica Metodo di Pagamento";
    b1.accessibilityHint = @"Modifica Metodo di Pagamento";
    b1.accessibilityTraits = UIAccessibilityTraitButton;
    
    tp1l = [[UIImageView alloc] initWithFrame:CGRectMake(0, 5, 42, 27)];
    tp1l.contentMode = UIViewContentModeScaleAspectFit;
    last4 = [[UILabel alloc] initWithFrame:CGRectMake(0, h-18, 42, 16)];//[[UILabel alloc] initWithFrame:CGRectMake(50, 8, w3-50, 30)];
    last4.font = [self regularFontWithSize:12];
    last4.textAlignment = NSTextAlignmentCenter;
    last4.adjustsFontSizeToFitWidth = YES;
    last4.minimumScaleFactor = 0.5;
    last4.textColor = ZEGODARKGREY;
    
    tp1cash = [[UIImageView alloc] initWithFrame:CGRectMake(50, 8, w3-50, 23)];
    tp1cash.contentMode = UIViewContentModeScaleAspectFit;
    tp1cash.image = [UIImage imageNamed:@"cashgrey"];
    UILabel* cash = [[UILabel alloc] initWithFrame:CGRectMake(50, h-18, w3-50, 16)];
    cash.font = [self regularFontWithSize:12];
    cash.textAlignment = NSTextAlignmentCenter;
    cash.adjustsFontSizeToFitWidth = YES;
    cash.minimumScaleFactor = 0.5;
    cash.textColor = ZEGODARKGREY;
    cash.text = @"Contanti";
    if(card) {
        NSString* name = [NSString stringWithFormat:@"%@",[card.brand stringByReplacingOccurrencesOfString:@" " withString:@"_"]];
        tp1l.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@",[name lowercaseString]]];
        last4.text = [NSString stringWithFormat:@"** %@",card.lastdigit];
        
    } else {
        tp1l.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@",@"credit_card"]];
        last4.text = [NSString stringWithFormat:@"-"];
    }
    
    
    UIView* b2 = [[UIView alloc] initWithFrame:CGRectMake(w3, 0, w3, h)];
    UILabel* lb2 = [[UILabel alloc] initWithFrame:CGRectMake(0, h-20, w3, 20)];
    lb2.textAlignment = NSTextAlignmentCenter;
    lb2.textColor = ZEGODARKGREY;
    lb2.font = [self lightFontWithSize:10];
    lb2.text = @"Opzioni";
    lb2.hidden = YES;
    [b2 addSubview:lb2];
    // content 2
    UIImageView* tp2 = [[UIImageView alloc] initWithFrame:CGRectMake(0, 10, w3, 30)];
    tp2.contentMode = UIViewContentModeScaleAspectFit;
    tp2.image = [UIImage imageNamed:[NSString stringWithFormat:@"p%ld",passNum]];
    tp2.userInteractionEnabled = YES;
    [tp2 addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(passNumTapped:)]];
    //
    
    UIView* b3 = [[UIView alloc] initWithFrame:CGRectMake(2*w3, 0, w3, h)];
    lb3 = [[UILabel alloc] initWithFrame:CGRectMake(0, h-16, w3, 16)];
    lb3.textAlignment = NSTextAlignmentCenter;
    lb3.font = [self lightFontWithSize:10];
    lb3.text = NSLocalizedString(@"Modifica",nil);
    [b3 addSubview:lb3];
    [b3 addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(priceTapped:)]];
    // content 3
    tp3 = [[UILabel alloc] initWithFrame:CGRectMake(0, 5, w3, 28)];
    [self updatePrice:req.passprice];
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
    [b1 addSubview:last4];
    [b1 addSubview:tp1l];
    [b1 addSubview:tp1cash];
    [b1 addSubview:cash];
    [self addSubview:l1];
    [self addSubview:l2];
    
    self.layer.borderColor = [UIColor lightGrayColor].CGColor;
    self.layer.borderWidth = 0.5;
    
    return self;
    
}

-(void)setToCash {
    method = @"cash";
    tp1cash.image = [UIImage imageNamed:@"cashgreen"];
    tp1l.image = [UIImage imageNamed:@"cardgrey"];
    last4.text = @"-";
}
-(void)updateCard:(StripeCard*)card {
    if(card) {
        method = @"card";
        tp1cash.image = [UIImage imageNamed:@"cashgrey"];
        NSString* name = [NSString stringWithFormat:@"%@",[card.brand stringByReplacingOccurrencesOfString:@" " withString:@"_"]];
        tp1l.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@",[name lowercaseString]]];
        last4.text = [NSString stringWithFormat:@"** %@",card.lastdigit];
    } else {
        tp1l.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@",@"credit_card"]];
        last4.text = [NSString stringWithFormat:@"-"];
        [self setToCash];
    }
}

-(void) priceTapped:(UIGestureRecognizer*)sender {
    [self.delegate elementViewPriceSelected:self];
}

-(void) cardTapped:(UIGestureRecognizer*)sender {
    [self.delegate elementViewCardSelected:self];
}

-(void) passNumTapped:(UIGestureRecognizer*)sender {
    passNum = (passNum+1)%4;
    ((UIImageView*)sender.view).image = [UIImage imageNamed:[NSString stringWithFormat:@"p%ld",passNum]];
    [self.delegate elementView:self passnumUpdatedTo:passNum];
}

-(void)updatePrice:(int)p {
    UIFont *font1 = [self regularFontWithSize:28];
    NSString* integ = [NSString stringWithFormat:@"%d",(p/100)];
    NSInteger cent = (p%100);
    NSString* dec = [NSString stringWithFormat:cent < 10 ? @",0%d €" :@",%d €",(p%100)];
    NSDictionary *dict1 = [NSDictionary dictionaryWithObject: font1 forKey:NSFontAttributeName];
    NSMutableAttributedString *aAttrString1 = [[NSMutableAttributedString alloc] initWithString:integ attributes: dict1];
    
    UIFont *font2 = [self lightFontWithSize:16];
    NSDictionary *dict2 = [NSDictionary dictionaryWithObject: font2 forKey:NSFontAttributeName];
    NSMutableAttributedString *aAttrString2 = [[NSMutableAttributedString alloc] initWithString:dec attributes:dict2];
    
    [aAttrString1 appendAttributedString:aAttrString2];
    tp3.attributedText = aAttrString1;
    tp3.textColor = ZEGOGREEN;
    tp3.textAlignment = NSTextAlignmentCenter;
    lb3.text = NSLocalizedString(@"Modifica",nil);
}
@end
