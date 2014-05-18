//
//  EPNode.m
//  TreeGen
//
//  Created by Ethan Petuchowski on 5/18/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import "EPNode.h"


@implementation EPNode

- (instancetype)initWithParent:(EPNode *)parent
{
    if (self = [self init]) {
        _parent = parent;
        _nodeView = [[NodeView alloc] initWithFrame:NSMakeRect(10, 10, OUTER_RADIUS, OUTER_RADIUS)];
    }
    return self;
}

- (void)setCenter:(CGPoint)center
{
    _center = center;
    _nodeView.frame = NSMakeRect(center.x - OUTER_RADIUS,
                                 center.y - OUTER_RADIUS,
                                 OUTER_RADIUS, OUTER_RADIUS);
}

@end
