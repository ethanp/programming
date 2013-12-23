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

// makes cards appear on the screen when the game first starts up
- (void)viewDidLayoutSubviews
{
    [self updateUI];
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
    CGFloat height = self.layoutContainerView.bounds.size.height;
    CGFloat width  = self.layoutContainerView.bounds.size.width;
    [self.grid setCellAspectRatio:width/height];
    [self.grid setSize:CGSizeMake(width, height)];
    [self.grid setMinimumNumberOfCells:[self.game.cardsInPlay count]];
    
    for (NSString *cardName in [self.cardsInView copy])
        [self removeCardFromView:cardName];
    
    for (Card *card in self.game.cardsInPlay)
        [self addCardToView:card];
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
        CGFloat height = self.layoutContainerView.bounds.size.height;
        CGFloat width  = self.layoutContainerView.bounds.size.width;
        [self.grid setCellAspectRatio:width/height];
        [self.grid setSize:CGSizeMake(width, height)];
        [self.grid setMinimumNumberOfCells:newCount];
    }
    
    // add the view to self.cardsInView and redrawAllCards()
    int row = newIndex / self.grid.columnCount;
    int col = newIndex % self.grid.columnCount;
    
    CGRect rect =
        CGRectOffset([self.grid frameOfCellAtRow:row inColumn:col], 0.9, 0.9);
    
    [self putCardInViewAtIndex:newIndex intoViewInRect:rect];
    CardView *brandNewCardView = [self.cardsInView objectForKey:card.attributedContents];
    [brandNewCardView animateCardInsertion];
}

- (void)updateUI
{
    /*
     * REDRAW ALL CARDS IF NECESSARY
     */
    
    // new game, or redeal
    if (!self.game)
        [self redrawAllCards];
    
    // if size changes across 9 in either direction,
    // move to new grid
    // (not sure this will work, but it sounds nice)
    if (([self.cardsInView count] > 9) != ([self.game.cardsInPlay count] > 9))
        [self redrawAllCards];


    /* 
     * UPDATE WHICH CARDS ARE ON THE SCREEN
     * AND WHETHER THEY ARE "CHOSEN"
     */
    
    NSMutableDictionary *viewDictCopy = [self.cardsInView mutableCopy];
    
    // add cards that are in play but not in view to view
    // un/choose cards that are in view but have the wrong "thinksItsChosen" status
    for (Card *card in self.game.cardsInPlay) {
        CardView *cardView = [self.cardsInView objectForKey:card.attributedContents];
        if (!cardView) {
            [self addCardToView:card];
        } else if (card.chosen != cardView.thinksItsChosen) {
            cardView.thinksItsChosen = card.chosen;
            [cardView animateChooseCard];
        }
        [viewDictCopy removeObjectForKey:card.attributedContents];
    }
    
    // remove cards that have been removed for any reason (really just matched)
    for (NSString *cardName in viewDictCopy.allKeys) {
        [self removeCardFromView:cardName];
    }
    
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
