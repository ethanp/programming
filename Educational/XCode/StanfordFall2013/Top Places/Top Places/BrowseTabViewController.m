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

@end

@implementation BrowseTabViewController


- (void)viewDidLoad
{
    [super viewDidLoad];
    NSData *data = [NSData dataWithContentsOfURL:[FlickrFetcher URLforTopPlaces]];
    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                             options:0 error:nil];
    NSLog(@"%@", jsonDict);
    NSDictionary *placesDict = jsonDict[@"places"];
    NSLog(@"%@", placesDict);
    NSArray *placeArray = placesDict[@"place"];
    NSLog(@"%@", placeArray);
    NSDictionary *firstPlace = placeArray[0];
    NSLog(@"%@", firstPlace);
    NSString *placeString = firstPlace[@"_content"];
    NSLog(@"%@", placeString);
}


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
#warning Potentially incomplete method implementation.
    // Return the number of sections.
    return 0;
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
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier
                                                            forIndexPath:indexPath];
    
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
