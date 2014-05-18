//
//  TreeViewController.h
//  TreeGen
//
//  Created by Ethan Petuchowski on 5/18/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import <Cocoa/Cocoa.h>
#import "TreeView.h"

@interface TreeViewController : NSViewController
@property (unsafe_unretained) IBOutlet NSTextView *generatedCodeView;
@property (weak) IBOutlet TreeView *treeView;
@end
