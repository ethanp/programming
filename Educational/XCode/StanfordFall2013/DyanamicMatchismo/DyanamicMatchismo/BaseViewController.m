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

- (NSMutableArray *)cardsInView
{
    if (!_cardsInView) _cardsInView = [[NSMutableArray alloc] init];
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
    [self updateUI];
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
    [self.cardsInView addObject:[[CardView alloc]
                                 initWithFrame:rect
                                 withCard:self.game.cardsInPlay[index]
                                 inContainer:self]];
}

- (void)redrawAllCards
{
    [[self.layoutContainerView subviews]
     makeObjectsPerformSelector:@selector(removeFromSuperview)];
    
    CGFloat height = self.layoutContainerView.bounds.size.height;
    CGFloat width  = self.layoutContainerView.bounds.size.width;
    [self.grid setCellAspectRatio:width/height];
    [self.grid setSize:CGSizeMake(width, height)];
    [self.grid setMinimumNumberOfCells:[self.game.cardsInPlay count]];
    
    // TODO call a method that animates removing each CardView
    // then use makeAllObjectsPerformSelector:@selector(animateRemovingCard:)
    [self.cardsInView removeAllObjects];
    int cardsPlaced = 0;
    for (int row = 0; row < self.grid.rowCount; row++) {
        for (int col = 0; col < self.grid.columnCount; col++) {
            if (cardsPlaced < [self.game.cardsInPlay count]) {
                
                CGRect rect = [self.grid frameOfCellAtRow:row
                                                 inColumn:col];
                
                rect.size.height *= 0.9;
                rect.size.width  *= 0.9;
                
                [self putCardInViewAtIndex:cardsPlaced
                            intoViewInRect:rect];
                
                cardsPlaced++;
            }
            else break;
        }
    }
    
    for (CardView *cardView in self.cardsInView) {
        
        // TODO call a method that animates adding each CardView
        // then use makeAllObjectsPerformSelector:@selector(animateAddingCard:)
        [self.layoutContainerView addSubview:cardView];
    }
}

// TODO
- (void)animateCardInsertion:card
{
    
}

// TODO
- (void)animateCardRemoval:(id)card
{
    
}

// TODO
- (void)animateChooseCard:(id)card
{
    
}

// TODO
- (void)removeCardFromViewAtIndex:(int)index
{
    
}

- (void)addCardToView:(Card *)card
{
    // if there's space for the card in the grid,
    // find the first empty spot and stick it in there (animatedly)
    int gridCapacity = [self.grid columnCount] * [self.grid rowCount];
    if ([self.cardsInView count] + 1 > gridCapacity) {
        
    }
    
    // otw add the view to self.cardsInView and redrawAllCards()
}

/* This method should:
 *  1. Flip cards on the screen
 *  2. Remove cards from the screen
 *  3. Add cards to the screen

===========================================
My current algorithm is totally brain-dead;
===========================================
 Here's a much better way to do it:
 ----------------------------------
    view_dict := VIEW.toDict()
    for card in PLAY:
        if card in view_dict:
            if card.chosen and not view_dict[card].chosen:
                animate_choose_card()
        else:
            add_card_to_view(card)
        view_dict.remove(card)
    for card in view_dict.keys():
        remove_from_view(card)
 */
- (void)updateUI
{
    // new game, or redeal
    if (!self.game)
        [self redrawAllCards];
    
    // remove CardViews that are no longer in play
    for (int index = 0; index < [self.cardsInView count]; index++) {
        CardView *cardView = [self.cardsInView objectAtIndex:index];
        if (![self.game.cardsInPlay containsObject:cardView.card]) {
            [self removeCardFromViewAtIndex:index];
        }
    }
    
    // create dictionary of { card -> cardInView }
    NSMutableDictionary *cardsInViewDict = [[NSMutableDictionary alloc] init];
    for (CardView *cardView in self.cardsInView) {
        [cardsInViewDict setObject:cardView forKey:cardView.card.attributedContents];
    }
    
    // add cards that are in play but not in view to view
    for (Card *card in self.game.cardsInPlay) {
        if (![cardsInViewDict objectForKey:card.attributedContents]) {
            [self addCardToView:card];
        }
    }
    
    
    // if size changes across 9 in either direction,
    // move to new grid
    // (not sure this will work, but it sounds nice)
    if (([self.cardsInView count] > 9) != ([self.game.cardsInPlay count] > 9))
        [self redrawAllCards];
    
    
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
