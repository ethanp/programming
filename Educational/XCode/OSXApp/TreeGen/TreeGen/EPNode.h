//
//  EPNode.h
//  TreeGen
//
//  Created by Ethan Petuchowski on 5/18/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import <Cocoa/Cocoa.h>
#import "NodeView.h"
static const int RADIUS = 50;
static const int OUTER_RADIUS = RADIUS + 20;

@interface EPNode : NSObject

@property (nonatomic) NSInteger value;
@property (nonatomic) EPNode *parent;
@property (nonatomic) EPNode *left;
@property (nonatomic) EPNode *right;
@property (nonatomic) CGPoint center;
- (instancetype)initWithParent:(EPNode *)parent;
@property (nonatomic) NodeView *nodeView;
@end
