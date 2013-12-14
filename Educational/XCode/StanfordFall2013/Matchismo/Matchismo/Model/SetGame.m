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
static const int MATCH_BONUS = 6;
static const int COST_TO_CHOOSE = 1;

- (instancetype)initWithCardCount:(NSUInteger)count
                        usingDeck:(Deck *)deck
{
    self = [super initWithCardCount:count usingDeck:deck];
    self.numCardsToMatch = 3;    
    return self;
}

@end
