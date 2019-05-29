//
//  RideListViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 17/01/17.
//  Copyright © 2017 Luca Adamo. All rights reserved.
//

#import "RideListViewController.h"
#import "SingleRideDetailViewController.h"

@interface RideListViewController ()

@end

@implementation RideListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [_backView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(goBack)]];
    _topTitle.font = [self boldFontWithSize:24];
    [[self navigationController] setNavigationBarHidden:YES];
    
    _table.dataSource = self;
    _table.delegate = self;
    _table.separatorStyle = UITableViewCellSeparatorStyleNone;
    _passengerLabel.textColor = ZEGOGREEN;
    _driverLabel.textColor = ZEGODARKGREY;
    mode = @"rider";
    _passengerLabel.userInteractionEnabled = YES;
    _driverLabel.userInteractionEnabled = YES;
    [_passengerLabel addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(changeMode:)]];
    [_driverLabel addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(changeMode:)]];
}

-(void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self updateRides];
}

-(void)viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    if(!greenLine) {
        CGFloat h = _selectorView.frame.size.height;
        greenLine = [[UIView alloc] initWithFrame:CGRectMake(0, h-3, self.view.frame.size.width/2,2)];
        [_selectorView addSubview:greenLine];
    }
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [rides count];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 100.f;
}

-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: @"rideCell"];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"rideCell"];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
    }
    
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    CompatRidrequest* req = [rides objectAtIndex:indexPath.row];
    selectedRide = req;
    [self performSegueWithIdentifier:@"rideDetailSegue" sender:nil];
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath {
    if(cell) {
        CompatRidrequest* req = [rides objectAtIndex:indexPath.row];
        UIView* box = [(UIView*)cell.contentView viewWithTag:200];
        box.layer.cornerRadius = 3;
        box.layer.shadowColor = [UIColor blackColor].CGColor;
        box.layer.shadowOffset = CGSizeMake(4, 4);
        box.layer.shadowOpacity = 0.9;
        box.layer.shadowRadius = 5;
        box.layer.masksToBounds = YES;
        UILabel* ridedate = [(UILabel*)cell.contentView viewWithTag:100];
        UILabel* rideshortid = [(UILabel*)cell.contentView viewWithTag:101];
        UILabel* pickup = [(UILabel*)cell.contentView viewWithTag:102];
        UILabel* dropoff = [(UILabel*)cell.contentView viewWithTag:103];
        UILabel* price = [(UILabel*)cell.contentView viewWithTag:104];
        UIImageView* method = [(UIImageView*)cell.contentView viewWithTag:150];
        UIImageView* level = [(UIImageView*)cell.contentView viewWithTag:151];
        method.image = [UIImage imageNamed:[req.method isEqualToString:@"cash"] ? @"cashgreen" : @"cardgreen"];
        if(req.lev && req.lev > 0) {
            level.hidden = NO;
        level.backgroundColor = req.lev == 2 ? [UIColor colorWithRed:202/255.f green:104/255.f blue:180/255.f alpha:1] : [UIColor colorWithRed:223/255.f green:112/255.f blue:48/255.f alpha:1];
        } else {
            level.hidden = YES;
        }
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setTimeZone:[NSTimeZone localTimeZone]];
        [dateFormatter setDateFormat:@"dd MMMM yyyy, HH:mm"];
        NSDate *epochNSDate = [[NSDate alloc] initWithTimeIntervalSince1970:[req.reqdate doubleValue]];
        ridedate.text = [NSString stringWithFormat:@"%@",[dateFormatter stringFromDate:epochNSDate]];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        rideshortid.text = req.shortid;
        pickup.text = req.pickup;
        dropoff.text = req.dropoff;
        
        
        NSInteger p = [mode isEqualToString:@"rider"] ? req.passprice : req.drivprice;
        
        
        UIFont *font1 = [self regularFontWithSize:22];
        NSString* integ = [NSString stringWithFormat:@"%ld",(p/100)];
        NSInteger cent = (p%100);
        NSString* dec = [NSString stringWithFormat:cent < 10 ? @",0%ld €" :@",%ld €",(p%100)];
        NSDictionary *dict1 = [NSDictionary dictionaryWithObject: font1 forKey:NSFontAttributeName];
        NSMutableAttributedString *aAttrString1 = [[NSMutableAttributedString alloc] initWithString:integ attributes: dict1];
        
        UIFont *font2 = [self lightFontWithSize:16];
        NSDictionary *dict2 = [NSDictionary dictionaryWithObject: font2 forKey:NSFontAttributeName];
        NSMutableAttributedString *aAttrString2 = [[NSMutableAttributedString alloc] initWithString:dec attributes:dict2];
        
        [aAttrString1 appendAttributedString:aAttrString2];
        price.attributedText = aAttrString1;
        price.textColor =  [mode isEqualToString:@"rider"] && req.status > 11 ? [UIColor redColor] :  ZEGOGREEN;
        price.textAlignment = NSTextAlignmentRight;
        
        _backView.isAccessibilityElement = YES;
        _backView.accessibilityLabel = @"Indietro";
        _backView.accessibilityHint = @"Indietro";
        
        
    }
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if([[segue identifier] isEqualToString:@"rideDetailSegue"]) {
        SingleRideDetailViewController* svc = [segue destinationViewController];
        svc.umode = mode;
        svc.request = selectedRide;
    }
}


-(void)changeMode:(UIGestureRecognizer*)sender {
    CGFloat fx = 0;
    if([sender.view isEqual:_passengerLabel]) {
        _passengerLabel.textColor = ZEGOGREEN;
        _driverLabel.textColor = ZEGODARKGREY;
        mode = @"rider";
    } else {
        _passengerLabel.textColor = ZEGODARKGREY;
        _driverLabel.textColor = ZEGOGREEN;
        mode = @"driver";
        fx = self.view.frame.size.width/2;
    }
    
    [UIView animateWithDuration:0.180
                     animations:^{
                         CGRect frame = greenLine.frame;
                         frame.origin.x = fx;
                        greenLine.frame = frame;
                     }
                     completion:^(BOOL finished){
                         // whatever you need to do when animations are complete
                     }];
    
    [self updateRides];
}

-(void)updateRides {
    [self startLoading];
    _placeHolder.hidden = YES;
    [[ZegoAPIManager sharedManager] userRides:^(NSArray *resp) {
        rides = [NSMutableArray arrayWithArray:resp];
        if([rides count] == 0) {
            [_table reloadData];
        } else {
            [_table reloadData];
        }
        _table.hidden = !resp || [resp count] == 0;
        _placeHolder.hidden = !_table.hidden;

        [self stopLoading];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        ErrorResponse* re = [self handleError:error];
        if(re) {
            [self.view makeToast:re.msg duration:3 position:CSToastPositionCenter];
        }
        [self stopLoading];
    } withMode:mode forUserWithId:[self loggedUser].uid];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
