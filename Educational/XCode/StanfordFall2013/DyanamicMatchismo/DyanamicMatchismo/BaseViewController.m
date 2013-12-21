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

- (NSInteger)numCardsInPlay
{
    int numCardsInPlay = 0;
    for (Card *card in self.game.cards) {
        if (!card.chosen) {
            numCardsInPlay++;
        }
    }
    return numCardsInPlay;
}

- (void)updateUI
{
    CGFloat height = self.layoutContainerView.bounds.size.height;
    CGFloat width  = self.layoutContainerView.bounds.size.width;
    [_grid setCellAspectRatio:height/width];
    [_grid setSize:CGSizeMake(height, width)];
    [_grid setMinimumNumberOfCells:[self numCardsInPlay]];
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
