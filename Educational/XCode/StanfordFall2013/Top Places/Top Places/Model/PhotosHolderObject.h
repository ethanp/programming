//
//  PhotosHolderObject.h
//  Top Places
//
//  Created by Ethan Petuchowski on 1/1/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "PhotoListViewController.h"

@interface PhotosHolderObject : NSObject

@property (nonatomic) NSArray *photos;

- (void)loadPhotos;
- (void)loadCountPhotos:(NSInteger)num withURL:(NSURL *)url;
- (NSDictionary *)getInfoForPhotoNumber:(NSInteger)num;
- (instancetype)initWithController:(PhotoListViewController *)parentVC;

@end
