//
//  BaseViewController.h
//  DyanamicMatchismo
//
//  Created by Ethan Petuchowski on 12/19/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

/* UIKit has UIButton and UI* and a bunch of NS* */
#import <UIKit/UIKit.h>
#import "MatchingGame.h"
#import "Deck.h"


@interface BaseViewController : UIViewController


@property (strong, nonatomic) MatchingGame *game;

/* if you want the buttons to have a particular ordering,
 * you can't use an "output collection" */
@property (strong, nonatomic) IBOutletCollection(UIButton) NSArray *cardButtons;

/* to attach a new UILabel, you MUST drag a NEW Label object from the Object Pane.
 * You cannot reuse an old one because the old one is linked to a different var_name.
 * That link was created when you ctrl-dragged into the @property that it used to
 *   be connected to */
@property (weak, nonatomic) IBOutlet UILabel *scoreLabel;
@property (weak, nonatomic) IBOutlet UIButton *redealButton;

- (void)restartGame;

- (IBAction)touchRedealButton:(UIButton *)sender;

@end
