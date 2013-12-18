//
//  BaseViewController.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/14/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "BaseViewController.h"
#import "HistoryViewController.h"

@interface BaseViewController ()
@end

@implementation BaseViewController

- (Deck *)createDeck
{
    return [[Deck alloc] init];
}

- (void)restartGame
{
    self.game = nil;
    [self updateUI];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    NSLog(@"%@", segue.identifier);
    if ([segue.identifier isEqualToString:@"History"]) {
        if ([segue.destinationViewController isKindOfClass:[HistoryViewController class]]) {
            HistoryViewController *hVC = (HistoryViewController *)segue.destinationViewController;
            hVC.gameHistory = self.game.history;
        }
    }
}


- (IBAction)touchRedealButton:(UIButton *)sender {
    [self restartGame];
    self.messageLabel.text = [NSString stringWithFormat:@"Redealt"];
}

- (IBAction)touchCardButton:(UIButton *)sender
{
    // because we can't guarantee a particular index in the array for this button
    int chosenButtonIndex = [self.cardButtons indexOfObject:sender];
    self.messageLabel.attributedText = [self.game chooseCardAtIndex:chosenButtonIndex];
    [self updateUI];
}

- (void)updateUI
{
    for (UIButton *cardButton in self.cardButtons) {
        int cardButtonIndex = [self.cardButtons indexOfObject:cardButton];
        Card *card = [self.game cardAtIndex:cardButtonIndex];
        [cardButton setTitle:[self titleForCard:card]
                    forState:UIControlStateNormal];
        [cardButton setBackgroundImage:[self backgroundImageForCard:card]
                              forState:UIControlStateNormal];
        cardButton.titleLabel.font = [UIFont systemFontOfSize:14];
        cardButton.enabled = !card.isMatched;
        self.scoreLabel.text =
        [NSString stringWithFormat:@"Score: %d", self.game.score];
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
