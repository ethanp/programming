//
//  PlacePhotosObject.m
//  Top Places
//
//  Created by Ethan Petuchowski on 1/1/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import "PlacePhotosObject.h"
#import "FlickrFetcher.h"


@interface PlacePhotosObject ()

@property (nonatomic) NSURL *url;

@end

@implementation PlacePhotosObject

#define NUM_PHOTOS 50

- (NSURL *)url
{
    if (!_url)
        _url = [FlickrFetcher URLforPhotosInPlace:self.place_id maxResults:NUM_PHOTOS];
    return _url;
}

- (NSDictionary *)baseDict
{
    if (!_baseDict) {
        NSData *data = [NSData dataWithContentsOfURL:self.url];
        _baseDict = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
        NSLog(@"%@", _baseDict);
    }
    return _baseDict;
}

@end
