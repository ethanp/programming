//
//  SessionModel.swift
//  HIIM
//
//  Created by Ethan Petuchowski on 4/14/15.
//  Copyright (c) 2015 Ethan Petuchowski. All rights reserved.
//

import Foundation
class SessionModel {

    // properties
    var intervalLength = 15
    var successesInARow = 0
    var numSuccesses = 0
    var successfulSeconds = 0
    var numFailures = 0
    var failingSeconds = 0
    
    init() {
    }
}