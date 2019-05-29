//
//  EditPhoneViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 21/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "LoginViewController.h"

@interface EditPhoneViewController : LoginViewController<InsertPinControllerDelegate>
{
    
}

-(IBAction)saveAction:(id)sender;

@end
