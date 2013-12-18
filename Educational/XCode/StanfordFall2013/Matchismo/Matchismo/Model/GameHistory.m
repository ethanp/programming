//
//  GameHistory.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/17/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "GameHistory.h"

@implementation GameHistory

- (NSMutableString *)history
{
    if (!_history) _history = [[NSMutableString alloc] init];
    return _history;
}

- (void)addLineToHistory:(NSString *)str
{
    [self.history appendString:str];
    [self.history appendString:@"\n"];
}

- (void)clearHistory
{
    self.history = Nil;
}

- (NSString *)historyString
{
    return [self.history copy];
}

@end
