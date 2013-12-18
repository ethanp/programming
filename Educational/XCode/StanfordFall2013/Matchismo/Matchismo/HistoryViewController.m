//
//  HistoryViewController.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/17/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "HistoryViewController.h"
#import "GameHistory.h"

@interface HistoryViewController ()
@property (weak, nonatomic) IBOutlet UITextView *historyTextView;
@property (strong, nonatomic) GameHistory *history;

@end

@implementation HistoryViewController

- (GameHistory *)history
{
    if (!_history) _history = [[GameHistory alloc] init];
    return _history;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
 	// Load the History onto the screen
    // from what's stored in the GameHistory for this View
    [self.historyTextView setText:[self.history historyString]];
}

@end
