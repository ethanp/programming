//
//  SetCardView.m
//  DyanamicMatchismo
//
//  Created by Ethan Petuchowski on 12/20/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "SetCardView.h"

@implementation SetCardView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

// TODO
- (void)animateCardInsertion
{
    
}

// TODO
- (void)animateCardRemoval
{
    
}

// TODO
- (void)animateChooseCard
{
    
}

#define SHAPE_INSET_PROPORTION 0.7

- (void)drawRect:(CGRect)rect
{
    [super drawRect:rect];
    
    // TODO draw the card face in here
    // still not sure how to accomplish this for all the shapes
    
    CGRect shapeBounds = CGRectInset(self.frame,
                                     SHAPE_INSET_PROPORTION,
                                     SHAPE_INSET_PROPORTION);

    CGFloat hMid   = shapeBounds.origin.x + shapeBounds.size.width/2;
    CGFloat vMid   = shapeBounds.origin.y + shapeBounds.size.height/2;
    CGFloat left   = shapeBounds.origin.x;
    CGFloat right  = shapeBounds.origin.x + shapeBounds.size.width;
    CGFloat top    = shapeBounds.origin.y;
    CGFloat bottom = shapeBounds.origin.y + shapeBounds.size.height;
    
    UIBezierPath *shapeOutline = [[UIBezierPath alloc] init];
    
    // DRAW DIAMOND
    if ([self.card.shape isEqualToString:@"Diamond"]) {
        CGPoint sTop    = CGPointMake(hMid, top);
        CGPoint sRight  = CGPointMake(right, vMid);
        CGPoint sBottom = CGPointMake(hMid, bottom);
        CGPoint sLeft   = CGPointMake(left, vMid);
        
        [shapeOutline moveToPoint:sTop];
        [shapeOutline addLineToPoint:sRight];
        [shapeOutline addLineToPoint:sBottom];
        [shapeOutline addLineToPoint:sLeft];
        [shapeOutline closePath];
    }
    
    // set the color
    [self.card.colorDict[self.card.color] setFill];
    
    // differentiate it when it's chosen
    if (self.card.chosen) {} else {}
}


@end
