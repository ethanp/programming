//
//  EPAppDelegate.h
//  TreeGen
//
//  Created by Ethan Petuchowski on 5/18/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import <Cocoa/Cocoa.h>
#import "TreeViewController.h"

@interface EPAppDelegate : NSObject <NSApplicationDelegate>

@property (assign) IBOutlet NSWindow *window;
@property (nonatomic) TreeViewController *treeVC;
@end
