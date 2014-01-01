//
//  BrowseTabViewController.m
//  Top Places
//
//  Created by Ethan Petuchowski on 12/30/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "BrowseTabViewController.h"
#import "FlickrFetcher.h"

@interface BrowseTabViewController ()

@property (nonatomic) NSDictionary *topPlaces;

@end

@implementation BrowseTabViewController

- (NSDictionary *)topPlaces
{
    if (!_topPlaces) {
        NSData *data = [NSData dataWithContentsOfURL:[FlickrFetcher URLforTopPlaces]];
        _topPlaces = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
    }
    return _topPlaces;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
//    NSLog(@"%@", self.topPlaces);
//    NSDictionary *placesDict = self.topPlaces[@"places"];
//    NSLog(@"%@", placesDict);
//    NSArray *placeArray = placesDict[@"place"];
//    NSLog(@"%@", placeArray);
//    NSDictionary *firstPlace = placeArray[0];
//    NSLog(@"%@", firstPlace);
//    NSString *placeString = firstPlace[@"_content"];
//    NSLog(@"%@", placeString);
    [self numberOfSectionsInTableView:nil];
    
}


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    NSArray *placeDictArray = self.topPlaces[@"places"][@"place"];
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
    return [countriesSet count];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
#warning Incomplete method implementation.
    // Return the number of rows in the section.
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    // Configure the cell...
    
    return cell;
}

/*
#pragma mark - Navigation

// In a story board-based application,
// you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}

 */

@end
