//
//  GameHistory.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/17/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "GameHistory.h"

@implementation GameHistory

- (NSMutableAttributedString *)history
{
    if (!_history) _history = [[NSMutableAttributedString alloc] init];
    return _history;
}

- (void)addLine:(NSAttributedString *)str
{
    [self.history appendAttributedString:str];
    [self.history appendAttributedString:[[NSAttributedString alloc] initWithString:@"\n"]];
}

- (void)clearHistory
{
    self.history = Nil;
}

- (NSAttributedString *)historyAttrString
{
    return [self.history copy];
}

@end
