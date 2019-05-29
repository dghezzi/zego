//
//  NumberPadUIView.h
//  Letzgo
//
//  Created by Luca Adamo on 14/11/16.
//  Copyright © 2016 Luca Adamo. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol NumberPadDelegate
-(void) digitTapped:(NSString*)d;
-(void) deleteTapped;
@end

@interface NumberPadUIView : UIView
{
    
}

@property (assign) id <NumberPadDelegate> delegate;
@end
