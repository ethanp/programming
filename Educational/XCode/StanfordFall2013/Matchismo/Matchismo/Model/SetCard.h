//
//  SetCard.h
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/5/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "Card.h"

@interface SetCard : Card

@property (strong, nonatomic) NSString *shape;
@property (nonatomic) NSUInteger color;  // not sure what type this should be yet
@property (nonatomic) NSUInteger fillingType;  // not sure what type this should be yet

+ (NSArray *)validShapes;
+ (NSArray *)validColors;
+ (NSArray *)validFillingTypes;

@end
