//
//  PhotoListViewController.m
//  Top Places
//
//  Created by Ethan Petuchowski on 12/31/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "PhotoListViewController.h"

@interface PhotoListViewController ()

@end

@implementation PhotoListViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
}

#pragma mark - Table view data source


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

// abstract
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    [NSException raise:@"Abstract Method" format:@"numberOfRows is Abstract"];
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"PhotoList Cell" forIndexPath:indexPath];
    
    // Configure the cell...
    
    /* Any list of photos should display the photo’s title as the table view cell’s
     title and its description as the table view cell’s subtitle. If the photo
     has no title, use its description as the title. If it has no title or
     description, use “Unknown” as the title. Flickr photo dictionary keys are
     #defined in FlickrFetcher.h. */
    
    cell.textLabel.text = @"TODO";
    cell.detailTextLabel.text = @"TODO TOO";
    
    return cell;
}

#pragma mark - Navigation

// In a story board-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}

@end
