//
//  BaseViewController.m
//  DyanamicMatchismo
//
//  Created by Ethan Petuchowski on 12/19/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "BaseViewController.h"
#import "CardView.h"

@interface BaseViewController ()

@end

@implementation BaseViewController

- (NSMutableDictionary *)cardsInView
{
    if (!_cardsInView) _cardsInView = [[NSMutableDictionary alloc] init];
    return _cardsInView;
}

- (Grid *)grid
{
    if (!_grid) _grid = [[Grid alloc] init];
    return _grid;
}

- (void)restartGame
{
    self.game = nil;
    [self redrawAllCards];
}

// NOTE: this gets called when the layout is about to switch btn portrait/landscape
- (void)viewDidLayoutSubviews
{
    [super viewDidLayoutSubviews];
    // TODO: resize self.grid in here
    [self redrawAllCards];
    return;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    if (![self.layoutContainerView.subviews count])
        [self drawAllCards];
}

- (IBAction)touchRedealButton:(UIButton *)sender {
    [self restartGame];
}

// Note: inheriting classes must specify the actual CardView they want to use
- (void)putCardInViewAtIndex:(int)index intoViewInRect:(CGRect)rect
{
    Card *card = self.game.cardsInPlay[index];
    [self.cardsInView setObject:[[CardView alloc]
                                 initWithFrame:rect withCard:card inContainer:self]
                         forKey:card.attributedContents];
}

- (void)redrawAllCards
{
    for (NSString *cardName in [self.cardsInView copy])
        [self removeCardFromView:cardName];
    
    [self drawAllCards];
}

- (void)resetGridSize
{
    CGFloat height = self.layoutContainerView.bounds.size.height;
    CGFloat width  = self.layoutContainerView.bounds.size.width;
    NSLog(@"Height: %f; Width: %f", height, width);
    [self.grid setCellAspectRatio:1.3/2.0];
    [self.grid setSize:CGSizeMake(width, height)];
    [self.grid setMinimumNumberOfCells:[self.game.cardsInPlay count]];
    NSLog(@"Grid Size: %@", NSStringFromCGSize([self.grid size]));
}

// doesn't check that these cards aren't already in the view
- (void)drawAllCards
{
    [self resetGridSize];
    for (Card *card in self.game.cardsInPlay)
        if (!card.isMatched)
            [self addCardToView:card];
    [self updateUI];
}

- (void)removeCardFromView:(NSString *)cardName
{
    CardView *cardView = [self.cardsInView objectForKey:cardName];
    [cardView animateCardRemoval];
    [self.cardsInView removeObjectForKey:cardName];
}

- (void)addCardToView:(Card *)card
{
    // ensure the grid has enough space
    int gridCapacity = [self.grid columnCount] * [self.grid rowCount];
    int newIndex = [self.cardsInView count];
    int newCount = newIndex + 1;
    if (newCount > gridCapacity) {
        [NSException raise:@"Grid is wrong size"
                    format:@"Has capacity %d, should have capacity %d", gridCapacity, newCount];
    }
    
    // add the view to self.cardsInView and redrawAllCards()
    int row = newIndex / self.grid.columnCount;
    int col = newIndex % self.grid.columnCount;
    
    CGRect rect =
        CGRectOffset([self.grid frameOfCellAtRow:row inColumn:col], 0.9, 0.9);
    
    [self putCardInViewAtIndex:newIndex intoViewInRect:rect];
    CardView *brandNewCardView = [self.cardsInView objectForKey:card.attributedContents];
    [self.layoutContainerView addSubview:brandNewCardView];
    [brandNewCardView animateCardInsertion];
}

- (void)updateUI
{
    /* update which cards are "chosen" or "matched" */
    NSMutableDictionary *viewDictCopy = [self.cardsInView mutableCopy];
    for (Card *card in self.game.cardsInPlay) {
        CardView *cardView = [viewDictCopy objectForKey:card.attributedContents];
        if (card.chosen != cardView.thinksItsChosen) {
            cardView.thinksItsChosen = card.chosen;
            [cardView animateChooseCard];
        }
        if (card.isMatched != cardView.thinksItsMatched) {
            cardView.thinksItsMatched = card.isMatched;
            [self removeCardFromView:(NSString *)card.attributedContents];
        }
        [viewDictCopy removeObjectForKey:card.attributedContents];
    }
    
    // remove cards that have been matched
    for (NSString *cardName in viewDictCopy.allKeys)
        [self removeCardFromView:cardName];
    
    // update score
    self.scoreLabel.text = [NSString stringWithFormat:@"Score: %d", self.game.score];
}

- (void)cardWasChosen:(Card *)card
{
    [self.game chooseCardAtIndex:[self.game.cards indexOfObject:card]];
    [self updateUI];
    
}

- (NSString *)titleForCard:(Card *)card
{
    return card.isChosen ? card.contents : @"";
}

- (UIImage *)backgroundImageForCard:(Card *)card
{
    return [UIImage imageNamed:card.isChosen ? @"cardfront" : @"cardback"];
}

@end
