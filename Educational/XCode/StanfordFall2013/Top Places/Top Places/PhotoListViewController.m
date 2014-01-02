//
//  PhotoListViewController.m
//  Top Places
//
//  Created by Ethan Petuchowski on 12/31/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "PhotoListViewController.h"
#import "PlacePhotosObject.h"

@interface PhotoListViewController ()

@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *spinner;
@property (nonatomic) PhotosHolderObject *photosHolder;

@end


@implementation PhotoListViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self.spinner startAnimating];
}

- (void)doneLoading
{
    [self.spinner stopAnimating];
    [self.tableView reloadData];
}

#pragma mark - Table view data source


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

// ABSTRACT
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{ [NSException raise:@"Abstract Method" format:@"numberOfRows is Abstract"]; return 0; }

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell =
            [tableView dequeueReusableCellWithIdentifier:@"PhotoList Cell"
                                            forIndexPath:indexPath];
    
    NSDictionary *cellDict = [self.photosHolder getInfoForPhotoNumber:indexPath.row];
    cell.textLabel.text = cellDict[@"title"];
    cell.detailTextLabel.text = cellDict[@"subtitle"];
    return cell;
}

#pragma mark - Navigation

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}

@end
