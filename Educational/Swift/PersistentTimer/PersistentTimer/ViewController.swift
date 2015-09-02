//
//  ViewController.swift
//  PersistentTimer
//
//  Created by Ethan Petuchowski on 9/2/15.
//  Copyright (c) 2015 Ethanp. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var stepper: UIStepper!
    @IBOutlet weak var left: UIButton!
    @IBOutlet weak var right: UILabel!

    var upTimer = NSTimer()
    var downTimer = NSTimer()
    var secTodo = 0
    var tickingDown = false
    let ONE_SECOND = 1.0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        stepper.value = 1
        upTimer = NSTimer.scheduledTimerWithTimeInterval(stepper.value, target: self, selector: "countUp", userInfo: nil, repeats: true)
        displaySecs()
    }
    
    func countUp() {
        secTodo++
        displaySecs()
    }
    
    func countDown() {
        secTodo--
        displaySecs()
    }
    
    func displaySecs() {
        
        if !tickingDown {
            left.setTitleColor(secTodo > 0 ? UIColor.redColor() : UIColor.greenColor(), forState: UIControlState.Normal)
        }
        let prefix = secTodo < 0 ? "-" : ""
        let absSec = abs(secTodo)
        var secString = String(format: "\(prefix)%d:%02d", arguments: [absSec/60, absSec%60])
        left.setTitle(secString, forState: UIControlState.Normal)
    }

    @IBAction func stepperValueChanged(sender: UIStepper) {
        if sender.value < 1 {
            sender.value = 1
        }
        var minutes = "Minutes"
        if sender.value == 1 {
            minutes = "Minute"
        }
        right.text = "\(Int(sender.value).description) \(minutes)"
        upTimer.invalidate()
        upTimer = NSTimer.scheduledTimerWithTimeInterval(sender.value, target: self, selector: "countUp", userInfo: nil, repeats: true)
    }
    @IBAction func buttonPressed(sender: UIButton) {
        tickingDown = !tickingDown
        if tickingDown {
            sender.setTitleColor(UIColor.blackColor(), forState: UIControlState.Normal)
            downTimer.invalidate()
            downTimer = NSTimer.scheduledTimerWithTimeInterval(ONE_SECOND, target: self, selector: "countDown", userInfo: nil, repeats: true)
        }
        else {
            downTimer.invalidate()
        }
    }
}

