//
//  ImageCropperViewController.m
//  Letzgo
//
//  Created by Luca Adamo on 24/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import "ImageCropperViewController.h"

@interface ImageCropperViewController ()

@end

@implementation ImageCropperViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    currentScale = 1;
    currentRotation = 1;
    dism = 0;
    presented = NO;
    
    [_close addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(closeVC:)]];
    
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void) viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self setUpView];
    
    // add gesture recognizers
    UIPinchGestureRecognizer* upgr = [[UIPinchGestureRecognizer alloc] initWithTarget:self action:@selector(pinch:)];
    upgr.delegate = self;
    [self.view addGestureRecognizer:upgr];
    
    UIRotationGestureRecognizer* urgr = [[UIRotationGestureRecognizer alloc]
                                         initWithTarget:self action:@selector(rotate:)];
    urgr.delegate = self;
    [self.view  addGestureRecognizer:urgr];
    
    UIPanGestureRecognizer* upagr = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(pan:)];
    upagr.delegate = self;
    [self.view  addGestureRecognizer:upagr];
    
    if(!presented) {
        [self retakeImg:self];
    }
    
}

- (void) useCameraRoll:(id)sender
{
    
    if ([UIImagePickerController isSourceTypeAvailable:
         UIImagePickerControllerSourceTypeSavedPhotosAlbum])
    {
        UIImagePickerController *imagePicker =
        [[UIImagePickerController alloc] init];
        imagePicker.delegate = self;
        imagePicker.sourceType =
        UIImagePickerControllerSourceTypePhotoLibrary;
        imagePicker.mediaTypes = @[(NSString *) kUTTypeImage];
        imagePicker.allowsEditing = NO;
        [self presentViewController:imagePicker
                           animated:YES completion:nil];
    }
}

-(void) useCamera:(id)sender

{
    _cameraView = [[CameraSessionView alloc] initWithFrame:CGRectMake(0, 21, self.view.frame.size.width, self.view.frame.size.height-21)];
    _cameraView.delegate = self;
    [_cameraView hideDismissButton];
    [self.view addSubview:_cameraView];
    _upload.hidden = YES;
    _retake.hidden = YES;}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    
    NSString *mediaType = info[UIImagePickerControllerMediaType];
    
    [picker dismissViewControllerAnimated:NO completion:NULL];
    
    if ([mediaType isEqualToString:(NSString *)kUTTypeImage]) {
        UIImage *image = info[UIImagePickerControllerOriginalImage];
        pic.image = image;
        CGFloat ws = image.size.width/pic.frame.size.width;
        CGFloat hs = image.size.height/pic.frame.size.height;
        CGFloat limitingScale = fmin(ws,hs);
        fillImgWidth = image.size.width / limitingScale;
        fillImgHeight = image.size.height / limitingScale;
        
    }
    else if ([mediaType isEqualToString:(NSString *)kUTTypeMovie])
    {
        
    }
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    [picker dismissViewControllerAnimated:NO completion:NULL];
    [self retakeImg:self];
}

-(void) setUpView
{
    if(!box)
    {
        CGFloat w = self.view.frame.size.width;
        CGFloat h = self.view.frame.size.height;
        
        CGRect fr = CGRectMake(0, (h-w)/2, w, w);
        
        box = [[UIView alloc] initWithFrame:fr];
        pic = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, w, w)];
        
        UIView* screenTop = [[UIView alloc] initWithFrame:CGRectMake(0, 0, w, (h-w)/2)];
        UIView* round = [[UIView alloc] initWithFrame:fr];
        UIView* screenBottom = [[UIView alloc] initWithFrame:CGRectMake(0, w+(h-w)/2, w, (h-w)/2)];
        screenBottom.backgroundColor = [UIColor blackColor];
        screenBottom.alpha = 0.75;
        screenTop.backgroundColor = [UIColor blackColor];
        screenTop.alpha = 0.75;
        round.backgroundColor = [UIColor blackColor];
        round.alpha = 0.75;
        CALayer *mask = [CALayer layer];
        mask.contents = (id)[[UIImage imageNamed:@"ph_mask"] CGImage];
        mask.frame = CGRectMake(0,0,round.frame.size.width,round.frame.size.height);
        round.layer.mask = mask;
        round.layer.masksToBounds = YES;
        
        [box addSubview:pic];
        [self.view addSubview:box];
        [self.view addSubview:round];
        [self.view addSubview:screenTop];
        [self.view addSubview:screenBottom];
        [self.view bringSubviewToFront:screenBottom];
        [self.view bringSubviewToFront:screenTop];
        [self.view bringSubviewToFront:round];
        
        pic.contentMode = UIViewContentModeScaleAspectFill;
        pic.userInteractionEnabled = YES;
        [self.view bringSubviewToFront:_close];
        [self.view bringSubviewToFront:_retake];
        [self.view bringSubviewToFront:_upload];
        
    }
}

- (void)pinch:(UIPinchGestureRecognizer *)gesture {
    [self adjustAnchorPointForGestureRecognizer:gesture];
    
    if (gesture.state == UIGestureRecognizerStateBegan
        || gesture.state == UIGestureRecognizerStateChanged) {
        
        
        [self scaleImageWithScale:gesture.scale];
        gesture.scale = 1;
    }
    
    if(gesture.state == UIGestureRecognizerStateEnded) {
        [self moveView:pic withinBoundsWithX:0 andY:0];
    }
}

