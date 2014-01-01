//
//  HistoryTabViewController.m
//  Top Places
//
//  Created by Ethan Petuchowski on 12/30/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "HistoryTabViewController.h"

@interface HistoryTabViewController ()

@end

@implementation HistoryTabViewController

#define NUM_PHOTOS_TO_DISPLAY 20

#pragma mark - Table view data source

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return NUM_PHOTOS_TO_DISPLAY;
}

#pragma mark - Navigation

// In a story board-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}

@end
