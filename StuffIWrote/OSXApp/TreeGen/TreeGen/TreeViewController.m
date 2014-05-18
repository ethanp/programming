//
//  TreeViewController.m
//  TreeGen
//
//  Created by Ethan Petuchowski on 5/18/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import "TreeViewController.h"

@interface TreeViewController ()

@end

@implementation TreeViewController

- (IBAction)generateCodeButtonPressed:(id)sender {
    [_generatedCodeView setString:[[_generatedCodeView string] stringByAppendingString:@"Something"]];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Initialization code here.
    }
    return self;
}

@end
