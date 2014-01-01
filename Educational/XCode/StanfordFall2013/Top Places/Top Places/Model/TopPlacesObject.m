//
//  TopPlacesObject.m
//  Top Places
//
//  Created by Ethan Petuchowski on 12/31/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "TopPlacesObject.h"
#import "FlickrFetcher.h"


@interface TopPlacesObject ()

@property (nonatomic) NSDictionary *baseDict;
@property (nonatomic) NSArray *alphabeticalArrayOfCountries;


@end


@implementation TopPlacesObject

- (NSDictionary *)baseDict
{
    if (!_baseDict) {
        NSData *data = [NSData dataWithContentsOfURL:[FlickrFetcher URLforTopPlaces]];
        _baseDict = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
    }
    return _baseDict;
}

- (NSArray *)alphabeticalArrayOfCountries
{
    if (!_alphabeticalArrayOfCountries) {
        NSArray *placeDictArray = [self.baseDict valueForKeyPath:FLICKR_RESULTS_PLACES];
        NSMutableSet *countriesSet = [[NSMutableSet alloc] init];
        NSRegularExpression *getCountry = [NSRegularExpression regularExpressionWithPattern:@".*, (.*$)" options:0 error:nil];
        for (NSDictionary *placeDict in placeDictArray) {
            NSString *placeName = placeDict[FLICKR_PLACE_NAME];
            NSLog(@"%@", placeName);
            NSTextCheckingResult *match = [getCountry firstMatchInString:placeName options:0 range:NSMakeRange(0, [placeName length])];
            NSString *countryName = [placeName substringWithRange:[match rangeAtIndex:1]];
            NSLog(@"Country: %@", countryName);
            [countriesSet addObject:countryName];
        }
        NSLog(@"%@", countriesSet);
        _alphabeticalArrayOfCountries = [[countriesSet allObjects] sortedArrayUsingSelector:@selector(localizedCaseInsensitiveCompare:)];
    }
    return _alphabeticalArrayOfCountries;
}


@end
