//
//  TreeView.m
//  TreeGen
//
//  Created by Ethan Petuchowski on 5/18/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import "TreeView.h"

@implementation TreeView

- (id)initWithFrame:(NSRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        _color = [NSColor lightGrayColor];
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
