//
//  SideProfileViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 20/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"
#import <MobileCoreServices/MobileCoreServices.h>

@interface SideProfileViewController : ZegoViewController<UIImagePickerControllerDelegate,UINavigationControllerDelegate, UITableViewDataSource,UITableViewDelegate>
{
    User* u;
    NSInteger idx;
    Address* homeAddress;
    Address* workAddress;
}

@property (strong, nonatomic) IBOutlet UIView *backView;
@property (strong, nonatomic) IBOutlet UILabel *topTitle;
@property (strong, nonatomic) IBOutlet UIView *changeProfileFoto;
@property (strong, nonatomic) IBOutlet UITableView *table;
@property (strong, nonatomic) IBOutlet UIImageView *profileImg;
@property (strong, nonatomic) IBOutlet UILabel *profileImgCta;

@end
