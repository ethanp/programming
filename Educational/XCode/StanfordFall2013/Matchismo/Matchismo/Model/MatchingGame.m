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

- (NSInteger)numCardsToMatch
{
    if (!_numCardsToMatch) {
        _numCardsToMatch = 0;
    }
    return _numCardsToMatch;
}

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

/* Virtual Method
 * http://stackoverflow.com/questions/5222083/implement-a-pure-virtual-method-in-objective-c
 */
- (NSString *)chooseCardAtIndex:(NSUInteger)index
{
    NSAssert(NO, @"Subclasses need to override this method");
    return Nil;
}


- (instancetype)initWithCardCount:(NSUInteger)count
                        usingDeck:(Deck *)deck
{
    self = [super init];
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

@end
