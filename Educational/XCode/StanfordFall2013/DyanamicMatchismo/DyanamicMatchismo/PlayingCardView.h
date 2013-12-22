//
//  PlayingCardView.h
//  SuperCard
//
//  Created by CS193p Instructor.
//  Copyright (c) 2013 Stanford University. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CardView.h"
#import "PlayingCard.h"
#import "PlayingCardViewController.h"

@interface PlayingCardView : CardView

@property (weak, nonatomic) PlayingCard *card;

- (id)initWithFrame:(CGRect)frame withCard:(PlayingCard *)card inContainer:(PlayingCardViewController *)viewController;

@end
