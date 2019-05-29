//
//  EditSimpleFieldViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 21/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "EditSimpleFieldViewController.h"

@interface EditSimpleFieldViewController ()

@end

@implementation EditSimpleFieldViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self navigationController].navigationBarHidden = YES;
    [_backView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(goBack)]];
    _topTitle.font = [self boldFontWithSize:24];
    _next.titleLabel.font = [self boldFontWithSize:22];
    _editField.delegate = self;
    _editField.font = [self italicFontWithSize:(IS_IPHONE_5 || IS_IPHONE_4_OR_LESS)  ? 20 :26];
    [_editField addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    User* u = [self loggedUser];
    
    if(_field == 0) {
        _editField.placeholder = NSLocalizedString(@"Inserisci Nome",nil);
        _editField.text = u.fname;
    } else if(_field == 1) {
        _editField.placeholder = NSLocalizedString(@"Inserisci Cognome",nil);
        _editField.text = u.lname;
    } else if(_field == 2) {
        _editField.placeholder = NSLocalizedString(@"Inserisci Email",nil);
        _editField.text = u.email;
        _editField.keyboardType = UIKeyboardTypeEmailAddress;
        _editField.autocapitalizationType = UITextAutocapitalizationTypeNone;
    } else if(_field == 3) {
        _editField.placeholder = NSLocalizedString(@"Inserisci Email lavoro",nil);
        _editField.text = u.wemail;
        _editField.keyboardType = UIKeyboardTypeEmailAddress;
        _editField.autocapitalizationType = UITextAutocapitalizationTypeNone;
    }
    _fieldMessage.hidden = YES;
    _fieldMessage.font = [self italicFontWithSize:14];
    _fieldMessage.minimumScaleFactor = 10/14.f;
    
    _backView.isAccessibilityElement = YES;
    _backView.accessibilityLabel = @"Indietro";
    _backView.accessibilityHint = @"Indietro";
    _backView.accessibilityTraits = UIAccessibilityTraitButton;
}


-(BOOL) textFieldShouldReturn:(UITextField *)textField{       
    [textField resignFirstResponder];
    return YES;
}


-(void) textFieldDidChange:(UITextField*)sender {
    BOOL enable = YES;
    
    enable = enable && [_editField.text length] > 0;
   
    
    if(_field == 2 || _field == 3 ) {
        if(![self validateEmail:_editField.text]) {
            _editField.textColor = [UIColor redColor];
            enable = NO;
        } else {
            _editField.textColor = [UIColor blackColor];
            enable = YES;
        }
    }
    
    if(enable) {
        _next.backgroundColor = ZEGOGREEN;
        _next.enabled = YES;
    } else {
        _next.backgroundColor =ZEGODARKGREY;
        _next.enabled = NO;
    }
    
    if(!_fieldMessage.hidden) {
        _fieldMessage.hidden = YES;
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)saveUser:(id)sender {
    [self startLoading];
    [[ZegoAPIManager sharedManager] getUser:^(User *resp) {
        if(resp) {
            [self updateUser:resp];
            
            if(_field == 0) {
                resp.fname = _editField.text;
            } else if(_field == 1) {
                resp.lname = _editField.text;
            } else if(_field == 2) {
                resp.email = _editField.text;
            } else if(_field == 3) {
                resp.wemail = _editField.text;
            }
            
            [[ZegoAPIManager sharedManager] update:^(User *resp) {
                if(resp) {
                    [self updateUser:resp];
                    [self goBack];
                }
                [self stopLoading];
            } failure:^(RKObjectRequestOperation *operation, NSError *error) {
                if(error) {
                    ErrorResponse* e = [self handleError:error];
                    if(e) {
                        [self failedToCompleteWithError:e.msg];
                    }
                }
                [self stopLoading];
            } withRequest:resp];
            
        }
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        if(error) {
            [self handleError:error];
        }
        [self stopLoading];
    } withId:[self loggedUser].uid];
}

-(void)failedToCompleteWithError:(NSString*)err {
    _fieldMessage.textColor = [UIColor redColor];
    _fieldMessage.text = err;
    _fieldMessage.hidden = NO;
    _next.backgroundColor =ZEGODARKGREY;
    _next.enabled = NO;
    _editField.textColor = [UIColor redColor];
    _editField.enabled = YES;
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
