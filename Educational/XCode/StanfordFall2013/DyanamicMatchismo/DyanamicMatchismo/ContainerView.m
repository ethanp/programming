//
//  ContainerView.m
//  DyanamicMatchismo
//
//  Created by Ethan Petuchowski on 12/23/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "ContainerView.h"
#import "CardView.h"

@interface ContainerView ()
- (void)handlePinch:(UIPinchGestureRecognizer *)gesture;
@end

@implementation ContainerView

- (void)handlePinch:(UIPinchGestureRecognizer *)gesture
{
    for (id subview in self.subviews) {
        if ([subview isKindOfClass:[CardView class]]) {
            CardView *cardView = (CardView *)subview;
            if (!cardView.bounds.origin.x)
                [cardView animateMoveToPile];
            else
                [cardView animateReturnFromPile];
        }
    }
    return;
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    UIPinchGestureRecognizer *pinchRecognizer =
    [[UIPinchGestureRecognizer alloc] initWithTarget:self
                                              action:@selector(handlePinch:)];
    
    [self addGestureRecognizer:pinchRecognizer];
}

@end
