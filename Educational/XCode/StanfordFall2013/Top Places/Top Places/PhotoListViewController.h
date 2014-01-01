//
//  PhotoListViewController.h
//  Top Places
//
//  Created by Ethan Petuchowski on 12/31/13.
//  Copyright (c) 2013 Ethan Petuchowski. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PhotoListViewController : UITableViewController

@property (nonatomic) NSString *putAtTop;
- (void)doneLoading;

@end
