//
//  EditSimpleFieldViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 21/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"

@interface EditSimpleFieldViewController : ZegoViewController<UITextFieldDelegate>
{
    
}

@property (nonatomic, assign) NSInteger field;
@property (strong, nonatomic) IBOutlet UITextField* editField;
@property (strong, nonatomic) IBOutlet UIView *backView;
@property (strong, nonatomic) IBOutlet UILabel *topTitle;
@property (strong, nonatomic) IBOutlet UIButton *next;
@property (strong, nonatomic) IBOutlet UILabel *fieldMessage;
@property (strong, nonatomic) IBOutlet UILabel *topMessage;
-(IBAction)saveUser:(id)sender;

@end
