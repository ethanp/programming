//
//  SetCardView.m
//  DyanamicMatchismo
//
//  Created by Ethan Petuchowski on 12/20/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "SetCardView.h"

@implementation SetCardView

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

- (void)drawDiamondInRect:(CGRect)rect
{
    UIBezierPath *shapeOutline = [[UIBezierPath alloc] init];
    [shapeOutline moveToPoint:[self normPt:topMid ofRect:rect]];
    
    // TODO etc.
//    [shapeOutline addLineToPoint:[self normPt:rightMiddle];
//    [shapeOutline addLineToPoint:[self normPt:bottomMiddle];
//    [shapeOutline addLineToPoint:[self normPt:leftMiddle];
//    [shapeOutline closePath];
}

- (void)drawOvalInRect:(CGRect)rect
{
    UIBezierPath *shapeOutline = [[UIBezierPath alloc] init];
    shapeOutline = [UIBezierPath bezierPathWithOvalInRect:rect];
}

- (void)drawSquiggleInRect:(CGRect)rect
{
    UIBezierPath *shapeOutline = [[UIBezierPath alloc] init];
//    [shapeOutline moveToPoint:[self normBound:topMid ofRect:rect]];
}

- (void)drawRect:(CGRect)rect
{
    [super drawRect:rect];
    
    // TODO draw the card face in here
    
    // TODO keep changing all this to use the card.number
    
    // TODO note that the shapes themselves are always the same size
    // its just that there are either 1,2,3 of them
    
    
    
    CGRect shapeArea = CGRectInset(self.frame,
                                     SHAPE_INSET_PROPORTION,
                                     SHAPE_INSET_PROPORTION);
    
    CGRect singleShapeRect = CGRectInset(shapeArea, 1, 0.333);

    NSArray *shapeRectsArray = Nil;
    
    /* ===========  CREATE SHAPE FRAMES  =========== */
    
    if ([self.card.number isEqualToNumber:@1])
        shapeRectsArray = @[[NSValue valueWithCGRect:singleShapeRect]];
    
    else if ([self.card.number isEqualToNumber:@2]) {
        CGRect upperRect = singleShapeRect;
        CGRect lowerRect = singleShapeRect;
        upperRect.origin.y -= upperRect.size.height/2;
        lowerRect.origin.y += lowerRect.size.height/2;
        
        shapeRectsArray = @[[NSValue valueWithCGRect:upperRect],
                            [NSValue valueWithCGRect:lowerRect]];
    }
    
    else if ([self.card.number isEqualToNumber:@3]) {
        CGRect upperRect = singleShapeRect;
        CGRect lowerRect = singleShapeRect;
        upperRect.origin.y -= upperRect.size.height;
        lowerRect.origin.y += lowerRect.size.height;
        
        shapeRectsArray = @[[NSValue valueWithCGRect:upperRect],
                            [NSValue valueWithCGRect:singleShapeRect],
                            [NSValue valueWithCGRect:lowerRect]];
    }
    
    else [NSException raise:@"Invalid card number"
                     format:@"Number was: %@", self.card.number];
    
    if (!shapeRectsArray)
        [NSException raise:@"shapeRectsArray was never filled" format:@""];
    
    
    /* ===========  DRAW SHAPES  =========== */
    
    if ([self.card.shape isEqualToString:@"Diamond"])
        for (NSValue *rectVal in shapeRectsArray)
            [self drawDiamondInRect:[rectVal CGRectValue]];
    
    else if ([self.card.shape isEqualToString:@"Oval"])
        for (NSValue *rectVal in shapeRectsArray)
            [self drawOvalInRect:[rectVal CGRectValue]];
    
    else if ([self.card.shape isEqualToString:@"Squiggle"])
        for (NSValue *rectVal in shapeRectsArray)
            [self drawSquiggleInRect:[rectVal CGRectValue]];
    
    else [NSException raise:@"Invalid card shape" format:@"%@", self.card.shape];
    
    // COLOR
    UIColor *cardColor = self.card.colorDict[self.card.color];
    [cardColor set]; // does a setFill /and/ a setStroke
    
    // SOLID
    if ([self.card.fillType isEqualToString:@"Solid"]) {
//        [shapeOutline fill];
    }
    
    // UNFILLED
    else if ([self.card.fillType isEqualToString:@"Unfilled"]) {
//        [shapeOutline stroke];
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
