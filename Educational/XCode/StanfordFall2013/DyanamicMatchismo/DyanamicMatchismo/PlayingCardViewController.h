//
//  ViewController.h
//  SuperCard
//
//  Created by CS193p Instructor.
//  Modified by Ethan Petuchowski, no relation to Stanford.
//  Copyright (c) 2013 Stanford University. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
#import "CardMatchingGame.h"

@interface PlayingCardViewController : BaseViewController

@property (strong, nonatomic) CardMatchingGame *game;

@end
