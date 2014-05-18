//
//  NodeView.m
//  TreeGen
//
//  Created by Ethan Petuchowski on 5/18/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import "NodeView.h"
#import "EPNode.h"

static const NSInteger NODE_OUTER_LINE_WIDTH = 7;

@implementation NodeView

- (id)initWithFrame:(NSRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        _color = [NSColor blueColor];
    }
    return self;
}

// required to receive mouse events
// developer.apple.com/library/mac/documentation/Cocoa/Conceptual/EventOverview/EventHandlingBasics/EventHandlingBasics.html

- (BOOL)acceptsFirstResponder {
    return YES;
}

- (void)drawRect:(NSRect)dirtyRect {
    
    /* TODO it should draw a big number inside itself */
    
    // Get the graphics context that we are currently executing under
    NSGraphicsContext* gc = [NSGraphicsContext currentContext];
    
    // Save the current graphics context settings
    [gc saveGraphicsState];
    
    // Set the color in the current graphics context for future draw operations
    [[NSColor blackColor] setStroke];
    [[NSColor yellowColor] setFill];
    
    // Create our circle path
    NSRect rect = NSMakeRect(10, 10, RADIUS, RADIUS);
    NSBezierPath* circlePath = [NSBezierPath bezierPath];
    [circlePath appendBezierPathWithOvalInRect: rect];
    
    // Outline and fill the path
    [circlePath setLineWidth:NODE_OUTER_LINE_WIDTH];
    [circlePath stroke];
    [circlePath fill];
    
    // Restore the context to what it was before we messed with it
    [gc restoreGraphicsState];
}

- (void)mouseDown:(NSEvent *)theEvent {
    // mouseInCloseBox and trackingCloseBoxHit are instance variables
    
    /* sample method */
    
//    if (mouseInCloseBox = NSPointInRect([self convertPoint:[theEvent locationInWindow] fromView:nil], closeBox)) {
//        trackingCloseBoxHit = YES;
//        [self setNeedsDisplayInRect:closeBox];
//    }
//    else if ([theEvent clickCount] > 1) {
//        [[self window] miniaturize:self];
//        return;
//    }
}

- (void)mouseDragged:(NSEvent *)theEvent {
    
    /* sample method */

//    NSPoint windowOrigin;
//    NSWindow *window = [self window];
//    
//    if (trackingCloseBoxHit) {
//        mouseInCloseBox = NSPointInRect([self convertPoint:[theEvent locationInWindow] fromView:nil], closeBox);
//        [self setNeedsDisplayInRect:closeBox];
//        return;
//    }
//    
//    windowOrigin = [window frame].origin;
//    
//    [window setFrameOrigin:NSMakePoint(windowOrigin.x + [theEvent deltaX], windowOrigin.y - [theEvent deltaY])];
}

- (void)mouseUp:(NSEvent *)theEvent {
    
    /* sample method */
    
//    if (NSPointInRect([self convertPoint:[theEvent locationInWindow] fromView:nil], closeBox)) {
//        [self tryToCloseWindow];
//        return;
//    }
//    trackingCloseBoxHit = NO;
//    [self setNeedsDisplayInRect:closeBox];
    
}

@end
