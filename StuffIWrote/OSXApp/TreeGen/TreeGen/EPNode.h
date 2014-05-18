//
//  EPNode.h
//  TreeGen
//
//  Created by Ethan Petuchowski on 5/18/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import <Cocoa/Cocoa.h>

@interface EPNode : NSObject

@property (nonatomic) NSInteger value;
@property (nonatomic) EPNode *left;
@property (nonatomic) EPNode *right;

@end
