//
//  CardMatchingGame.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 11/12/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "CardMatchingGame.h"

@interface CardMatchingGame()

@property (nonatomic, strong) NSMutableArray *faceUpCards; // of Card
@property (nonatomic, strong) NSMutableArray *cards; // of Card [no type-parameters]

@end

@implementation CardMatchingGame

- (NSMutableArray *)faceUpCards
{
    if (!_faceUpCards) _faceUpCards = [[NSMutableArray alloc] init];
    return _faceUpCards;
}

- (instancetype)initWithCardCount:(NSUInteger)count
                        usingDeck:(Deck *)deck
{
    self = [super init]; /* bc we're init'ing a subclass, 
                          note there's no alloc bc it's `self`
                          and the caller has to call alloc first */
    self.numCardsToMatch = 3;
    if (self) {
        // note we're going to "match" on value XOR suit
        // because drawRandomCard actually removes the card
        for (int i = 0; i < count; i++) {
            Card *card = [deck drawRandomCard];
            [self.cards addObject:card]; /* calls constructor if necessary */
        }
    }
    
    return self;
}


static const int MISMATCH_PENALTY = 2;
static const int MATCH_BONUS = 6;
static const int COST_TO_CHOOSE = 1;

- (Card *)cardAtIndex:(NSUInteger)index
{
    // guard Error ArrayIndexOutOfBounds
    return (index < [self.cards count]) ? self.cards[index] : nil;
}


- (void)markAllCardsAsMatched
{
    for (Card *card in self.faceUpCards) {
        card.matched = YES;
    }
    [self.faceUpCards removeAllObjects];
}

- (NSString *)chooseCardAtIndex:(NSUInteger)index
{
    NSMutableString *toRet = [[NSMutableString alloc] init];
    Card *card = [self cardAtIndex:index];
    if (!card.isMatched) {
        if (card.isChosen) {
            // if it's face-up, flip it back down
            card.chosen = NO; // setter has different name than getter (no reason?)
            [self.faceUpCards removeObject:card];
        } else {  // match against other chosen cards
            card.chosen = YES;
            if ([self.faceUpCards count] >= self.numCardsToMatch - 1) {
                int matchScore = [card match:self.faceUpCards];
                [self.faceUpCards addObject:card];
                if (matchScore) {
                    int scoreIncrease = matchScore * MATCH_BONUS / [self numCardsToMatch];
                    self.score += scoreIncrease;
                    [toRet appendString:@"Matched: "];
                    for (Card *turned in self.faceUpCards) {
                        [toRet appendFormat:@" %@",turned.contents];
                    }
                    [self markAllCardsAsMatched]; // from assn2: ALL get removed
                    NSString *plural = scoreIncrease > 1 ? @"s" : @"";
                    [toRet appendFormat:@"for %d point%@!", scoreIncrease, plural];
                } else {
                    // flip oldest card back over
                    Card *oldCard = [self.faceUpCards objectAtIndex:0];
                    oldCard.chosen = NO;
                    [self.faceUpCards removeObjectAtIndex:0];
                    [toRet appendFormat:@"No match, %d point penalty", MISMATCH_PENALTY];
                    self.score -= MISMATCH_PENALTY;
                }
            } else {
                [self.faceUpCards addObject:card];
            }
            self.score -= COST_TO_CHOOSE;
        }
    }
    return [NSString stringWithString:toRet];
}

@end
