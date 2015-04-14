//
//  ViewController.swift
//  HIIM
//
//  Created by Ethan Petuchowski on 4/14/15.
//  Copyright (c) 2015 Ethan Petuchowski. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    // for this session
    var interval = 15
    var successesInARow = 0
    var numSuccesses = 0
    var successfulSeconds = 0
    var numFailures = 0
    var failingSeconds = 0
    

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

