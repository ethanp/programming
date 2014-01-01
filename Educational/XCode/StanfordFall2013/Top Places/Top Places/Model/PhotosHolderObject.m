//
//  PhotosHolderObject.m
//  Top Places
//
//  Created by Ethan Petuchowski on 1/1/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import "PhotosHolderObject.h"
#import "FlickrFetcher.h"

@implementation PhotosHolderObject

// ABSTRACT
- (void)loadPhotos { [NSException raise:@"loadPhotos is Abstract" format:@""]; }

- (NSDictionary *)getInfoForPhotoNumber:(NSInteger)num
{
    NSDictionary *photoDict = self.photos[num];
    
    NSString *title = photoDict[FLICKR_PHOTO_TITLE];
    NSString *description = @"";
    if (!title) title = photoDict[FLICKR_PHOTO_DESCRIPTION];
    else  description = photoDict[FLICKR_PHOTO_DESCRIPTION];
    
    return @{@"title"    : (title ? title : @"unknown"),
             @"subtitle" : (description ? description : @"")};
}

@end
