//
//  CreditCardViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 11/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"
#import "CenterMapViewController.h"
#import "CreditCardViewController.h"

@interface CreditCardViewController : ZegoViewController<UITableViewDelegate,UITableViewDataSource>
{
    NSMutableArray* cards;
}

@property(nonatomic,strong) NSString* method;
@property(nonatomic,strong) CenterMapViewController* map;
@property (strong, nonatomic) IBOutlet UITableView* table;
@property (strong, nonatomic) IBOutlet UIButton* addCard;
@property (strong, nonatomic) IBOutlet UIView *backView;
@property (strong, nonatomic) IBOutlet UILabel *topTitle;
@property (strong, nonatomic) IBOutlet UILabel *placeHolder;
@property (strong, nonatomic) IBOutlet UILabel *cashLabel;

@property (strong, nonatomic) IBOutlet UIButton *payDebtNow;
@property (strong, nonatomic) IBOutlet UILabel *payDebtLabel1;
@property (strong, nonatomic) IBOutlet UILabel *payDebtLabel2;
@property (strong, nonatomic) IBOutlet UIView *cashView;
@property (strong, nonatomic) IBOutlet UIImageView *cashActive;

-(IBAction)addCard:(id)sender;
-(IBAction)payDebt:(id)sender;
@end
