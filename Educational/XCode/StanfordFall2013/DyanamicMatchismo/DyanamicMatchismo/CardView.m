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
    if (gesture.state == UIGestureRecognizerStateRecognized) {
        [self.container cardWasChosen:self.card];
    }
}

// pretty sloppy
- (void)animateCardInsertion
{
    self.frame = CGRectMake(400, 400,
                            self.homeFrame.size.width,
                            self.homeFrame.size.height);
    [UIView animateWithDuration:0.5
                          delay:0.2
                        options:UIViewAnimationOptionCurveEaseInOut
                     animations:^{ self.frame = self.homeFrame; }
                     completion:nil];
    return;
}

// pretty sloppy
- (void)animateCardRemoval
{
    CGRect outframe = CGRectMake(400, 400,
                                 self.homeFrame.size.width,
                                 self.homeFrame.size.height);
    [UIView animateWithDuration:0.5
                          delay:0
                        options:UIViewAnimationOptionCurveEaseInOut
                     animations:^{ self.frame = outframe; }
                     completion:nil];
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
    
    [roundedRect addClip];  // parts of the path outside the rect will be clipped
    
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
    self.contentMode = UIViewContentModeRedraw;  // redraw everything when bounds change
}

- (id)initWithFrame:(CGRect)frame withCard:(Card *)card
        inContainer:(BaseViewController *)viewController
{
    self = [self initWithFrame:frame];
    // Initialization code
    self.card = card;
    self.container = viewController;
    self.homeFrame = frame;
    return self;
}

- (id)initWithFrame:(CGRect)frame
{
    /* Maybe I should initWithFrame:offScreenSomewhere
     * and save the initFrame as a @property CGRect realLocation
     * then animateCardInsertionToFrame:self.realLocation
     */
    self = [super initWithFrame:frame];
    UITapGestureRecognizer *tapRecognizer =
    [[UITapGestureRecognizer alloc] initWithTarget:self  // set handler (could be VC)
                                            action:@selector(handleTap:)]; // of handler
    
    // here, we could set tapRecognizer.numberOfTapsRequired/numberOfTouchesRequired

    [self addGestureRecognizer:tapRecognizer];
    [self setup];
    return self;
}

@end
