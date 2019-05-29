//
//  BecomeDriverViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 22/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"

@interface BecomeDriverViewController : ZegoViewController<UITableViewDelegate, UITableViewDataSource>
{
    NSMutableArray* areas;
}

@property (strong, nonatomic) DriverData* driverData;
@property (strong, nonatomic) IBOutlet UITableView* table;
@property (strong, nonatomic) IBOutlet UIButton* uploadDocument;
@property (strong, nonatomic) IBOutlet UIView *backView;
@property (strong, nonatomic) IBOutlet UILabel *topTitle;
@property (strong, nonatomic) IBOutlet UILabel *placeHolder;
@property (strong, nonatomic) IBOutlet UILabel *cta;

-(IBAction)goToWebArea:(id)sender;
@end
