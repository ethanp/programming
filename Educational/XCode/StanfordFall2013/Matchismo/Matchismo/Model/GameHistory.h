//
//  GameHistory.h
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/17/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GameHistory : NSObject

@property (nonatomic, strong) NSMutableAttributedString *history;

- (void)addLine:(NSAttributedString *)str;
- (void)clearHistory;
- (NSAttributedString *)historyAttrString;

@end
