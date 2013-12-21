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
#import "Grid.h"
#import "CardView.h"


@interface BaseViewController : UIViewController

@property (strong, nonatomic) Grid *grid;
@property (strong, nonatomic) MatchingGame *game;

/* to attach a new UILabel, you MUST drag a NEW Label object from the Object Pane.
 * You cannot reuse an old one because the old one is linked to a different var_name.
 * That link was created when you ctrl-dragged into the @property that it used to
 *   be connected to */
@property (weak, nonatomic) IBOutlet UILabel *scoreLabel;
@property (weak, nonatomic) IBOutlet UIButton *redealButton;
@property (weak, nonatomic) IBOutlet UIView *layoutContainerView;
@property (weak, nonatomic) IBOutletCollection(CardView) NSMutableArray *cardsInView;

- (void)restartGame;
- (void)updateUI;
- (IBAction)touchRedealButton:(UIButton *)sender;

@end
