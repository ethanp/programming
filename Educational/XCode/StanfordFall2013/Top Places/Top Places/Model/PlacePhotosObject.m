//
//  PlacePhotosObject.m
//  Top Places
//
//  Created by Ethan Petuchowski on 1/1/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import "PlacePhotosObject.h"
#import "FlickrFetcher.h"
#import "PlacePhotoListViewController.h"


@interface PlacePhotosObject ()

@property (nonatomic) PlacePhotoListViewController *parentController;

@end


@implementation PlacePhotosObject

#define NUM_PHOTOS 50

- (instancetype)initWithController:(PlacePhotoListViewController *)parentVC
{
    self = [super init];
    self.parentController = parentVC;
    return self;
}

- (void)loadPhotos
{
    NSURLRequest *request =
            [NSURLRequest requestWithURL:
                    [FlickrFetcher URLforPhotosInPlace:self.parentController.place_id
                                            maxResults:NUM_PHOTOS]];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:
                             [NSURLSessionConfiguration ephemeralSessionConfiguration]];
    
    NSURLSessionDownloadTask *task =
        [session downloadTaskWithRequest:request completionHandler:
             ^(NSURL *localFile, NSURLResponse *response, NSError *error) {
                 if (!error) {
                     NSData *data = [NSData dataWithContentsOfURL:localFile];
                     NSDictionary *baseDict = [NSJSONSerialization
                                               JSONObjectWithData:data
                                               options:0 error:nil];
                     self.photos = [baseDict valueForKeyPath:FLICKR_RESULTS_PHOTOS];
                     self.photos = [self.photos sortedArrayUsingComparator:
                          ^NSComparisonResult(id obj1, id obj2) {
                             NSString *t1 = ((NSDictionary *)obj1)[@"title"];
                             NSString *t2 = ((NSDictionary *)obj2)[@"title"];
                             return [t1 caseInsensitiveCompare:t2];
                     }];
                     NSLog(@"%@", self.photos);
                     dispatch_async(dispatch_get_main_queue(), ^{
                         [self.parentController doneLoading];
                     });
                 }
             }
         ];
    [task resume];
}



@end
