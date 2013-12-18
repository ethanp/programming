//
//  HistoryViewController.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/17/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "HistoryViewController.h"

@interface HistoryViewController ()
@property (weak, nonatomic) IBOutlet UITextView *historyTextView;

@end

@implementation HistoryViewController

- (void)viewDidLoad
{
    [super viewDidLoad];

 	// TODO Load the History onto the screen
    // from what's stored in the GameHistory for this View
    [self.historyTextView setText:Nil];
}

@end
