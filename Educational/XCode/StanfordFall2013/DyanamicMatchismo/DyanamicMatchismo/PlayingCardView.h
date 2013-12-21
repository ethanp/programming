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

@interface PlayingCardView : CardView

@property (strong, nonatomic) PlayingCard *card;

@end
