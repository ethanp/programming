//
//  Event.swift
//  BP2
//
//  Created by Ethan Petuchowski on 3/23/15.
//  Copyright (c) 2015 Ethan Petuchowski. All rights reserved.
//

import Foundation
class Event {
    var name: String
    var date: NSDate
    
    init(name: String, date: NSDate) {
        self.name = name
        self.date = date
    }
}