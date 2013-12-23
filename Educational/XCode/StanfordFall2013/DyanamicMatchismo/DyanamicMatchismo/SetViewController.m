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
#import "SetCard.h"
#import "SetCardView.h"

@interface SetViewController ()

@property (strong, nonatomic) SetGame *game;

#define SET_CARDS_TO_MATCH 3
#define SET_CARDS_TO_START 12

@end

@implementation SetViewController

@synthesize game = _game;


- (SetGame *)game
{
    if (!_game) _game = [[SetGame alloc]
                         initWithCardCount:SET_CARDS_TO_START];
    return _game;
}


- (Deck *)createDeck
{
    return [[SetCardDeck alloc] init];
}


- (UIImage *)selectedCardImage:(Card *)card
{
    return [UIImage imageNamed:card.isChosen ? @"selectedcard" : @"cardfront"];
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    [self updateUI];
}


- (void)updateUI
{
    [super updateUI];
}


- (void)putCardInViewAtIndex:(int)index intoViewInRect:(CGRect)rect
{
    SetCard *card = self.game.cardsInPlay[index];
    [self.cardsInView setObject:[[SetCardView alloc]
                                 initWithFrame:rect
                                 withCard:card
                                 inContainer:self]
                         forKey:card.attributedContents];
}

@end
