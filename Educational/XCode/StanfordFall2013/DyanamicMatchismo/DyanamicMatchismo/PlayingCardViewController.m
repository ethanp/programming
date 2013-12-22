//
//  ViewController.m
//  SuperCard
//
//  Created by CS193p Instructor.
//  Copyright (c) 2013 Stanford University. All rights reserved.
//

#import "PlayingCardViewController.h"
#import "PlayingCardView.h"
#import "PlayingCardDeck.h"
#import "PlayingCard.h"

@interface PlayingCardViewController ()

@end

@implementation PlayingCardViewController

@synthesize game = _game;

- (CardMatchingGame *)game
{
    if (!_game) _game = [[CardMatchingGame alloc] init];
    return _game;
}

- (void)putCardInPlayAtIndex:(int)index intoViewInRect:(CGRect)rect
{
    [self.cardsInView addObject:[[PlayingCardView alloc]
                                 initWithFrame:rect
                                 withCard:self.game.cardsInPlay[index]
                                 inContainer:self]];
}

@end
