//
//  PhotosHolderObject.m
//  Top Places
//
//  Created by Ethan Petuchowski on 1/1/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import "PhotosHolderObject.h"
#import "FlickrFetcher.h"

@interface PhotosHolderObject ()

@property (nonatomic) PhotoListViewController *parentController;

@end

@implementation PhotosHolderObject

- (instancetype)initWithController:(PhotoListViewController *)parentVC
{
    self = [super init];
    self.parentController = parentVC;
    return self;
}

// ABSTRACT
- (void)loadPhotos { [NSException raise:@"loadPhotos is Abstract" format:@""]; }

- (void)loadCountPhotos:(NSInteger)num withURL:(NSURL *)url
{
    NSURLRequest *request =
    [NSURLRequest requestWithURL:
     [FlickrFetcher URLforPhotosInPlace:url
                             maxResults:num]];
    
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


- (NSDictionary *)getInfoForPhotoNumber:(NSInteger)num
{
    NSDictionary *photoDict = self.photos[num];
    
    NSString *title = photoDict[FLICKR_PHOTO_TITLE];
    NSString *sub = @"";
    if (!title) title = photoDict[FLICKR_PHOTO_DESCRIPTION];
    else          sub = photoDict[FLICKR_PHOTO_DESCRIPTION];
    
    if (!title || [title isEqualToString:@""]) title = @"Unknown";
    
    return @{@"title"    : title,
             @"subtitle" : (sub ? sub : @"")};
}

@end
