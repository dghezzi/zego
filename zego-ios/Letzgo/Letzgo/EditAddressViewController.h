//
//  EditAddressViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 21/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"
#import "CenterMapViewController.h"
@interface EditAddressViewController : ZegoViewController<UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate>
{
    NSMutableArray* suggestions;
    GooglePlacePrediction* lastPred;
}
@property (strong, nonatomic) CenterMapViewController* map;
@property (strong, nonatomic) NSMutableArray* recent;
@property (strong, nonatomic) NSString* type;
@property (strong, nonatomic) NSString* recentype;
@property (strong, nonatomic) NSString* previous;
@property (strong, nonatomic) IBOutlet UITextField* field;
@property (strong, nonatomic) IBOutlet UIView *backView;
@property (strong, nonatomic) IBOutlet UILabel *topTitle;
@property (strong, nonatomic) IBOutlet UIButton *next;
@property (strong, nonatomic) IBOutlet UITableView* table;

-(IBAction)saveAddress:(id)sender;
@end
