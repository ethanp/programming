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
#import "PhotoViewController.h"

@interface PlacePhotoListViewController ()

@property (nonatomic) PlacePhotosObject *photosHolder;

@end

@implementation PlacePhotoListViewController

#define NUM_PHOTOS 50

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self.photosHolder loadCountPhotos:NUM_PHOTOS withURL:
            [FlickrFetcher URLforPhotosInPlace:self.place_id maxResults:NUM_PHOTOS]];
}

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

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([segue.destinationViewController isKindOfClass:[PhotoViewController class]]) {
        PhotoViewController *photoVC = segue.destinationViewController;
        NSIndexPath *indexPath = [self.tableView indexPathForCell:sender];
        photoVC.title = nil;
        photoVC.imageURL =
                [FlickrFetcher URLforPhoto:[self.photosHolder photos][indexPath.row]
                                    format:FlickrPhotoFormatLarge];
    } else {
        [NSException raise:@"Wrong VC" format:@"VC of class: %@",
                [segue.destinationViewController class].description];
    }
}

@end
