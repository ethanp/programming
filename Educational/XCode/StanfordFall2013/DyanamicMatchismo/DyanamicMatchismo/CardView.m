//
//  CardView.m
//  DyanamicMatchismo
//
//  Created by Ethan Petuchowski on 12/20/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "CardView.h"

@implementation CardView


- (void)handleTap:(UITapGestureRecognizer *)gesture
{
    if (gesture.state == UIGestureRecognizerStateEnded) {
        [self.container cardWasChosen:self.card];
    }
}

// TODO check out DROPIT in 'Lecture Code' for how to do this
- (void)animateCardInsertion
{
    return;
}

// TODO
- (void)animateCardRemoval
{
    return;
}

// Abstract
- (void)animateChooseCard {}


#pragma mark - Drawing

#define CORNER_FONT_STANDARD_HEIGHT 180.0
#define CORNER_RADIUS 12.0

- (CGFloat)cornerScaleFactor {
    return self.bounds.size.height / CORNER_FONT_STANDARD_HEIGHT;
}
- (CGFloat)cornerRadius { return CORNER_RADIUS * [self cornerScaleFactor]; }
- (CGFloat)cornerOffset { return [self cornerRadius] / 3.0; }


// Override `drawRect:` to perform custom drawing.
// If it's empty, leave it commented out.
// NEVER CALL it DIRECTLY
- (void)drawRect:(CGRect)rect
{
    UIBezierPath *roundedRect = [UIBezierPath
                                 bezierPathWithRoundedRect:self.bounds
                                 cornerRadius:[self cornerRadius]];
    
    [roundedRect addClip];
    
    [[UIColor whiteColor] setFill];
    UIRectFill(self.bounds);
    
    [[UIColor blackColor] setStroke];
    [roundedRect stroke];
}

#pragma mark - Initialization

- (void)setup
{
    self.backgroundColor = nil;
    self.opaque = NO;
    self.contentMode = UIViewContentModeRedraw;
}

- (id)initWithFrame:(CGRect)frame withCard:(Card *)card
        inContainer:(BaseViewController *)viewController
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        self.card = card;
        self.container = viewController;
    }
    return self;
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    [self addGestureRecognizer:[[UITapGestureRecognizer alloc]
                                initWithTarget:self
                                action:@selector(handleTap:)]];
    [self setup];
    return self;
}

- (void)awakeFromNib
{
    [self setup];
}

@end
