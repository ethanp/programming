//
//  PlacePhotosObject.h
//  Top Places
//
//  Created by Ethan Petuchowski on 1/1/14.
//  Copyright (c) 2014 Ethan Petuchowski. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface PlacePhotosObject : NSObject

@property (nonatomic) NSDictionary *baseDict; // TODO just move this private
@property (nonatomic) NSString *place_id;

@end
