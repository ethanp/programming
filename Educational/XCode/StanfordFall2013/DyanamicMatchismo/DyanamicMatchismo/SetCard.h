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
@property (nonatomic) NSString *color;
@property (nonatomic) NSString *fillType;
@property (nonatomic) NSDictionary *colorDict;
@property (nonatomic) NSNumber *number;


- (NSAttributedString *)attributedContents;
+ (NSArray *)validShapes;
+ (NSArray *)validColors;
+ (NSArray *)validFillTypes;
+ (NSDictionary *)shapesDict;


@end
