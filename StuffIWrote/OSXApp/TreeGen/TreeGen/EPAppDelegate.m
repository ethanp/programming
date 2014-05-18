//
//  EPAppDelegate.m
//  TreeGen
//
//  Created by Ethan Petuchowski on 5/18/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import "EPAppDelegate.h"
#import "NodeView.h"

@interface NodeView ()
@end
@implementation EPAppDelegate

- (void)applicationDidFinishLaunching:(NSNotification *)aNotification
{
    // TODO put a root node as a NodeView into a good starting location in the window
    // honestly for now a nodeview needn't be anything more than
    // a box that let's you click on it to add a number
    
    // each row of nodes should have a specified y-range,
    //   this should dynamically adjust when necessary
    
    // location of children of nodes should be dynamically adjusted at all times
}

@end
