//
//  TopPlacesViewController.m
//  Top Places
//
//  Created by Ethan Petuchowski on 12/30/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "TopPlacesViewController.h"
#import "FlickrFetcher.h"

@interface TopPlacesViewController ()

@end

@implementation TopPlacesViewController

- (void)viewDidLoad
{
    [super viewDidLoad];

    /* This was just to get up and running */
    NSData *data = [NSData dataWithContentsOfURL:[FlickrFetcher URLforTopPlaces]];
    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                             options:0 error:nil];
    NSLog(@"%@", jsonDict);
}

@end
