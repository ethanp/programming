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

@property (nonatomic) PlacePhotosObject *placePhotosObject;

@end

@implementation PlacePhotoListViewController

#define NUM_PHOTOS_TO_DISPLAY 50

- (PlacePhotosObject *)placePhotosObject
{
    if (!_placePhotosObject) {
        _placePhotosObject = [[PlacePhotosObject alloc] init];
        _placePhotosObject.place_id = self.place_id;
    }
    return _placePhotosObject;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
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
