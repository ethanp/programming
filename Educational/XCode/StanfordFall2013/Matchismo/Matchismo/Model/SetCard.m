//
//  SetCard.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/5/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "SetCard.h"

@implementation SetCard

// could call [super match:...] but doesn't have to in this case
- (int)match:(NSArray *)otherCards
{
    int score = 0;
    
    for (SetCard *otherCard in otherCards) {

    }
    
    return score;
}

- (NSString *)contents
{
    return @"";  // TODO
}

+ (NSArray *)validShapes
{
    return @[@"◼︎", @"▲", @"☯"];
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
