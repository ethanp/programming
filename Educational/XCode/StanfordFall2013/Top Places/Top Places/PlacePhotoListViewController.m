//
//  PlacePhotoListViewController.m
//  Top Places
//
//  Created by Ethan Petuchowski on 12/31/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "PlacePhotoListViewController.h"
#import "FlickrFetcher.h"
#import "PlacePhotosObject.h"

@interface PlacePhotoListViewController ()

@property (nonatomic) PlacePhotosObject *photosHolder;

@end

@implementation PlacePhotoListViewController

- (PlacePhotosObject *)photosHolder
{
    if (!_photosHolder) _photosHolder = [[PlacePhotosObject alloc]
                                         initWithController:self];
    return  _photosHolder;
}

#pragma mark - Table view data source

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [[self.photosHolder photos] count];
}

#pragma mark - Navigation

// In a story board-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}

@end
