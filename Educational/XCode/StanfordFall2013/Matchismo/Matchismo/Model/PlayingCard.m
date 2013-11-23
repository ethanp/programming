//
//  PlayingCard.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 11/12/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "PlayingCard.h"

@implementation PlayingCard

// could call [super match:...] but doesn't have to in this case
- (int)match:(NSArray *)otherCards
{
    int score = 0;
    
    for (PlayingCard *other in otherCards) {
        PlayingCard *otherCard = [otherCards firstObject];
        if (otherCard.rank == self.rank) {
            score += 4;
        } else if ([otherCard.suit isEqualToString:self.suit]) {
            score += 1;
        }
    }
    
    return score;
}

- (NSString *)contents
{
    NSArray *rankStrings = [PlayingCard rankStrings];
    return [rankStrings[self.rank] stringByAppendingString:self.suit];
    
}

+ (NSArray *)rankStrings
{
    return @[@"?",@"A",@"2",@"3",@"4",@"5",@"6",
             @"7",@"8",@"9",@"10",@"J",@"Q",@"K"];
}

+ (NSArray *)validSuits
{
    /* the @[] notation calls 
     * [[NSArray alloc] initWithObjects:obj_1,...,obj_n, Nil] */
    return @[@"♠", @"♣", @"♥", @"♦"];
}

// must @synthesize because we provide both setter AND getter
@synthesize suit = _suit;

- (void)setSuit:(NSString *)suit
{
    if ([[PlayingCard validSuits] containsObject:suit]) {
        _suit = suit;
    }
}

- (NSString *)suit
{
    /* this (NSString *) doesn't need to alloc-init bc
     * "you are setting your reference to the already existing object that the compiler
     *  creates from the hard-coded string. And you don't have to manage its memory because
     *  you didn't instantiate it." - StackOverflow
    http://stackoverflow.com/questions/637022/do-nsstring-objects-need-to-be-alloc-and-init
     */
    return _suit ? _suit : @"?";
}

+ (NSUInteger)maxRank
{
    return [[self rankStrings] count] - 1;
}

- (void)setRank:(NSUInteger)rank
{
    if (rank <= [PlayingCard maxRank]) {
        _rank = rank;
    }
}

@end
