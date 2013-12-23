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

- (void)viewDidLayoutSubviews
{
    [self updateUI];
}

- (IBAction)touchRedealButton:(UIButton *)sender {
    [self restartGame];
}

// inheriting classes must specify the actual CardView they want to use
- (void)putCardInPlayAtIndex:(int)index intoViewInRect:(CGRect)rect
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
                
                [self putCardInPlayAtIndex:cardsPlaced
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

/* This method should:
 *  1. Flip cards on the screen
 *  2. Remove cards from the screen
 *  3. Add cards to the screen
 */
- (void)updateUI
{
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
