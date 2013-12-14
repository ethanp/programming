//
//  Game.h
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/13/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface MatchingGame : NSObject


@property (nonatomic, readonly) NSInteger score;
@property (nonatomic, readwrite) int numCardsToMatch;
@property (nonatomic, strong) NSMutableArray *cards; // of Card [no type-parameters]

@end
