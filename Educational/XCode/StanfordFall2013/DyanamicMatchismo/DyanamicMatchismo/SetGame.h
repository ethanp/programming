//
//  SetGame.h
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/5/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "MatchingGame.h"
#import "SetCardDeck.h"

@interface SetGame : MatchingGame

@property (nonatomic) SetCardDeck *deck;

- (instancetype)init;

@end
