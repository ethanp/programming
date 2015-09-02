//
//  ViewController.swift
//  PersistentTimer
//
//  Created by Ethan Petuchowski on 9/2/15.
//  Copyright (c) 2015 Ethanp. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    // MARK: - Utilities
    
    func resetTimer(var timer: NSTimer, toSeconds: NSTimeInterval, selector: Selector) -> NSTimer {
        timer.invalidate()
        timer = NSTimer.scheduledTimerWithTimeInterval(toSeconds, target: self, selector: selector, userInfo: nil, repeats: true)
        return timer
    }
    func setTitleColor(color: UIColor) {
        left.setTitleColor(color, forState: UIControlState.Normal)
    }
    func increment(amount: Int) {
        secTodo += amount
        displaySecs()
    }
    func countUp() { increment(1) }
    func countDown() { increment(-1) }
    
    // MARK: - Interface Elements

    @IBOutlet weak var stepper: UIStepper!
    @IBOutlet weak var left: UIButton!
    @IBOutlet weak var right: UILabel!

    
    // MARK: - Global Variables - (heh.)
    
    var upTimer = NSTimer()
    var downTimer = NSTimer()
    var secTodo = 0
    var tickingDown = false
    let ONE_SECOND = 1.0
    
    
    // MARK: - Main Logic
    
    override func viewDidLoad() {
        super.viewDidLoad()
        stepper.value = 1
        upTimer = resetTimer(upTimer, toSeconds: stepper.value, selector: "countUp")
        displaySecs()
    }
    
    func displaySecs() {
        if !tickingDown {
            setTitleColor(secTodo > 0 ?
                  UIColor.redColor()
                : UIColor.greenColor()
            )
        }
        let prefix = secTodo < 0 ? "-" : ""
        var secString = String(
            format: "\(prefix)%d:%02d",
            arguments: [abs(secTodo)/60, abs(secTodo)%60]
        )
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
        upTimer = resetTimer(upTimer, toSeconds: sender.value, selector: "countUp")
    }
    
    @IBAction func buttonPressed(sender: UIButton) {
        tickingDown = !tickingDown
        if tickingDown {
            setTitleColor(UIColor.blackColor())
            downTimer = resetTimer(downTimer, toSeconds: ONE_SECOND, selector: "countDown")
        }
        else {
            downTimer.invalidate()
        }
    }
}

