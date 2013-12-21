//
//  SetCardView.h
//  DyanamicMatchismo
//
//  Created by Ethan Petuchowski on 12/20/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SetCard.h"

@interface SetCardView : UIView

//@property (nonatomic) NSUInteger rank;
//@property (strong, nonatomic) NSString *suit;
//@property (nonatomic) BOOL faceUp;
//
//- (void)pinch:(UIPinchGestureRecognizer *)gesture;
//- (void)handleSwipe:(UITapGestureRecognizer *)gesture;

@property (nonatomic) SetCard *card;

@end
