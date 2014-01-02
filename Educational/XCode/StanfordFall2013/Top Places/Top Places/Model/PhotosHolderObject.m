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
