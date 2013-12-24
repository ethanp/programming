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
- (void)animateChooseCard
{
    
}

#define SHAPE_INSET_PROPORTION 0.7

enum loc { left, top, right, bottom, hMid, vMid, uprMid, lwrMid };
- (CGFloat)normBound:(int)location ofRect:(CGRect)rect
{
    if (location == left)   return rect.origin.x;
    if (location == top)    return rect.origin.y;
    if (location == right)  return rect.origin.x + rect.size.width;
    if (location == bottom) return rect.origin.y + rect.size.height;
    if (location == hMid)   return rect.origin.x + rect.size.width/2;
    if (location == vMid)   return rect.origin.y + rect.size.height/2;
    if (location == uprMid) return rect.origin.y; // TODO finish this line
    // TODO write the other ones
    
    else [NSException raise:@"normBound has limited capabilities"
                     format:@"You passed a %d", location];
    return -1; // unreachable
}

enum pt { topMid, rtMid, btMid, lftMid, midMid,
          topRt, btRt, btLft, tpLft,
          upLftMid, btRtMid };
- (CGPoint)normPt:(int)location ofRect:(CGRect)rect
{
    if (location == topMid)
        return CGPointMake([self normBound:hMid ofRect:rect],
                           [self normBound:top ofRect:rect]);
    if (location == rtMid)
        return CGPointMake([self normBound:right ofRect:rect],
                           [self normBound:vMid ofRect:rect]);
    if (location == btMid)
        return CGPointMake([self normBound:hMid ofRect:rect],
                           [self normBound:bottom ofRect:rect]);
    if (location == lftMid)
        return CGPointMake([self normBound:left ofRect:rect],
                           [self normBound:vMid ofRect:rect]);
    if (location == midMid)
        return CGPointMake([self normBound:hMid ofRect:rect],
                           [self normBound:vMid ofRect:rect]);
    if (location == topRt)
        return CGPointMake([self normBound:right ofRect:rect],
                           [self normBound:top ofRect:rect]);
    if (location == btRt)
        return CGPointMake([self normBound:right ofRect:rect],
                           [self normBound:bottom ofRect:rect]);
    if (location == btLft)
        return CGPointMake([self normBound:left ofRect:rect],
                           [self normBound:bottom ofRect:rect]);
    if (location == tpLft)
        return CGPointMake([self normBound:left ofRect:rect],
                           [self normBound:top ofRect:rect]);
    
    else [NSException raise:@"normPt has limited capabilities"
                     format:@"You passed a %d", location];
    return CGPointZero; // unreachable
}

- (void)drawRect:(CGRect)rect
{
    [super drawRect:rect];
    
    // TODO draw the card face in here
    
    // TODO keep changing all this to use the card.number
    
    // TODO note that the shapes themselves are always the same size
    // its just that there are either 1,2,3 of them
    
    CGRect shapeBounds = CGRectInset(self.frame,
                                     SHAPE_INSET_PROPORTION,
                                     SHAPE_INSET_PROPORTION);

    CGFloat left   = shapeBounds.origin.x;
    CGFloat top    = shapeBounds.origin.y;
    CGFloat right  = shapeBounds.origin.x + shapeBounds.size.width;
    CGFloat bottom = shapeBounds.origin.y + shapeBounds.size.height;
    CGFloat hMid   = shapeBounds.origin.x + shapeBounds.size.width/2;
    CGFloat vMid   = shapeBounds.origin.y + shapeBounds.size.height/2;
    
    CGPoint topMiddle    = CGPointMake(hMid, top);
    CGPoint rightMiddle  = CGPointMake(right, vMid);
    CGPoint bottomMiddle = CGPointMake(hMid, bottom);
    CGPoint leftMiddle   = CGPointMake(left, vMid);
    
    UIBezierPath *shapeOutline = [[UIBezierPath alloc] init];
    
    // DRAW DIAMOND
    if ([self.card.shape isEqualToString:@"Diamond"]) {
        [shapeOutline moveToPoint:[self normPt:topMid ofRect:shapeBounds]];
        
        // TODO etc.
        [shapeOutline addLineToPoint:rightMiddle];
        [shapeOutline addLineToPoint:bottomMiddle];
        [shapeOutline addLineToPoint:leftMiddle];
        [shapeOutline closePath];
    }
    
    // DRAW OVAL
    else if ([self.card.shape isEqualToString:@"Oval"]) {
        shapeOutline = [UIBezierPath bezierPathWithOvalInRect:shapeBounds];
    }
    
    // DRAW SQUIGGLE (uh oh...)
    else if ([self.card.shape isEqualToString:@"Squiggle"]) {
        [shapeOutline moveToPoint:topMiddle];
        // TODO
    }
    
    else [NSException raise:@"Invalid card shape" format:@"%@", self.card.shape];
    
    // COLOR
    UIColor *cardColor = self.card.colorDict[self.card.color];
    [cardColor set]; // does a setFill /and/ a setStroke
    
    // SOLID
    if ([self.card.fillType isEqualToString:@"Solid"]) {
        [shapeOutline fill];
    }
    
    // UNFILLED
    else if ([self.card.fillType isEqualToString:@"Unfilled"]) {
        [shapeOutline stroke];
    }
    
    // STRIPED (uh oh...)
    else if ([self.card.fillType isEqualToString:@"Striped"]) {
        // ???
    }
    
    else [NSException raise:@"Invalid card fillType" format:@"%@", self.card.fillType];
    
    // differentiate it when it's chosen
    if (self.thinksItsChosen) {} else {}
}


@end
