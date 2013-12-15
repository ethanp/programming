//
//  BaseViewController.h
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/14/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

/* UIKit has UIButton and UI* and a bunch of NS* */
#import <UIKit/UIKit.h>
#import "MatchingGame.h"
#import "Deck.h"


@interface BaseViewController : UIViewController

- (void)restartGame;

- (IBAction)touchRedealButton:(UIButton *)sender;

@end
