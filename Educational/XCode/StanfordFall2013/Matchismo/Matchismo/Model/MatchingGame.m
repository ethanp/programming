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

// method stub
- (NSAttributedString *)chooseCardAtIndex:(NSUInteger)index
{
    return Nil;
}


@end
