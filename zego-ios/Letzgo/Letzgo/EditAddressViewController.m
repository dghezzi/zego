//
//  EditAddressViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 21/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "EditAddressViewController.h"

@interface EditAddressViewController ()

@end

@implementation EditAddressViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    _table.delegate = self;
    _table.dataSource = self;
    suggestions = [[NSMutableArray alloc] init];
    [self navigationController].navigationBarHidden = YES;
    [_backView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(goBack)]];
    _topTitle.font = [self boldFontWithSize:24];
    [self resetTopTitle];
    _next.titleLabel.font = [self boldFontWithSize:22];
    _field.delegate = self;
    _field.font = [self italicFontWithSize:(IS_IPHONE_5 || IS_IPHONE_4_OR_LESS)  ? 20 :26];
    [_field addTarget:self action:@selector(textFieldDidChange:)
     forControlEvents:UIControlEventEditingChanged];
   
        _field.placeholder = NSLocalizedString(@"Inserisci indirizzo",nil);
        _field.text = @"";
        _field.keyboardType = UIKeyboardTypeDefault;
    
    _next.enabled = lastPred != nil;
    _next.backgroundColor = !lastPred ? ZEGODARKGREY : ZEGOGREEN;
    
    if(_previous) {
        _field.text = _previous;
    }
   
    _recentype = _type;
    
    _backView.isAccessibilityElement = YES;
    _backView.accessibilityLabel = @"Indietro";
    _backView.accessibilityHint = @"Indietro";
    _backView.accessibilityTraits = UIAccessibilityTraitButton;
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self recentMode] ? [self countRecent] : [suggestions count];
}

-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:[self recentMode] ? @"recentCell" : @"addressCell"];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:[self recentMode] ? @"recentCell" : @"addressCell"];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    
    cell.backgroundColor = [UIColor clearColor];
    cell.contentView.backgroundColor = [UIColor clearColor];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    return cell;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if([self recentMode]) {
        return 44;
    }
    return IS_IPHONE_5 || IS_IPHONE_4_OR_LESS ? 45.0 : 60.0;
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath {
    if(cell) {
        
        if([self recentMode]) {
            
            UIImageView* leftimg = [(UIImageView*)cell.contentView viewWithTag:200];
            UILabel* lb = [(UILabel*)cell.contentView viewWithTag:201];
            UIImageView* pen = [(UIImageView*)cell.contentView viewWithTag:202];
            
            Address* add = nil;
            if(indexPath.row == 0) {
                add = [self recentWithType:@"home"];
                leftimg.image = [UIImage imageNamed:@"home_icon"];
                lb.text = add ? add.address : NSLocalizedString(@"Indirizzo casa",nil);
                lb.textColor = ZEGOGREEN;
            } else if(indexPath.row == 1) {
                add = [self recentWithType:@"work"];
                leftimg.image = [UIImage imageNamed:@"work_icon"];
                lb.text = add ? add.address : NSLocalizedString(@"Indirizzo lavoro",nil);
                lb.textColor = ZEGOGREEN;
            } else {
                add = [[self subRecentArray] objectAtIndex:(indexPath.row-(2))];
                leftimg.image = [UIImage imageNamed:@"graydot"];
                lb.text = add.address;
                lb.textColor = ZEGODARKGREY;
            }
            
            if(add) {
                NSArray* com = [add.address componentsSeparatedByString:@","];
                if(com && [com count] > 1) {
                    NSString* top = [com objectAtIndex:0];
                    top = [top stringByAppendingString:[NSString stringWithFormat:@", %@",[com objectAtIndex:1]]];
                    
                    NSString* bot = @"";
                    for(int i = 2;i<[com count];i++) {
                        bot = [bot stringByAppendingString:[com objectAtIndex:i]];
                        if(i<([com count]-1)) {
                            bot = [bot stringByAppendingString:@", "];
                        }
                    }
                    lb.text = top;
                }
            }
            
            
            pen.hidden = add;
            
            
        } else {
        GooglePlacePrediction* pred = [suggestions objectAtIndex:indexPath.row];
        UILabel* lb = [(UILabel*)cell.contentView viewWithTag:100];
        UILabel* slb = [(UILabel*)cell.contentView viewWithTag:101];

        NSArray* com = [pred.descr componentsSeparatedByString:@","];
        if(com && [com count] > 1) {
            NSString* top = [com objectAtIndex:0];
            NSString* bot = @"";
            for(int i = 1;i<[com count];i++) {
                bot = [bot stringByAppendingString:[com objectAtIndex:i]];
                if(i<([com count]-1)) {
                    bot = [bot stringByAppendingString:@", "];
                }
            }
            lb.text = top;
            slb.text = bot;
            slb.hidden = NO;
        } else {
            lb.text = pred.descr;
            slb.hidden = YES;
        }
        }
    }
    
}

-(BOOL) textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}


