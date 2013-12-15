//
//  SetCard.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/5/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "SetCard.h"

@implementation SetCard

/* could call [super match:...] but doesn't have to in this case */
- (int)match:(NSArray *)otherCards
{
    int matchScore = 0;
    NSMutableSet *chosenShapes = [@[self.shape] mutableCopy];
    NSMutableSet *chosenColors = [@[self.color] mutableCopy];
    NSMutableSet *chosenFills  = [@[self.fillType] mutableCopy];
    for (SetCard *otherCard in otherCards) {
        [chosenShapes addObject:otherCard.shape];
        [chosenColors addObject:otherCard.color];
        [chosenFills addObject:otherCard.fillType];
    }
    
    if (([chosenShapes count] == 3 || [chosenShapes count] == 1) &&
        ([chosenColors count] == 3 || [chosenColors count] == 1) &&
        ([chosenFills count]  == 3 || [chosenFills count]  == 1))
    {
        matchScore = 1;
    }
    
    return matchScore;
}

/* this should probably be a string with formatting */
- (NSString *)contents
{
    return @"";  // TODO
}

+ (NSArray *)validShapes
{
    return @[@"◼︎", @"▲", @"☯"];
}

+ (NSArray *)validColors
{
    return @[@"Red", @"Blue", @"Green"];
}

+ (NSArray *)validFillTypes
{
    return @[@"Solid", @"Striped", @"Unfilled"];
}

// must @synthesize properties if we provide both setter AND getter
@synthesize shape = _shape;

- (void)setShape:(NSString *)shape
{
    if ([[SetCard validShapes] containsObject:shape]) {
        _shape = shape;
    }
}

- (NSString *)shape
{
    return _shape ? _shape : @"?";
}

@end