-(void) scaleImageWithScale:(CGFloat)f
{
    CGFloat cscale = (!currentScale || currentScale == 0) ? 1.0f : currentScale;
    currentScale = cscale;
    CGFloat newScale = currentScale * f;
    if(newScale>=1){
        CGAffineTransform transform =  CGAffineTransformScale([pic transform], f, f);
        pic.transform = transform;
        
        // update creation
        currentScale = newScale;
    }
}


- (IBAction)rotate:(UIRotationGestureRecognizer *)gestureRecognizer
{
    UIView *piece = pic;
    [self adjustAnchorPointForGestureRecognizer:gestureRecognizer];
    
    if ([gestureRecognizer state] == UIGestureRecognizerStateBegan ||
        [gestureRecognizer state] == UIGestureRecognizerStateChanged) {
        piece.transform = CGAffineTransformRotate([piece transform], [gestureRecognizer rotation]);
        
        currentRotation = currentRotation + [gestureRecognizer rotation];
        [gestureRecognizer setRotation:0];
    }
}

- (void)adjustAnchorPointForGestureRecognizer:(UIGestureRecognizer *)gestureRecognizer
{
    if (gestureRecognizer.state == UIGestureRecognizerStateBegan) {
        
        UIView *piece = gestureRecognizer.view;
        CGPoint locationInView = [gestureRecognizer locationInView:piece];
        CGPoint locationInSuperview = [gestureRecognizer locationInView:piece.superview];
        
        piece.layer.anchorPoint = CGPointMake(locationInView.x / piece.bounds.size.width,
                                              locationInView.y / piece.bounds.size.height);
        piece.center = locationInSuperview;
    }
}

- (void)pan:(UIPanGestureRecognizer *)gesture {
    UIView *piece = pic;
    UIImage* im = pic.image;
    
    [self adjustAnchorPointForGestureRecognizer:gesture];
    
    if ([gesture state] == UIGestureRecognizerStateBegan || [gesture state] == UIGestureRecognizerStateChanged) {
        CGPoint translation = [gesture translationInView:[piece superview]];
        CGFloat tx = translation.x;
        CGFloat ty = translation.y;
        [self moveView:piece withinBoundsWithX:tx andY:ty];
        
        [gesture setTranslation:CGPointZero inView:[piece superview]];
        
    }
}

-(void)moveView:(UIView*)piece withinBoundsWithX:(CGFloat)tx andY:(CGFloat)ty {
    CGFloat xmin = ((fillImgWidth*currentScale)/(-2.0))+piece.bounds.size.width;
    CGFloat xmax = (fillImgWidth*currentScale)/2.0;
    CGFloat transX = ([piece center].x + tx) <= xmin ? xmin :
    ([piece center].x + tx) >= xmax ? xmax : ([piece center].x + tx);
    
    CGFloat ymin = ((fillImgHeight*currentScale)/(-2.0))+piece.bounds.size.height;
    CGFloat ymax = (fillImgHeight*currentScale)/2.0;
    CGFloat transY = ([piece center].y + ty) <= ymin ? ymin :
    ([piece center].y + ty) >= ymax ? ymax : ([piece center].y + ty);
    
    [piece setCenter:CGPointMake(transX, transY)];
}

-(IBAction)closeVC:(id)sender
{
    [[self presentingViewController] dismissViewControllerAnimated:NO completion:nil];
}

-(IBAction)uploadImg:(id)sender
{
    [self startLoading];
    UIImage* full = [self imageWithView:box];
    NSInteger sec = [[NSDate date] timeIntervalSince1970];
    NSString* imgname = [NSString stringWithFormat:@"%ld_%ld.jpg",[self loggedUser].uid, sec];
    [self uploadImageToS3:full withSize:CGSizeMake(200, 200) andName:imgname];
}

-(IBAction)retakeImg:(id)sender
{
    SCLAlertView *alert = [[SCLAlertView alloc] init];
    alert.shouldDismissOnTapOutside = YES;
    [alert addButton:NSLocalizedString(@"Carica da galleria",nil) actionBlock:^(void) {
        dism = 1;
        [self useCameraRoll:self];
    }];
    
    [alert addButton:NSLocalizedString(@"Scatta una foto",nil) actionBlock:^(void) {
        dism = 2;
        [self useCamera:self];
    }];
    
    [alert showCustom:self image:[UIImage imageNamed:@"icon-update-w"] color:ZEGOGREEN title:@"" subTitle:NSLocalizedString(@"Carica un'immagine profilo.",nil) closeButtonTitle:NSLocalizedString(@"Chiudi",nil) duration:0.0f]; // Custom
    
    
    //Using Block
    [alert alertIsDismissed:^{
        if(dism == 0){
            [self closeVC:self];
        }
    }];
    presented = YES;
}


- (UIImage *) imageWithView:(UIView *)view
{
    UIGraphicsBeginImageContextWithOptions(view.bounds.size, NO, 0.0f);
    CGContextRef ctx = UIGraphicsGetCurrentContext();
    [[UIColor clearColor] set];
    CGContextFillRect(ctx, view.bounds);
    
    [view drawViewHierarchyInRect:view.bounds afterScreenUpdates:NO];
    UIImage * snapshotImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return snapshotImage;
}

-(void)didCaptureImage:(UIImage *)image {
    _upload.hidden = NO;
    _retake.hidden = NO;
    [self.cameraView removeFromSuperview];
    pic.image = image;
    
    CGFloat ws = image.size.width/pic.frame.size.width;
    CGFloat hs = image.size.height/pic.frame.size.height;
    CGFloat limitingScale = fmin(ws,hs);
    fillImgWidth = image.size.width / limitingScale;
    fillImgHeight = image.size.height / limitingScale;
    
    
    UIImageWriteToSavedPhotosAlbum(image, nil, nil, nil);
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
