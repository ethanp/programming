//
//  CardMatchingGame.h
//  Matchismo
//
//  Created by Ethan Petuchowski on 11/12/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

/* foundatation has NSArray and NS* */
#import <Foundation/Foundation.h>
#import "MatchingGame.h"
#import "PlayingCardDeck.h"

@interface CardMatchingGame : MatchingGame

@property (nonatomic) PlayingCardDeck *deck;

- (instancetype)init;

@end
