//
//  SetCardView.h
//  DyanamicMatchismo
//
//  Created by Ethan Petuchowski on 12/20/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SetCard.h"
#import "CardView.h"

@interface SetCardView : CardView

@property (nonatomic, weak) SetCard *card;

@end
