//
//  PromoListViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 17/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"

@interface PromoListViewController : ZegoViewController<UITableViewDelegate,UITableViewDataSource>
{
    NSMutableArray* promos;
}
@property (strong, nonatomic) IBOutlet UILabel *promoDisclaimer;

@property (strong, nonatomic) IBOutlet UITableView* table;
@property (strong, nonatomic) IBOutlet UIButton* addPromo;
@property (strong, nonatomic) IBOutlet UIView *backView;
@property (strong, nonatomic) IBOutlet UILabel *topTitle;
@property (strong, nonatomic) IBOutlet UILabel *placeHolder;

-(IBAction)addPromo:(id)sender;
@end
