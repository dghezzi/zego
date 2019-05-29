//
//  RideListViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 17/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"

@interface RideListViewController : ZegoViewController<UITableViewDelegate,UITableViewDataSource>
{
    NSMutableArray* rides;
    UIView* greenLine;
    NSString* mode;
    CompatRidrequest* selectedRide;
}

@property (strong, nonatomic) IBOutlet UITableView* table;
@property (strong, nonatomic) IBOutlet UIButton* addPromo;
@property (strong, nonatomic) IBOutlet UIView *backView;
@property (strong, nonatomic) IBOutlet UILabel *topTitle;

@property (strong, nonatomic) IBOutlet UIView *selectorView;
@property (strong, nonatomic) IBOutlet UILabel *passengerLabel;
@property (strong, nonatomic) IBOutlet UILabel *driverLabel;
@property (strong, nonatomic) IBOutlet UILabel *placeHolder;
@end
