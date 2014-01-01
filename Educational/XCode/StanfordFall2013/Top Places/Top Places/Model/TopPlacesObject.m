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
@property (nonatomic) NSDictionary *countryDict;

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
        _alphabeticalArrayOfCountries = [[self.countryDict allKeys]
                                         sortedArrayUsingSelector:
                                         @selector(caseInsensitiveCompare:)];
    }
    return _alphabeticalArrayOfCountries;
}

- (NSDictionary *)countryDict
{
    if (!_countryDict) {
        NSMutableDictionary *mutablePBC = [[NSMutableDictionary alloc] init];
        NSRegularExpression *parsePlace = [NSRegularExpression
                                           regularExpressionWithPattern:@"([^,]*), (.*, )?(.*$)"
                                           options:0 error:nil];
        for (NSDictionary *placeDict in self.placeDictArray) {
            NSString *placeName = placeDict[FLICKR_PLACE_NAME];
            NSTextCheckingResult *match = [parsePlace
                                           firstMatchInString:placeName options:0
                                           range:NSMakeRange(0, [placeName length])];
            NSString *cityName = [placeName substringWithRange:[match rangeAtIndex:1]];
            NSRange etcRange = [match rangeAtIndex:2];
            NSString *etcName = @"";
            if (etcRange.length) {
                etcRange.length -= 2;
                etcName = [placeName substringWithRange:etcRange];
            }
            NSString *countryName = [placeName substringWithRange:[match rangeAtIndex:3]];
            NSLog(@"%@", countryName);
            NSMutableArray *countryArray = mutablePBC[countryName];
            NSDictionary *dictToInsert = @{@"city" : cityName,
                                           @"etc"  : etcName};
            if ([countryArray count]) {
                [countryArray addObject:dictToInsert];
            } else {
                [mutablePBC setObject:[[NSMutableArray alloc] initWithArray:@[dictToInsert]]
                               forKey:countryName];
            }
            NSLog(@"%@", countryArray);
        }
        for (__strong NSArray *countryArray in [mutablePBC allValues]) {
            countryArray = [countryArray
                            sortedArrayUsingComparator:
                            ^NSComparisonResult(id obj1, id obj2) {
                                NSString *city1 = ((NSDictionary *)obj1)[@"city"];
                                NSString *city2 = ((NSDictionary *)obj2)[@"city"];
                                return [city1 caseInsensitiveCompare:city2];
                            }];
        }
        _countryDict = [mutablePBC copy];
    }
    return _countryDict;
}

- (NSArray *)countryArrayForCountry:(NSString *)country;
{
    return self.countryDict[country];
}

@end
