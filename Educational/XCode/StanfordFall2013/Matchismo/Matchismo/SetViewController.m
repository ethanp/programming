//
//  SetViewController.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/5/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "SetViewController.h"
#import "SetCardDeck.h"

/*
 This INHERITS from CardGameViewController()
 I'm not sure if that's the /best/ way to do this.
 At this point, I'm not even sure what that DOES in the case of view controllers,
    and therefore whether this will work!
 
 I'm having the following problem:
    I want to 'connect' the SetCards of the view to a @property *cardCollection
    but since the *cardCollection belongs to the SuperClass,
        I can't just drag the connector-line into this window
    So instead I have connected the SetCards to the *cardCollection in the
        SuperClass.m file, because I've gotta connect them /somewhere/.
    But it still may be incorrect, and may even not work once things start going.
 
 The only solution is to wait until there is enough implementation there
    to see whether it did what I intended or not.
 I can't find anyone explicitly referencing this issue in a way I can understand
µ    on StackOverflow or in the docs for ViewControllers.
 */

@interface SetViewController ()

@end

@implementation SetViewController

// this is supposed to OVERRIDE the CardGameVC method, I hope it works.
- (Deck *)createDeck
{
    return [[SetCardDeck alloc] init];
}

/* TODO replace this with the isChosen images for SetCards */
- (UIImage *)backgroundImageForCard:(Card *)card
{
    return [UIImage imageNamed:card.isChosen ? @"cardfront" : @"cardback"];
}

@end
