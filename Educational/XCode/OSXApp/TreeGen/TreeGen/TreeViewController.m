//
//  TreeViewController.m
//  TreeGen
//
//  Created by Ethan Petuchowski on 5/18/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import "TreeViewController.h"
#import "Tree.h"

@interface TreeViewController ()
@property Tree *tree;
@end

@implementation TreeViewController

- (IBAction)generateCodeButtonPressed:(id)sender {
    [_generatedCodeView setString:[[_generatedCodeView string] stringByAppendingString:@"Something"]];
}

- (void)awakeFromNib {
    _tree = [[Tree alloc] initWithTreeVC:self];
}

@end
