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

@property (nonatomic) NSArray *placeDictArray;
@property (nonatomic) NSDictionary *countryDict;

@end


@implementation TopPlacesObject

- (void)loadPlaceDictArray
{
    NSURLRequest *request = [NSURLRequest requestWithURL:[FlickrFetcher URLforTopPlaces]];
    NSURLSession *session = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration ephemeralSessionConfiguration]];
    NSURLSessionDownloadTask *task = [session downloadTaskWithRequest:request completionHandler:^(NSURL *location, NSURLResponse *response, NSError *error) {
        NSData *data = [NSData dataWithContentsOfURL:[FlickrFetcher URLforTopPlaces]];
        NSDictionary *baseDict = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
        NSLog(@"%@", baseDict);
        dispatch_async(dispatch_get_main_queue(), ^{
            self.placeDictArray = [baseDict valueForKeyPath:FLICKR_RESULTS_PLACES];
        });
    }];
    [task resume];
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

        /* The fastest way I can come up with is inserting the cities into
           a `tree` attached to the country, so that they can be retrieved sorted
           by doing an in-order traversal of that tree. This ends up being rougly
           logn+nlogn, and I believe what I've got is roughly n+nlogn for insertion and
           one removal, so it's better than 1/2 as fast + way simpler. Optimization is
           not justified. */
        
        NSMutableDictionary *mutablePBC = [[NSMutableDictionary alloc] init];
        NSRegularExpression *parsePlace = [NSRegularExpression
                                           regularExpressionWithPattern:@"([^,]*), (.*, )?(.*$)"
                                           options:0 error:nil];
        
        // add array of cities to each country
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
                                           @"subtitle"  : etcName,
                                           @"place_id" : placeDict[FLICKR_PLACE_ID]};
            if ([countryArray count]) {
                [countryArray addObject:dictToInsert];
            } else {
                [mutablePBC setObject:[[NSMutableArray alloc] initWithArray:@[dictToInsert]]
                               forKey:countryName];
            }
            NSLog(@"%@", countryArray);
        }

        // sort array of each country by city name
        for (NSString *countryName in [mutablePBC allKeys]) {
            NSArray *countryArray = mutablePBC[countryName];
            NSArray *sortedCountryArray =
                        [countryArray sortedArrayUsingComparator:
                               ^NSComparisonResult(id obj1, id obj2) {
                                   NSString *city1 = ((NSDictionary *)obj1)[@"city"];
                                   NSString *city2 = ((NSDictionary *)obj2)[@"city"];
                                   return [city1 caseInsensitiveCompare:city2];
                               }];
            [mutablePBC setValue:sortedCountryArray forKey:countryName];
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
