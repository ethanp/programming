//
//  SetCardDeck.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/5/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "SetCardDeck.h"
#import "SetCard.h"

@implementation SetCardDeck

/* returns an instance of 'this' class */
- (instancetype)init
{
    self = [super init];
    
    if (self) {
        for (NSString *shape in [SetCard validShapes]) {
            for (NSString *color in [SetCard validColors]) {
                for (NSString *fill in [SetCard validFillTypes]) {
                    for (int num = 1; num <= 3; num++) {
                        SetCard *card = [[SetCard alloc] init];
                        card.shape = shape;
                        card.color = color;
                        card.fillType = fill;
                        card.number = [NSNumber numberWithInt:num];
                        [self addCard:card];
                    }
                }
            }
        }
    }
    return self;
}

@end
