//
//  BrowseTabViewController.m
//  Top Places
//
//  Created by Ethan Petuchowski on 12/30/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "BrowseTabViewController.h"
#import "TopPlacesObject.h"
#import "PlacePhotoListViewController.h"

@interface BrowseTabViewController ()

@property (nonatomic) TopPlacesObject *topPlacesObject;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *spinner;

@end

@implementation BrowseTabViewController

- (TopPlacesObject *)topPlacesObject
{
    if (!_topPlacesObject) _topPlacesObject = [[TopPlacesObject alloc] initWithController:self];
    return _topPlacesObject;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    // MAKE THE SPINNER START TO SPIN
    [self.spinner startAnimating];
    [self.topPlacesObject loadPlaceDictArray];
}

- (void)doneLoading
{
    [self.spinner stopAnimating];
    /* TODO: MAKE IT LOAD THE TABLE NOW */
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
                                          inSection:indexPath.section][@"subtitle"];
    return cell;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    return [self sectionNameOfNum:section];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // `sender` is the `UITableViewCell`
    if ([segue.destinationViewController isKindOfClass:[PlacePhotoListViewController class]]) {
        PlacePhotoListViewController *placeVC = segue.destinationViewController;
        NSIndexPath *indexPath = [self.tableView indexPathForCell:sender];
        placeVC.city = [self getDictForRow:indexPath.row
                             inSection:indexPath.section][@"city"];
        
        placeVC.place_id = [self getDictForRow:indexPath.row
                                     inSection:indexPath.section][@"place_id"];
        
    } else [NSException raise:@"Wrong VC" format:@"VC of class: %@",
                        [segue.destinationViewController class].description];
    
    
}

@end
