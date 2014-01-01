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

- (void)loadPhotos
{
    NSURLRequest *request =
            [NSURLRequest requestWithURL:
                    [FlickrFetcher URLforPhotosInPlace:self.place_id
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
                     NSLog(@"%@", self.photos);
                     
                     // TODO sort the photos for display
                     
                     dispatch_async(dispatch_get_main_queue(), ^{
                         [self.parentController doneLoading];
                     });
                 }
             }
         ];
    [task resume];
}



@end
