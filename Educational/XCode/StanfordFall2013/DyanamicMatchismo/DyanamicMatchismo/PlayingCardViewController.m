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

// TODO make this an Array somehow...
@property (weak, nonatomic) IBOutlet PlayingCardView *playingCardView;
@property (strong, nonatomic) Deck *deck;
@end

@implementation PlayingCardViewController

- (Deck *)deck
{
    if (!_deck) _deck = [[PlayingCardDeck alloc] init];
    return _deck;
}

- (void)drawRandomPlayingCard
{
    Card *card = [self.deck drawRandomCard];
    if ([card isKindOfClass:[PlayingCard class]]) {
        PlayingCard *playingCard = (PlayingCard *)card;
        self.playingCardView.rank = playingCard.rank;
        self.playingCardView.suit = playingCard.suit;
    }
}

- (IBAction)swipe:(UISwipeGestureRecognizer *)sender
{
    if (!self.playingCardView.faceUp) [self drawRandomPlayingCard];

    [self.playingCardView handleSwipe:sender];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    [self.playingCardView addGestureRecognizer:[[UIPinchGestureRecognizer alloc]
                                                initWithTarget:self.playingCardView
                                                action:@selector(pinch:)]];

    // code from http://stackoverflow.com/questions/9032331/ios-flip-animation-only-for-specific-view
    [self.playingCardView addGestureRecognizer:[[UITapGestureRecognizer alloc]
                                                initWithTarget:self.playingCardView
                                                action:@selector(handleSwipe:)]];
}

@end
