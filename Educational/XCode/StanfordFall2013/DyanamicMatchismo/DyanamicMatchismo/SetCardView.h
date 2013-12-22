//
//  SetCardView.h
//  DyanamicMatchismo
//
//  Created by Ethan Petuchowski on 12/20/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SetCard.h"
#import "CardView.h"

@interface SetCardView : CardView

//@property (nonatomic) NSUInteger rank;
//@property (strong, nonatomic) NSString *suit;
//@property (nonatomic) BOOL faceUp;
//
//- (void)pinch:(UIPinchGestureRecognizer *)gesture;
//- (void)handleSwipe:(UITapGestureRecognizer *)gesture;

@property (nonatomic, weak) SetCard *card;

@end
