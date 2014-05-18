//
//  Tree.m
//  TreeGen
//
//  Created by Ethan Petuchowski on 5/18/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import "Tree.h"
#import "TreeViewController.h"
@interface Tree ()
@property (nonatomic) TreeViewController *treeVC;
@end

@implementation Tree
- (instancetype)initWithTreeVC:(TreeViewController *)treeVC
{
    if (self = [super init]) {
        _treeVC = treeVC;
        [self addNodeWithParent:nil atPoint:CGPointMake(300, 300)];
    }
    return self;
}

- (Tree *)addNodeWithParent:(EPNode *)parent atPoint:(CGPoint)center
{
    EPNode *newNode = [[EPNode alloc] initWithParent:parent];
    newNode.center = center;
    [_treeVC.treeView addSubview:newNode.nodeView];
    return self;
}
@end
