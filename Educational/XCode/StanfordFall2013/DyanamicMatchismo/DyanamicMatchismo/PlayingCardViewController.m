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

@property (strong, nonatomic) NSArray *cards; // of PlayingCards
@property (strong, nonatomic) PlayingCardDeck *deck;


@end

@implementation PlayingCardViewController

#define NUM_CARDS_TO_START 16

- (Deck *)deck
{
    if (!_deck) _deck = [[PlayingCardDeck alloc] init];
    return _deck;
}

- (PlayingCard *)drawRandomPlayingCard
{
    return (PlayingCard *)[self.deck drawRandomCard];
}


/* TODO this is just adding a card for now
 * but it will be adding ALL the cards (well, removing them first, etc.)
 */
- (IBAction)touchRedealButton:(UIButton *)sender
{
    // mark current set of cards as chosen to take them off the screen
    if ([self.cards count] > 0) {
        for (PlayingCard *card in self.cards) {
            card.chosen = YES;
        }
    }
    
    // draw a new set of cards
    NSMutableArray *drawnCards = [[NSMutableArray alloc] init];
    for (int i = 0; i < NUM_CARDS_TO_START; i++) {
        [drawnCards addObject:[self drawRandomPlayingCard]];
    }
    self.cards = [drawnCards copy];
    
    // TODO put the new cards on the screen
    [self updateUI];
}


- (void)viewDidLoad
{
    [super viewDidLoad];
}

@end
