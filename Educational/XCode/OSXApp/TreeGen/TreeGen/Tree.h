//
//  Tree.h
//  TreeGen
//
//  Created by Ethan Petuchowski on 5/18/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "EPNode.h"
@class TreeViewController;

@interface Tree : NSObject
@property (nonatomic) NSArray *nodes;
@property (nonatomic) NSArray *edges;
- (instancetype)initWithTreeVC:(TreeViewController *)treeVC;
@end
