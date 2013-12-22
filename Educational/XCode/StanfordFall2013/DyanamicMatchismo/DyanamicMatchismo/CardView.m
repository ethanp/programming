//
//  CardView.m
//  DyanamicMatchismo
//
//  Created by Ethan Petuchowski on 12/20/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "CardView.h"

@implementation CardView

- (id)initWithFrame:(CGRect)frame withCard:(Card *)card inContainer:(BaseViewController *)viewController
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        self.card = card;
        self.container = viewController;
    }
    return self;
}

- (void)handleTap:(UITapGestureRecognizer *)gesture // abstract
{
    return;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
