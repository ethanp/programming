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

// TODO check out DROPIT in 'Lecture Code' for how to do this
- (void)animateCardInsertion
{
    self.hidden = self.card.isMatched;
    return;
}

// TODO
- (void)animateCardRemoval
{
    /* self.hidden means:
        1. it still has its place in the view hierarchy,
        2. it still belongs to the superview,
        3. it still lives in its frame,
        4. it is not on screen,
        5. it does not handle events.                   
     He said "you probably won't need this for the homework, but you could.  */
    
    self.hidden = self.card.isMatched;
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
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        self.card = card;
        self.container = viewController;
    }
    [self animateCardInsertion];
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
