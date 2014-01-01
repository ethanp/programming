//
//  BrowseTabViewController.m
//  Top Places
//
//  Created by Ethan Petuchowski on 12/30/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "BrowseTabViewController.h"
#import "TopPlacesObject.h"

@interface BrowseTabViewController ()

@property (nonatomic) TopPlacesObject *topPlacesObject;
@end

@implementation BrowseTabViewController

- (TopPlacesObject *)topPlacesObject
{
    if (!_topPlacesObject) _topPlacesObject = [[TopPlacesObject alloc] init];
    return _topPlacesObject;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self numberOfSectionsInTableView:nil];
}


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return [[self.topPlacesObject alphabeticalArrayOfCountries] count];
}

- (NSString *)sectionNameOfNum:(NSInteger)sectionNum
{
    return self.topPlacesObject.alphabeticalArrayOfCountries[sectionNum];
}

- (NSArray *)getRowDictsForSectionNum:(NSInteger)sectionNum
{
    NSString *countryName = [self sectionNameOfNum:sectionNum];
    return [self.topPlacesObject countryArrayForCountry:countryName];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [[self getRowDictsForSectionNum:section ] count];
}

- (NSDictionary *)getDictForRow:(NSInteger)row inSection:(NSInteger)section
{
    NSArray *rowDicts = [self getRowDictsForSectionNum:section];
    return rowDicts[row];
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"BrowseTab Cell"
                                                            forIndexPath:indexPath];
    cell.textLabel.text = [self getDictForRow:indexPath.row
                                    inSection:indexPath.section][@"city"];
    
    cell.detailTextLabel.text = [self getDictForRow:indexPath.row
                                          inSection:indexPath.section][@"etc"];
    return cell;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    return [self sectionNameOfNum:section];
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
