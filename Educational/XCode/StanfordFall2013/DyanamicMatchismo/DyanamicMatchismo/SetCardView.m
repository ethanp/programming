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

- (void)drawRect:(CGRect)rect
{
    [super drawRect:rect];
    // TODO draw the card face in here
    
    // differentiate it when it's chosen
    if (self.card.chosen) {} else {}
}


@end
