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

#define SHAPE_INSET_PROPORTION 0.1

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
    [shapeOutline moveToPoint:[self normPt:topMid ofRect:rect]];
    return shapeOutline;
}

- (void)drawRect:(CGRect)rect
{
    [super drawRect:rect];
    
    // TODO draw the card face in here
    
    CGRect shapeArea = CGRectInset(self.bounds,
                                   self.bounds.size.width * SHAPE_INSET_PROPORTION,
                                   self.bounds.size.height * SHAPE_INSET_PROPORTION);
    
    CGRect singleShapeRect = CGRectInset(shapeArea, 0, shapeArea.size.height / 3);

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
    
    NSMutableArray *bezierPathArray = [[NSMutableArray alloc] init];
    
    if ([self.card.shape isEqualToString:@"Diamond"])
        for (NSValue *rectVal in shapeRectsArray)
            [bezierPathArray addObject:[self drawDiamondInRect:[rectVal CGRectValue]]];
    
    else if ([self.card.shape isEqualToString:@"Oval"])
        for (NSValue *rectVal in shapeRectsArray)
            [bezierPathArray addObject:[self drawOvalInRect:[rectVal CGRectValue]]];
    
    else if ([self.card.shape isEqualToString:@"Squiggle"])
        for (NSValue *rectVal in shapeRectsArray)
            [bezierPathArray addObject:[self drawSquiggleInRect:[rectVal CGRectValue]]];
    
    else [NSException raise:@"Invalid card shape" format:@"%@", self.card.shape];
    
    // COLOR
    UIColor *cardColor = self.card.colorDict[self.card.color];
    [cardColor set]; // does both setFill and setStroke
    
    // SOLID
    if ([self.card.fillType isEqualToString:@"Solid"])
        for (UIBezierPath *path in bezierPathArray)
            [path fill];
    
    // UNFILLED
    else if ([self.card.fillType isEqualToString:@"Unfilled"])
        for (UIBezierPath *path in bezierPathArray)
            [path stroke];
    
    // STRIPED: draw vertical stripes all across the rect, then clip them to the shape
    else if ([self.card.fillType isEqualToString:@"Striped"]) {
        for (int iconNum = 1; iconNum <= [self.card.number intValue]; iconNum++) {
            UIBezierPath *path = bezierPathArray[iconNum-1];
            CGRect rect = [shapeRectsArray[iconNum-1] CGRectValue];
            CGContextRef context = UIGraphicsGetCurrentContext();
            CGContextSaveGState(context);  // save state before clipping
            // clip to only keep strokes inside the path
            [path addClip];
            
            // draw stripes
            for (int xOffset = 0; xOffset <= rect.size.width/SHAPE_INSET_PROPORTION; xOffset += 2) {
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
    }
    
    else [NSException raise:@"Invalid card fillType" format:@"%@", self.card.fillType];
    
    // TODO differentiate it when it's chosen (shade? outline? etc?)
    if (self.thinksItsChosen) {} else {}
}


@end
