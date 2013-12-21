//
//  Game.h
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/13/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Card.h"
#import "Deck.h"

@interface MatchingGame : NSObject
{
    // http://stackoverflow.com/questions/575210/use-of-synthesize-property-in-objective-c-inheritance
    NSInteger score;
    int numCardsToMatch;
    NSMutableArray *cards;
    NSMutableArray *chosenCards;
    NSMutableArray *cardsInPlay;
}


@property (nonatomic, readwrite) NSInteger score;
@property (nonatomic, readwrite) int numCardsToMatch;
@property (nonatomic, strong) NSMutableArray *cards; // of Card [no type-parameters]
@property (nonatomic, strong) NSMutableArray *chosenCards; // of Card
@property (nonatomic, strong) NSMutableArray *cardsInPlay; // of Card

- (Card *)cardAtIndex:(NSUInteger)index;

/* designated initializer
 * must be called from any of our other initializers
 * or from any subclass's initializers */
- (instancetype)initWithCardCount:(NSUInteger)count
                        usingDeck:(Deck *)deck
                        numCardsToMatch:(NSUInteger)numCards;

- (void)markAllCardsAsMatched;

- (NSAttributedString *)chooseCardAtIndex:(NSUInteger)index;

@end
