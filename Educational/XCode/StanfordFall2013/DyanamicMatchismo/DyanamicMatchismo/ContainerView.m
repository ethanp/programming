//
//  ContainerView.m
//  DyanamicMatchismo
//
//  Created by Ethan Petuchowski on 12/23/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "ContainerView.h"

@interface ContainerView ()
- (void)handlePinch:(UIPinchGestureRecognizer *)gesture;
@end

@implementation ContainerView

// TODO
- (void)handlePinch:(UIPinchGestureRecognizer *)gesture
{
    return;
}

- (void)awakeFromNib
{
    UIPinchGestureRecognizer *pinchRecognizer =
    [[UIPinchGestureRecognizer alloc] initWithTarget:self
                                              action:@selector(handlePinch:)];
    
    [self addGestureRecognizer:pinchRecognizer];
}

@end
