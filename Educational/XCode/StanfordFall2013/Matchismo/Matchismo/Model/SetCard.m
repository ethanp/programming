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
    NSMutableSet *chosenShapes = [NSMutableSet setWithObject:self.shape];
    NSMutableSet *chosenColors = [NSMutableSet setWithObject:self.color];
    NSMutableSet *chosenFills  = [NSMutableSet setWithObject:self.fillType];
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
- (NSAttributedString *)attributedContents
{
    
    NSDictionary *colorDict
             = @{@"Red": [UIColor redColor],
                 @"Blue": [UIColor blueColor],
                 @"Green": [UIColor greenColor]};
    
    NSDictionary *shapesDict
             = @{@"Square": @"◼︎",
                 @"Triangle": @"▲",
                 @"Circle": @"☯"};
    
    
    NSMutableDictionary *attributes =
        [@{NSForegroundColorAttributeName: colorDict[self.color]} mutableCopy];
    
    if ([self.fillType  isEqualToString: @"Backfilled"]) {
        [attributes addEntriesFromDictionary:@{NSBackgroundColorAttributeName: [UIColor grayColor]}];
    } else if ([self.fillType  isEqualToString: @"Outlined"]) {
        [attributes addEntriesFromDictionary:@{NSBackgroundColorAttributeName: [UIColor yellowColor]}];
    }
    
    return [[NSAttributedString alloc] initWithString:shapesDict[self.shape] attributes:attributes];
}

// these could be done with bit vectors and enums, but hooey
+ (NSArray *)validShapes
{
    return @[@"Square", @"Triangle", @"Circle"];
}

+ (NSArray *)validColors
{
    return @[@"Red", @"Blue", @"Green"];
}

+ (NSArray *)validFillTypes
{
    return @[@"Normal", @"Outlined", @"Backfilled"];
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
