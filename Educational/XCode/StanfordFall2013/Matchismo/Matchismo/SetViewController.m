//
//  SetViewController.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/5/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "SetViewController.h"
#import "SetCardDeck.h"
#import "SetGame.h"

@interface SetViewController ()

@property (strong, nonatomic) SetGame *game;

@end

@implementation SetViewController

@synthesize game = _game;

- (SetGame *)game
{
    /* using a ternary operator for this doesn't work
     * because then you never actually assign to the _inner-variable */
    if (!_game) _game = [[SetGame alloc]
                         initWithCardCount:[self.cardButtons count]
                         usingDeck:[self createDeck]
                         mustMatch:3];
    return _game;
}

// this is supposed to OVERRIDE the CardGameVC method, I hope it works.
- (Deck *)createDeck
{
    return [[SetCardDeck alloc] init];
}

/* TODO replace this with the isChosen images for SetCards */
- (UIImage *)backgroundImageForCard:(Card *)card
{
    return [UIImage imageNamed:card.isChosen ? @"cardfront" : @"cardback"];
}

@end
