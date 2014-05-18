//
//  EdgeView.h
//  TreeGen
//
//  Created by Ethan Petuchowski on 5/18/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import <Cocoa/Cocoa.h>
#import "EPNode.h"

@interface EdgeView : NSView

@property (nonatomic) EPNode *parent;
@property (nonatomic) EPNode *child;

@end
