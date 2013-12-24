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
    [self restartGame];
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
        NSLog(@"Enlarging Grid");
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
    [self.layoutContainerView addSubview:brandNewCardView];
    [brandNewCardView animateCardInsertion];
}

/* update which cards are "chosen" or "matched" */
- (void)updateUI
{
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
