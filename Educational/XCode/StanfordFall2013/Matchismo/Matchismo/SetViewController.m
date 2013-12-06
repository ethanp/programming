//
//  SetViewController.m
//  Matchismo
//
//  Created by Ethan Petuchowski on 12/5/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import "SetViewController.h"


/*
 This INHERITS from CardGameViewController()
 I'm not sure if that's what's /supposed/ to go on
 At this point, I'm not even sure what that DOES in the case of view controllers!
 
 I'm having the following problem:
    I want to 'connect' the SetCards of the view to a @property *cardCollection
    but since the *cardCollection is in the SuperClass, 
        I can't just drag it into this window
    So instead I have connected the SetCards to the *cardCollection in the
        SuperClass.m file, because that makes the /most/ sense out of my options
    but it still may be incorrect, and may even not work once things start going.
 
 The only solution is to wait until there is enough implementation there
    to see whether it did what I intended or not.
 I can't find anyone explicitly referencing this issue in a way I can understand
    on StackOverflow
 */

@interface SetViewController ()

@end

@implementation SetViewController


@end
