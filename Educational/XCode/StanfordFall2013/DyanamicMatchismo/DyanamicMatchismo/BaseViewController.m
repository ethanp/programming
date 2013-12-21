//
//  BaseViewController.m
//  DyanamicMatchismo
//
//  Created by Ethan Petuchowski on 12/19/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "BaseViewController.h"

@interface BaseViewController ()

@end

@implementation BaseViewController

- (Deck *)createDeck
{
    return [[Deck alloc] init];
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

- (IBAction)touchRedealButton:(UIButton *)sender {
    [self restartGame];
}

- (void)updateUI
{
    CGFloat height = self.layoutContainerView.bounds.size.height;
    CGFloat width  = self.layoutContainerView.bounds.size.width;
    [self.grid setCellAspectRatio:height/width];
    [self.grid setSize:CGSizeMake(height, width)];
    [self.grid setMinimumNumberOfCells:[self.game.cardsInPlay count]];
    NSArray *cardsToPlace = [self.game.cardsInPlay copy];
    int cardsPlaced = 0;
    for (int row = 0; row < self.grid.rowCount; row++) {
        for (int col = 0; col < self.grid.columnCount; col++) {
            if (cardsPlaced < [cardsToPlace count]) {
                CGRect rect = [self.grid frameOfCellAtRow:row inColumn:col];
                [self.cardsInView
                 addObject:[[CardView alloc]
                            initWithFrame:rect
                            withCard:cardsToPlace[cardsPlaced]]];
                cardsPlaced++;
            }
            else break;
        }
    }
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
