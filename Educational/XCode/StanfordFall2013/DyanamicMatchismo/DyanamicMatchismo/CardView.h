//
//  CardView.h
//  DyanamicMatchismo
//
//  Created by Ethan Petuchowski on 12/20/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Card.h"

@interface CardView : UIView

@property (strong, nonatomic) Card *card;  // set by the MatchingGame at initialization

- (void)handleTap:(UITapGestureRecognizer *)gesture; // abstract
- (id)initWithFrame:(CGRect)rect withCard:(Card *)card;

@end
