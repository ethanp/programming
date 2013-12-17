//
//  Game.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/13/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "MatchingGame.h"

@implementation MatchingGame


// need these to get rid of warnings, but I'm not sure why
@synthesize numCardsToMatch = _numCardsToMatch;
@synthesize score = _score;
@synthesize cards = _cards;
@synthesize chosenCards = _chosenCards;


static const int MISMATCH_PENALTY = 2;
static const int MATCH_BONUS = 6;
static const int COST_TO_CHOOSE = 1;


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



- (NSMutableArray *)chosenCards
{
    if (!_chosenCards) _chosenCards = [[NSMutableArray alloc] init];
    return _chosenCards;
}


- (void)markAllCardsAsMatched
{
    for (Card *card in self.chosenCards) {
        card.matched = YES;
    }
    [self.chosenCards removeAllObjects];
}


- (instancetype)initWithCardCount:(NSUInteger)count
                        usingDeck:(Deck *)deck
                        numCardsToMatch:(NSUInteger)numCards
{
    /* bc we're init'ing a subclass,
     note there's no alloc bc it's `self`
     and the caller has to call alloc first */
    self = [super init];
    if (self) {
        // note we're going to "match" on value XOR suit
        // because drawRandomCard actually removes the card
        for (int i = 0; i < count; i++) {
            Card *card = [deck drawRandomCard];
            [self.cards addObject:card]; /* calls constructor if necessary */
        }
        self.numCardsToMatch = numCards;
    }
    return self;
}



- (NSString *)chooseCardAtIndex:(NSUInteger)index
{
    NSMutableString *toRet = [[NSMutableString alloc] init];
    Card *card = [self cardAtIndex:index];
    if (!card.isMatched) {
        if (card.isChosen) {
            // if it's face-up, flip it back down
            card.chosen = NO; // setter has different name than getter (no reason?)
            [self.chosenCards removeObject:card];
        } else {  // match against other chosen cards
            card.chosen = YES;
            if ([self.chosenCards count] >= self.numCardsToMatch - 1) {
                int matchScore = [card match:self.chosenCards];
                [self.chosenCards addObject:card];
                if (matchScore) {
                    int scoreIncrease = matchScore * MATCH_BONUS / [self numCardsToMatch];
                    self.score += scoreIncrease;
                    [toRet appendString:@"Matched: "];
                    for (Card *turned in self.chosenCards) {
                        [toRet appendFormat:@" %@",turned.contents];
                    }
                    [self markAllCardsAsMatched];
                    NSString *plural = scoreIncrease > 1 ? @"s" : @"";
                    [toRet appendFormat:@"for %d point%@!", scoreIncrease, plural];
                } else {
                    // flip oldest card back over
                    Card *oldCard = [self.chosenCards objectAtIndex:0];
                    oldCard.chosen = NO;
                    [self.chosenCards removeObjectAtIndex:0];
                    [toRet appendFormat:@"No match, %d point penalty", MISMATCH_PENALTY];
                    self.score -= MISMATCH_PENALTY;
                }
            } else {
                [self.chosenCards addObject:card];
            }
            self.score -= COST_TO_CHOOSE;
        }
    }
    return [NSString stringWithString:toRet];
}

@end
