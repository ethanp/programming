//
//  TopPlacesObject.h
//  Top Places
//
//  Created by Ethan Petuchowski on 12/31/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface TopPlacesObject : NSObject

@property (nonatomic) NSArray *alphabeticalArrayOfCountries;

- (NSArray *)countryArrayForCountry:(NSString *)country;
- (void)loadPlaceDictArray;
- (TopPlacesObject *)initWithController:(UIViewController *)viewController;

@end
