//
//  Game.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/13/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "MatchingGame.h"
#import "Card.h"

@implementation MatchingGame


@synthesize cards = _cards;

// need these to get rid of warnings, but I'm not sure why
@synthesize numCardsToMatch = _numCardsToMatch;
@synthesize score = _score;

/* dynamic allocation for reference properties go in the getter */
- (NSMutableArray *)cards
{
    if (!_cards) _cards = [[NSMutableArray alloc] init];
    return _cards;
}


- (Card *)cardAtIndex:(NSUInteger)index
{
    // guard Error ArrayIndexOutOfBounds
    return (index < [self.cards count]) ? self.cards[index] : nil;
}

@end
