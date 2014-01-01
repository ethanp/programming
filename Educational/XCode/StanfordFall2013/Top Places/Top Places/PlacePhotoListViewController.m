//
//  PlacePhotoListViewController.m
//  Top Places
//
//  Created by Ethan Petuchowski on 12/31/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "PlacePhotoListViewController.h"
#import "FlickrFetcher.h"

@interface PlacePhotoListViewController ()

@property (nonatomic) NSURL *photosURL;  // try to abstract this to base-class

@end

@implementation PlacePhotoListViewController

#define NUM_PHOTOS_TO_DISPLAY 50

- (NSURL *)photosURL
{
    if (!_photosURL)
        _photosURL = [FlickrFetcher URLforPhotosInPlace:self.place_id
                                             maxResults:NUM_PHOTOS_TO_DISPLAY];
    return _photosURL;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.title = self.city;
}

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
