//
//  NodeView.m
//  TreeGen
//
//  Created by Ethan Petuchowski on 5/18/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import "NodeView.h"

@implementation NodeView

- (id)initWithFrame:(NSRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        _color = [NSColor blueColor];
    }
    return self;
}

- (void)drawRect:(NSRect)dirtyRect {
    // set any NSColor for filling, say white:
    [_color setFill];
    NSRectFill(dirtyRect);
    [super drawRect:dirtyRect];
}

@end
