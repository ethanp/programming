//
//  Game.h
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/13/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Card.h"

@interface MatchingGame : NSObject
{
    // http://stackoverflow.com/questions/575210/use-of-synthesize-property-in-objective-c-inheritance
    NSInteger score;
    int numCardsToMatch;
    NSMutableArray *cards;
}


@property (nonatomic, readwrite) NSInteger score;
@property (nonatomic, readwrite) int numCardsToMatch;
@property (nonatomic, strong) NSMutableArray *cards; // of Card [no type-parameters]

- (Card *)cardAtIndex:(NSUInteger)index;

@end
