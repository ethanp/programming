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

/* He says you're not supposed to do this because it breaks MVC.
 * Instead, you're supposed to duplicate all the properties of the card
 * then in init (I guess?) you set them.
 * Maybe I'll change to his way later. */
@property (weak, nonatomic) Card *card;  // set by the MatchingGame at initialization

@property (weak, nonatomic) BaseViewController *container;
@property (nonatomic) BOOL thinksItsChosen;

- (void)handleTap:(UITapGestureRecognizer *)gesture; // abstract
- (id)initWithFrame:(CGRect)rect withCard:(Card *)card
        inContainer:(BaseViewController *)viewController;

- (void)animateCardInsertion;
- (void)animateCardRemoval;
- (void)animateChooseCard;
- (CGFloat)cornerScaleFactor;
- (CGFloat)cornerRadius;
- (CGFloat)cornerOffset;
- (void)setup;

@end
