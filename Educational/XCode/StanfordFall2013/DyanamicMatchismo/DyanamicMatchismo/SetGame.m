//
//  SetGame.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/5/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "SetGame.h"

@interface SetGame()
@end

@implementation SetGame

static const int MISMATCH_PENALTY = 2;
static const int MATCH_BONUS = 16;
static const int COST_TO_CHOOSE = 1;
static const int NUM_CARDS_TO_START = 12;
static const int NUM_CARDS_TO_MATCH = 3;


@synthesize deck = _deck;

- (SetCardDeck *)deck
{
    if (!_deck) _deck = [[SetCardDeck alloc] init];
    return _deck;
}

- (instancetype)init
{
    self = [super initWithCardCount:NUM_CARDS_TO_START];
    self.numCardsToMatch = NUM_CARDS_TO_MATCH;
    return self;
}

- (Card *)addCardToGame
{
    return [super addCardToGame];
}

- (NSAttributedString *)chooseCardAtIndex:(NSUInteger)index
{
    NSMutableAttributedString *toRet = [[NSMutableAttributedString alloc] init];
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
                NSMutableAttributedString *cardsContents = [[NSMutableAttributedString
                                                             alloc] init];
                for (Card *turned in self.chosenCards) {
                    [cardsContents appendAttributedString:[turned attributedContents]];
                }
                if (matchScore) {
                    int scoreIncrease = matchScore * MATCH_BONUS;
                    self.score += scoreIncrease;
                    [toRet appendAttributedString:[[NSAttributedString alloc]
                                                   initWithString: @"Matched: "]];
                    [toRet appendAttributedString:cardsContents];
                    [self markAllCardsAsMatched];
                    NSString *plural = scoreIncrease > 1 ? @"s" : @"";

                    NSString *pointString = [[NSString alloc]
                                             initWithFormat:@" for %d point%@!",
                                             scoreIncrease, plural];
                    
                    [toRet appendAttributedString:[[NSAttributedString alloc]
                                                   initWithString:pointString]];
                } else {
                    // flip oldest card back over
                    Card *oldCard = [self.chosenCards objectAtIndex:0];
                    oldCard.chosen = NO;
                    [self.chosenCards removeObjectAtIndex:0];
                    NSString *noPointMessage = [[NSString alloc]
                                                initWithFormat:@"don't match, %d point penalty",
                                                MISMATCH_PENALTY];

                    [toRet appendAttributedString:cardsContents];
                    
                    [toRet appendAttributedString:[[NSAttributedString alloc]
                                                   initWithString:noPointMessage]];
                    
                    self.score -= MISMATCH_PENALTY;
                }
            } else {
                [self.chosenCards addObject:card];
            }
            self.score -= COST_TO_CHOOSE;
        }
    }
    return toRet;
}

@end
