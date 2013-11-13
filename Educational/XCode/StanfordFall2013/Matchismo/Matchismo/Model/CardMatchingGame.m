//
//  CardMatchingGame.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 11/12/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "CardMatchingGame.h"

@interface CardMatchingGame()
// we can make it readwrite /here/ because this is the /implementation/
@property (nonatomic, readwrite) NSInteger score;
@property (nonatomic, strong) NSMutableArray *cards; // of Card (no type-parameters)
@end

@implementation CardMatchingGame

- (NSMutableArray *)cards
{
    if (!_cards) _cards = [[NSMutableArray alloc] init];
    return _cards;
}

- (instancetype)initWithCardCount:(NSUInteger)count
                        usingDeck:(Deck *)deck
{
    self = [super init]; // bc we're init'ing a subclass
    
    if (self) {
        // note we're not adding 2 of each card we have
        // i.e. we're going to "match" on value XOR suit
        for (int i = 0; i < count; i++) {
            Card *card = [deck drawRandomCard];
            [self.cards addObject:card];
        }
    }
    
    return self;
}


static const int MISMATCH_PENALTY = 2;
static const int MATCH_BONUS = 4;
static const int COST_TO_CHOOSE = 1;



- (Card *)cardAtIndex:(NSUInteger)index
{
    // guard Error ArrayIndexOutOfBounds
    return (index < [self.cards count]) ? self.cards[index] : nil;
}

- (void)chooseCardAtIndex:(NSUInteger)index
{
    Card *card = [self cardAtIndex:index];
    
    if (!card.isMatched) {
        if (card.isChosen) {
            // if it's face-up, flip it back down
            card.chosen = NO; // setter has different name than getter (no reason?)
        } else {
            // match against other chosen cards
            for (Card *otherCard in self.cards) {
                if (otherCard.isChosen && !otherCard.isMatched) {
                    // TODO doesn't /have/ to be one card in the array ...
                    // get the /particular/ score of having matched these two particular cards
                    int matchScore = [card match:@[otherCard]];
                    if (matchScore) {
                        self.score += matchScore * MATCH_BONUS;
                        otherCard.matched = YES;
                        card.matched = YES;
                    } else {
                        self.score -= MISMATCH_PENALTY;
                        otherCard.chosen = NO; // flip it back over
                    }
                    break; // can only choose 2 cards for now
                }
            }
            self.score -= COST_TO_CHOOSE;
            card.chosen = YES;
        }
    }
}

@end
