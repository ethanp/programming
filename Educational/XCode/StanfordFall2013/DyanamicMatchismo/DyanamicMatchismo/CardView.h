//
//  CardView.h
//  DyanamicMatchismo
//
//  Created by Ethan Petuchowski on 12/20/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Card.h"
#import "BaseViewController.h"

@interface CardView : UIView

@property (weak, nonatomic) Card *card;  // set by the MatchingGame at initialization
@property (weak, nonatomic) BaseViewController *container;
@property (nonatomic) BOOL thinksItsChosen;

- (void)handleTap:(UITapGestureRecognizer *)gesture; // abstract
- (id)initWithFrame:(CGRect)rect withCard:(Card *)card
        inContainer:(BaseViewController *)viewController;

- (void)animateCardInsertion;
- (void)animateCardRemoval;
- (void)animateChooseCard;

@end
