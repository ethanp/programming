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
@synthesize cardsInPlay = _cardsInPlay;
@synthesize deck = _deck;


/* dynamic allocation for reference properties go in the getter */
- (NSMutableArray *)cards
{
    if (!_cards) _cards = [[NSMutableArray alloc] init];
    return _cards;
}

- (NSMutableArray *)cardsInPlay
{
    if (!_cardsInPlay) {
        _cardsInPlay = [[NSMutableArray alloc] init];
        for (Card *card in self.cards) {
            if (!card.matched) {
                [_cardsInPlay addObject:card];
            }
        }
    }
    return _cardsInPlay;
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
{
    /* bc we're init'ing a subclass,
     note there's no alloc bc it's `self`
     and the caller has to call alloc first */
    self = [super init];
    if (self) {
        for (int i = 0; i < count; i++) {
            Card *card = [self.deck drawRandomCard];
            [self.cards addObject:card]; /* calls constructor if necessary */
        }
    }
    self.cardsInPlay = [self.cards mutableCopy];
    return self;
}

// method stub
- (NSAttributedString *)chooseCardAtIndex:(NSUInteger)index
{
    return Nil;
}


@end
