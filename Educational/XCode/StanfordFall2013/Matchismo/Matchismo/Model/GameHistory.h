//
//  GameHistory.h
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/17/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GameHistory : NSObject

@property (nonatomic, strong) NSMutableString *history;

- (void)addLineToHistory:(NSString *)str;
- (void)clearHistory;
- (NSString *)historyString;

@end
