//
//  ImageCropperViewController.h
//  Letzgo
//
//  Created by Luca Adamo on 24/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ZegoViewController.h"
#import "TouchDelegate.h"
#import <QuartzCore/QuartzCore.h>
#import <MobileCoreServices/MobileCoreServices.h>
#import "CameraSessionView.h"

@interface ImageCropperViewController : ZegoViewController<UIGestureRecognizerDelegate, TouchDelegate, UIImagePickerControllerDelegate,UINavigationControllerDelegate,CACameraSessionDelegate>
{
    UIView* box;
    UIImageView* pic;
    CGFloat currentScale;
    CGFloat currentRotation;
    CGFloat offsetX;
    CGFloat offsetY;
    
    CGFloat fillImgWidth;
    CGFloat fillImgHeight;
    
    
    BOOL presented;
    MBProgressHUD *hud;
    long rnd;
    
    NSInteger dism;
}


@property(nonatomic,strong) IBOutlet UIView* close;
@property(nonatomic,strong) IBOutlet UIButton* upload;
@property(nonatomic,strong) IBOutlet UIButton* retake;

@property (nonatomic, strong) CameraSessionView *cameraView;

-(IBAction)closeVC:(id)sender;
-(IBAction)uploadImg:(id)sender;
-(IBAction)retakeImg:(id)sender;
@end
