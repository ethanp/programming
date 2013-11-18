//
//  CardMatchingGame.h
//  Matchismo
//
//  Created by Ethan Petuchowski on 11/12/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

/* foundatation has NSArray and NS* */
#import <Foundation/Foundation.h>
#import "Deck.h"   /* imports models */
#import "Card.h"   /* doesn't import viewController */

@interface CardMatchingGame : NSObject

/* designated initializer
 * must be called from any of our other initializers
 * or from any subclass's initializers */
- (instancetype)initWithCardCount:(NSUInteger)count
                        usingDeck:(Deck *)deck;

- (void)chooseCardAtIndex:(NSUInteger)index;
- (Card *)cardAtIndex:(NSUInteger)index;

@property (nonatomic, readonly) NSInteger score;

// should I be more worried about manipulating this in the view?
@property (nonatomic, readwrite) BOOL isThreeCardMode;

@end
