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
@property (nonatomic) NSArray *placeDictArray;
@property (nonatomic) NSArray *alphabeticalArrayOfCountries;
@property (nonatomic) NSDictionary *citiesByCountry;


@end


@implementation TopPlacesObject

- (NSDictionary *)baseDict
{
    if (!_baseDict) {
        NSData *data = [NSData dataWithContentsOfURL:[FlickrFetcher URLforTopPlaces]];
        _baseDict = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
        NSLog(@"%@", _baseDict);
    }
    return _baseDict;
}

- (NSArray *)placeDictArray
{
    if (!_placeDictArray)
        _placeDictArray = [self.baseDict valueForKeyPath:FLICKR_RESULTS_PLACES];
    return _placeDictArray;
}

- (NSArray *)alphabeticalArrayOfCountries
{
    if (!_alphabeticalArrayOfCountries) {
        _alphabeticalArrayOfCountries = [[self.citiesByCountry allKeys]
                                         sortedArrayUsingSelector:@selector(localizedCaseInsensitiveCompare:)];
    }
    return _alphabeticalArrayOfCountries;
}

- (NSDictionary *)citiesByCountry
{
    if (!_citiesByCountry) {
        NSMutableDictionary *mutablePBC = [[NSMutableDictionary alloc] init];
        NSRegularExpression *parsePlace = [NSRegularExpression regularExpressionWithPattern:@"([^,]*),(.*,)?(.*$)" options:0 error:nil];
        for (NSDictionary *placeDict in self.placeDictArray) {
            NSString *placeName = placeDict[FLICKR_PLACE_NAME];
            NSLog(@"%@", placeName);
            NSTextCheckingResult *match = [parsePlace firstMatchInString:placeName options:0 range:NSMakeRange(0, [placeName length])];
            NSString *cityName = [placeName substringWithRange:[match rangeAtIndex:1]];
            NSLog(@"City: %@", cityName);
            NSRange etcRange = [match rangeAtIndex:2];
            NSString *etcName = @"";
            if (etcRange.length) {
                etcRange.length -= 1;
                etcName = [placeName substringWithRange:etcRange];
                NSLog(@"Etc: %@", etcName);
            }
            NSString *countryName = [placeName substringWithRange:[match rangeAtIndex:3]];
            NSLog(@"Country: %@", countryName);
            NSMutableArray *countryArray = [mutablePBC objectForKey:countryName];
            NSArray *arrayToInsert = @[cityName, etcName];
            if ([countryArray count]) {
                [countryArray addObject:arrayToInsert];
            } else {
                [mutablePBC setObject:[[NSMutableArray alloc] initWithArray:@[arrayToInsert]]
                               forKey:countryName];
            }
        }
        _citiesByCountry = [mutablePBC copy];
    }
    return _citiesByCountry;
}


@end
