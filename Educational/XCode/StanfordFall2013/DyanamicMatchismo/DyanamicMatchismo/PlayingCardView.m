//
//  PlayingCardView.m
//  SuperCard
//
//  Created by CS193p Instructor.
//  Modified by Ethan Petuchowski, who is unaffiliated with Stanford.
//  Copyright (c) 2013 Stanford University. All rights reserved.
//

#import "PlayingCardView.h"

@interface PlayingCardView()
@end

@implementation PlayingCardView

#pragma mark - Properties

- (NSString *)rankAsString
{
    return @[@"?",@"A",@"2",@"3",@"4",@"5",@"6",@"7",@"8",@"9",@"10",@"J",@"Q",@"K"]
            [self.card.rank];
}

# pragma mark - Animation

- (void)animateChooseCard
{
    [UIView transitionWithView:self
                      duration:0.5
                       options:UIViewAnimationOptionTransitionFlipFromLeft
                    animations:^{}
                    completion:nil];
    
    [self setNeedsDisplay];
}


- (void)drawRect:(CGRect)rect
{
    [super drawRect:rect];

    if (self.card.chosen) {
        UIImage *faceImage = [UIImage imageNamed:
                              [NSString stringWithFormat:@"%@%@",
                               [self rankAsString], self.card.suit]];
        if (faceImage) {
            [faceImage drawInRect:self.bounds];
        } else {
            [self drawPips];
        }

        [self drawTextInCorners];
    } else {
        [[UIImage imageNamed:@"cardback"] drawInRect:self.bounds];
    }
}

- (void)pushContextAndRotateUpsideDown
{
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSaveGState(context);
    CGContextTranslateCTM(context, self.bounds.size.width, self.bounds.size.height);
    CGContextRotateCTM(context, M_PI);
}

- (void)popContext
{
    CGContextRestoreGState(UIGraphicsGetCurrentContext());
}

#pragma mark - Corners

- (void)drawTextInCorners
{
    NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
    paragraphStyle.alignment = NSTextAlignmentCenter;

    UIFont *cornerFont = [UIFont preferredFontForTextStyle:UIFontTextStyleBody];
    cornerFont = [cornerFont fontWithSize:cornerFont.pointSize * [self cornerScaleFactor]];

    NSAttributedString *cornerText =
    [[NSAttributedString alloc] initWithString:[NSString stringWithFormat:@"%@\n%@",
                                                [self rankAsString], self.card.suit]
                                    attributes:@{ NSFontAttributeName : cornerFont, NSParagraphStyleAttributeName : paragraphStyle }];

    CGRect textBounds;
    textBounds.origin = CGPointMake([self cornerOffset], [self cornerOffset]);
    textBounds.size = [cornerText size];
    [cornerText drawInRect:textBounds];

    [self pushContextAndRotateUpsideDown];
    [cornerText drawInRect:textBounds];
    [self popContext];
}

#pragma mark - Pips

#define PIP_HOFFSET_PERCENTAGE 0.165
#define PIP_VOFFSET1_PERCENTAGE 0.090
#define PIP_VOFFSET2_PERCENTAGE 0.175
#define PIP_VOFFSET3_PERCENTAGE 0.270

- (void)drawPips
{
    if ((self.card.rank == 1) || (self.card.rank == 3) ||
        (self.card.rank == 5) || (self.card.rank == 9)) {
        [self drawPipsWithHorizontalOffset:0
                            verticalOffset:0
                        mirroredVertically:NO];
    }
    if ((self.card.rank == 6) || (self.card.rank == 7) || (self.card.rank == 8)) {
        [self drawPipsWithHorizontalOffset:PIP_HOFFSET_PERCENTAGE
                            verticalOffset:0
                        mirroredVertically:NO];
    }
    if ((self.card.rank == 2) || (self.card.rank == 3) ||
        (self.card.rank == 7) || (self.card.rank == 8) || (self.card.rank == 10)) {
        [self drawPipsWithHorizontalOffset:0
                            verticalOffset:PIP_VOFFSET2_PERCENTAGE
                        mirroredVertically:(self.card.rank != 7)];
    }
    if ((self.card.rank == 4) || (self.card.rank == 5) ||
        (self.card.rank == 6) || (self.card.rank == 7) ||
        (self.card.rank == 8) || (self.card.rank == 9) || (self.card.rank == 10)) {
        [self drawPipsWithHorizontalOffset:PIP_HOFFSET_PERCENTAGE
                            verticalOffset:PIP_VOFFSET3_PERCENTAGE
                        mirroredVertically:YES];
    }
    if ((self.card.rank == 9) || (self.card.rank == 10)) {
        [self drawPipsWithHorizontalOffset:PIP_HOFFSET_PERCENTAGE
                            verticalOffset:PIP_VOFFSET1_PERCENTAGE
                        mirroredVertically:YES];
    }
}

#define PIP_FONT_SCALE_FACTOR 0.012

- (void)drawPipsWithHorizontalOffset:(CGFloat)hoffset
                      verticalOffset:(CGFloat)voffset
                          upsideDown:(BOOL)upsideDown
{
    if (upsideDown) [self pushContextAndRotateUpsideDown];
    CGPoint middle = CGPointMake(self.bounds.size.width/2, self.bounds.size.height/2);
    UIFont *pipFont = [UIFont preferredFontForTextStyle:UIFontTextStyleBody];
    pipFont = [pipFont fontWithSize:
               [pipFont pointSize] * self.bounds.size.width * PIP_FONT_SCALE_FACTOR];

    NSAttributedString *attributedSuit = [[NSAttributedString alloc]
                                          initWithString:self.card.suit
                                          attributes:@{ NSFontAttributeName:pipFont }];

    CGSize pipSize = [attributedSuit size];
    
    CGPoint pipOrigin = CGPointMake(
                            middle.x-pipSize.width/2.0-hoffset*self.bounds.size.width,
                            middle.y-pipSize.height/2.0-voffset*self.bounds.size.height
                        );
    
    [attributedSuit drawAtPoint:pipOrigin];
    
    if (hoffset) {
        pipOrigin.x += hoffset*2.0*self.bounds.size.width;
        [attributedSuit drawAtPoint:pipOrigin];
    }
    
    if (upsideDown) [self popContext];
}

- (void)drawPipsWithHorizontalOffset:(CGFloat)hoffset
                      verticalOffset:(CGFloat)voffset
                  mirroredVertically:(BOOL)mirroredVertically
{
    [self drawPipsWithHorizontalOffset:hoffset
                        verticalOffset:voffset
                            upsideDown:NO];
    if (mirroredVertically) {
        [self drawPipsWithHorizontalOffset:hoffset
                            verticalOffset:voffset
                                upsideDown:YES];
    }
}

#pragma mark - Initialization

- (id)initWithFrame:(CGRect)frame
           withCard:(PlayingCard *)card
        inContainer:(PlayingCardViewController *)viewController
{
    // this method can be removed if this ends up being the only line
    self = [super initWithFrame:frame withCard:card inContainer:viewController];
    return self;
}

@end
