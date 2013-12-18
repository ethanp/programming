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
                if (matchScore) {
                    int scoreIncrease = matchScore * MATCH_BONUS;
                    self.score += scoreIncrease;
                    [toRet appendAttributedString:[[NSAttributedString alloc] initWithString: @"Matched: "]];
                    for (Card *turned in self.chosenCards) {
                        [toRet appendAttributedString:[turned attributedContents]];
                    }
                    [self markAllCardsAsMatched];
                    NSString *plural = scoreIncrease > 1 ? @"s" : @"";

                    NSString *pointString = [[NSString alloc]
                                             initWithFormat:@" for %d point%@!", scoreIncrease, plural];
                    
                    [toRet appendAttributedString:[[NSAttributedString alloc]
                                                   initWithString:pointString]];
                } else {
                    // flip oldest card back over
                    Card *oldCard = [self.chosenCards objectAtIndex:0];
                    oldCard.chosen = NO;
                    [self.chosenCards removeObjectAtIndex:0];

                    NSString *noPointMessage = [[NSString alloc]
                                                initWithFormat:@"No match, %d point penalty", MISMATCH_PENALTY];
                    
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
    if ([toRet length]) {
        NSLog(@"Adding \"%@\" to history", [toRet string]);
        [self.history addLine:toRet];
    }
    return toRet;
}

@end