-(void) textFieldDidChange:(UITextField*)sender {

    AutocompleteRequest* ar = [[AutocompleteRequest alloc] init];
    ar.term = sender.text;
    [[ZegoAPIManager sharedManager] autocomplete:^(AutocompleteResponse *resp) {
        if(resp) {
            suggestions = resp.predictions;
            [_table reloadData];
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            [self handleError:error];
        }
    } withRequest:ar];
}


-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if([self recentMode]) {
        if(indexPath.row == 0) {
            Address* h = [self recentWithType:@"home"];
            if(!h) {
                _type = @"home";
                _topTitle.text = @"Indirizzo casa";
                [_table reloadData];
            } else {
                [self selectedAddress:h];
                //_field.text = h.address;
                //[self textFieldDidChange:_field];
            }
        } else if(indexPath.row == 1) {
            Address* w = [self recentWithType:@"work"];
            if(!w) {
                _type = @"work";
                _topTitle.text = @"Indirizzo lavoro";
                [_table reloadData];
            } else {
                [self selectedAddress:w];
                //_field.text = w.address;
                //[self textFieldDidChange:_field];
            }
        } else {
            Address* a = [[self subRecentArray] objectAtIndex:(indexPath.row-(2))];
            [self selectedAddress:a];
            //_field.text = a.address;
            //[self textFieldDidChange:_field];
        }
    } else {
    
    GooglePlacePrediction* pred = [suggestions objectAtIndex:indexPath.row];
    lastPred = pred;
    
    _field.text = pred.descr;
    
    _next.enabled = lastPred != nil;
    _next.backgroundColor = !lastPred ? ZEGODARKGREY : ZEGOGREEN;
    [_field resignFirstResponder];
    }
    
}

-(IBAction)saveAddress:(id)sender {
    
    GeoCodeRequest* greq = [[GeoCodeRequest alloc] init];
    greq.address = lastPred.description;
    greq.placeid = lastPred.place_id;
    [self startLoading];
    [[ZegoAPIManager sharedManager] geocode:^(GeoCodeResponse *resp) {
        if(resp) {
            Address* a = [[Address alloc] init];
            a.type = _type;
            a.uid = [self loggedUser].uid;
            a.lat = resp.lat;
            a.lng = resp.lng;
            a.address = resp.address;
            
            if([_type isEqualToString:@"dropoff"] || [_type isEqualToString:@"pickup"]) {
                [self selectedAddress:a];
            } else {
            [[ZegoAPIManager sharedManager] saveAddress:^(Address *resp) {
                if(_recent) {
                    _type = _recentype;
                    [_recent addObject:resp];
                    [suggestions removeAllObjects];
                    _field.text = nil;
                    [self resetTopTitle];
                    [_table reloadData];
                } else {                
                    [self goBack];
                }
            } failure:^(RKObjectRequestOperation *operation, NSError *error) {
                if(error) {
                    [self handleError:error];
                }
            } withAddress:a];
            }}
        [self stopLoading];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            [self handleError:error];
        }
        [self stopLoading];
    } withRequest:greq];
   
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(NSInteger) countRecent {
    int sp = 0;
    for(Address* a in _recent) {
        if([a.type isEqualToString:@"home"] || [a.type isEqualToString:@"work"]) {
            sp = sp + 1;
        }
        
    }
    return [_recent count] + (2-sp);
}

-(BOOL)recentMode {
    return _recent && (!suggestions || [suggestions count] == 0) && ([_type isEqualToString:@"pickup"] || [_type isEqualToString:@"dropoff"]);
}

-(Address*) recentWithType:(NSString*)t {
    for(Address* a in _recent) {
        if([a.type isEqualToString:t]) {
            return a;
        }
    }
    return nil;
}
-(NSMutableArray*)subRecentArray {
    NSMutableArray* subRecent = [[NSMutableArray alloc] init];
    for(Address* a in _recent) {
        if([a.type isEqualToString:@"home"] || [a.type isEqualToString:@"work"]) {
            
        } else {
            [subRecent addObject:a];
        }
        
    }
    return subRecent;
}
-(NSInteger)specialCount {
    int sp = 0;
    for(Address* a in _recent) {
        if([a.type isEqualToString:@"home"] || [a.type isEqualToString:@"work"]) {
            sp = sp + 1;
        }
        
    }
    return sp;
}

-(void)selectedAddress:(Address*)add {
    [_map addressSelected:add forType:_recentype];
    [self goBack];
}

-(void)resetTopTitle {
    if([_type isEqualToString:@"home"]) {
        _topTitle.text = @"Indirizzo casa";
    } else if([_type isEqualToString:@"work"]) {
        _topTitle.text = @"Indirizzo lavoro";
    } else if([_type isEqualToString:@"pickup"]) {
        _topTitle.text = @"Indirizzo partenza";
    } else if([_type isEqualToString:@"dropoff"]) {
        _topTitle.text = @"Indirizzo destinazione";
    }
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
