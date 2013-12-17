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

@interface SetViewController ()

@property (strong, nonatomic) SetGame *game;

#define SET_CARDS_TO_MATCH 3

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
                           numCardsToMatch:SET_CARDS_TO_MATCH];
    return _game;
}


- (Deck *)createDeck
{
    return [[SetCardDeck alloc] init];
}


/* TODO replace this with the isChosen images for SetCards */
- (UIImage *)selectedCardImage:(Card *)card
{
    /* TODO first slot should be @"cardfrontChosen" but I need to make that */
    return [UIImage imageNamed:card.isChosen ? @"cardfront" : @"cardfront"];
}



- (void)updateUI
{
    for (UIButton *cardButton in self.cardButtons) {
        int cardButtonIndex = [self.cardButtons indexOfObject:cardButton];
        SetCard *card = (SetCard *)[self.game cardAtIndex:cardButtonIndex];
        [cardButton setAttributedTitle:[self attributedTitleForCard:card]
                    forState:UIControlStateNormal];
        [cardButton setBackgroundImage:[self backgroundImageForCard:card]
                              forState:UIControlStateNormal];
        cardButton.titleLabel.font = [UIFont systemFontOfSize:14];
        cardButton.enabled = !card.isMatched;
        self.scoreLabel.text =
        [NSString stringWithFormat:@"Score: %d", self.game.score];
    }
}

- (NSAttributedString *)attributedTitleForCard:(SetCard *)card
{
    return card.isChosen
            ? card.attributedContents
            : [[NSAttributedString alloc] initWithString:@"" attributes:@{}];
}


- (UIImage *)backgroundImageForCard:(Card *)card
{ // First one should be selectedCardFront (just change the color a bit or something)
    return [UIImage imageNamed:card.isChosen ? @"cardfront" : @"cardfront"];
}



@end
