//
//  CardMatchingGame.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 11/12/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "CardMatchingGame.h"

@interface CardMatchingGame()
@end

@implementation CardMatchingGame


static const int MISMATCH_PENALTY = 2;
static const int MATCH_BONUS = 12;
static const int COST_TO_CHOOSE = 1;


// TODO return (void)?
- (NSAttributedString *)chooseCardAtIndex:(NSUInteger)index
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
                NSMutableString *cardsContents = [[NSMutableString alloc] init];
                for (Card *turned in self.chosenCards) {
                    [cardsContents appendString:[turned contents]];
                }
                if (matchScore) {
                    int scoreIncrease = matchScore * MATCH_BONUS / [self numCardsToMatch];
                    self.score += scoreIncrease;
                    [toRet appendString:@"Matched: "];
                    [toRet appendString:cardsContents];
                    [self markAllCardsAsMatched];
                    NSString *plural = scoreIncrease > 1 ? @"s" : @"";
                    [toRet appendFormat:@"for %d point%@!", scoreIncrease, plural];
                } else {
                    // flip oldest card back over
                    Card *oldCard = [self.chosenCards objectAtIndex:0];
                    oldCard.chosen = NO;
                    [self.chosenCards removeObjectAtIndex:0];
                    [toRet appendString:cardsContents];
                    [toRet appendFormat:@"don't match, %d point penalty", MISMATCH_PENALTY];
                    self.score -= MISMATCH_PENALTY;
                }
            } else {
                [self.chosenCards addObject:card];
            }
            self.score -= COST_TO_CHOOSE;
        }
    }
    NSAttributedString *nsas = [[NSAttributedString alloc] initWithString:[toRet copy]];
    return nsas;
}

@end
