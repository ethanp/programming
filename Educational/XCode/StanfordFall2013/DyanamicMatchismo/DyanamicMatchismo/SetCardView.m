//
//  SetCardView.m
//  DyanamicMatchismo
//
//  Created by Ethan Petuchowski on 12/20/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "SetCardView.h"

@implementation SetCardView

- (void)animateChooseCard
{
    [UIView animateWithDuration:0.5
                          delay:0
                        options:UIViewAnimationOptionCurveEaseInOut
                     animations:^{ self.alpha = self.thinksItsChosen ? 0.6 : 1.0; }
                     completion:nil
     ];
}

#define SHAPE_INSET_PROPORTION 0.1
#define OFF_MID_PROP 0.3

enum loc { left, top, right, bottom, hMid, vMid };

- (CGFloat)normBound:(int)location ofRect:(CGRect)rect
{
    if (location == left)   return rect.origin.x;
    if (location == top)    return rect.origin.y;
    if (location == right)  return rect.origin.x + rect.size.width;
    if (location == bottom) return rect.origin.y + rect.size.height;
    if (location == hMid)   return rect.origin.x + rect.size.width/2;
    if (location == vMid)   return rect.origin.y + rect.size.height/2;
    
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

- (UIBezierPath *)drawDiamondInRect:(CGRect)rect
{
    UIBezierPath *shapeOutline = [[UIBezierPath alloc] init];
    [shapeOutline moveToPoint:[self normPt:topMid ofRect:rect]];
    [shapeOutline addLineToPoint:[self normPt:rtMid  ofRect:rect]];
    [shapeOutline addLineToPoint:[self normPt:btMid  ofRect:rect]];
    [shapeOutline addLineToPoint:[self normPt:lftMid ofRect:rect]];
    [shapeOutline closePath];
    return shapeOutline;
}

- (UIBezierPath *)drawOvalInRect:(CGRect)rect
{
    UIBezierPath *shapeOutline = [[UIBezierPath alloc] init];
    shapeOutline = [UIBezierPath bezierPathWithOvalInRect:rect];
    return shapeOutline;
}

- (UIBezierPath *)drawSquiggleInRect:(CGRect)rect
{
    UIBezierPath *shapeOutline = [[UIBezierPath alloc] init];
    CGPoint startPoint = [self normPt:tpLft ofRect:rect];
    startPoint.y += 10;
    [shapeOutline moveToPoint:[self normPt:topMid ofRect:rect]];
    [shapeOutline addCurveToPoint:[self normPt:btMid ofRect:rect]
                    controlPoint1:[self normPt:tpLft ofRect:rect]
                    controlPoint2:[self normPt:midMid ofRect:rect]];
    [shapeOutline addCurveToPoint:[self normPt:topMid ofRect:rect]
                    controlPoint1:[self normPt:btRt ofRect:rect]
                    controlPoint2:[self normPt:midMid ofRect:rect]];
    return shapeOutline;
}

- (void)drawRect:(CGRect)rect
{
    [super drawRect:rect];
    
    CGRect shapeArea = CGRectInset(self.bounds,
                                   self.bounds.size.width * SHAPE_INSET_PROPORTION,
                                   self.bounds.size.height * SHAPE_INSET_PROPORTION);
    
    CGRect singleShapeRect = CGRectInset(shapeArea, 0, shapeArea.size.height / 3);
    CGRect upperRect = singleShapeRect;
    CGRect lowerRect = singleShapeRect;
    NSArray *shapeRectsArray = Nil;
    
    
    /* ===========  CREATE SHAPE FRAMES  =========== */

    if ([self.card.number isEqualToNumber:@1])
        shapeRectsArray = @[[NSValue valueWithCGRect:singleShapeRect]];
    
    else if ([self.card.number isEqualToNumber:@2]) {
        upperRect.origin.y -= upperRect.size.height/2;
        lowerRect.origin.y += lowerRect.size.height/2;
        shapeRectsArray = @[[NSValue valueWithCGRect:upperRect],
                            [NSValue valueWithCGRect:lowerRect]];
    }
    
    else if ([self.card.number isEqualToNumber:@3]) {
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
    
    NSMutableArray *bezierPathArray = [[NSMutableArray alloc] init];

    for (NSValue *rectVal in shapeRectsArray) {
        if ([self.card.shape isEqualToString:@"Diamond"])
            [bezierPathArray addObject:[self drawDiamondInRect:[rectVal CGRectValue]]];
        else if ([self.card.shape isEqualToString:@"Oval"])
            [bezierPathArray addObject:[self drawOvalInRect:[rectVal CGRectValue]]];
        else if ([self.card.shape isEqualToString:@"Squiggle"])
            [bezierPathArray addObject:[self drawSquiggleInRect:[rectVal CGRectValue]]];
        else [NSException raise:@"Invalid card shape" format:@"%@", self.card.shape];
    }
    
    
    /* ===========  SET COLOR  =========== */

    UIColor *cardColor = self.card.colorDict[self.card.color];
    [cardColor set]; // does both setFill and setStroke

    
    /* ===========  DRAW FILL  =========== */
    
    for (UIBezierPath *path in bezierPathArray) {
        if ([self.card.fillType isEqualToString:@"Solid"])
            [path fill];
        else if ([self.card.fillType isEqualToString:@"Unfilled"])
            [path stroke];
        else if ([self.card.fillType isEqualToString:@"Striped"]) {
            CGContextRef context = UIGraphicsGetCurrentContext();
            CGContextSaveGState(context);  // save state before clipping
            // clip to only keep strokes inside the path
            [path addClip];
            // draw stripes
            for (int xOffset = 0; xOffset <= self.frame.size.width; xOffset += 2) {
                UIBezierPath *stripe = [[UIBezierPath alloc] init];
                [stripe moveToPoint:CGPointMake(xOffset, rect.origin.y)];
                [stripe addLineToPoint:
                 CGPointMake(xOffset, [self normBound:bottom ofRect:rect])];
                stripe.lineWidth /= 2;
                [stripe stroke];
            }
            // draw outline
            [path stroke];
            CGContextRestoreGState(UIGraphicsGetCurrentContext());
        }
        else [NSException raise:@"Invalid card fillType" format:@"%@", self.card.fillType];
    }
    
    
    /* ===========  DIFFERENTIATE CHOSEN  =========== */
    
    self.alpha = self.thinksItsChosen ? 0.6 : 1.0;
}

@end
