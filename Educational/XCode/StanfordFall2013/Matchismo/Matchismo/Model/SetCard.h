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
@property (nonatomic) NSString *color;  // could use an ENUM for this instead...
@property (nonatomic) NSString *fillType;  // could use an ENUM for this instead...

- (NSAttributedString *)attributedContents;
+ (NSArray *)validShapes;
+ (NSArray *)validColors;
+ (NSArray *)validFillTypes;

@end
