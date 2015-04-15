//
//  ViewController.swift
//  HIIM
//
//  Created by Ethan Petuchowski on 4/14/15.
//  Copyright (c) 2015 Ethan Petuchowski. All rights reserved.
//

import UIKit
import AVFoundation

class ViewController: UIViewController {
    
    @IBOutlet var timerLabel: UILabel!
    @IBOutlet var summaryText: UITextView!
    
    var session = SessionModel()
    let EVERY_SECOND: Double = 1
    var bellSound = NSURL(fileURLWithPath: NSBundle.mainBundle().pathForResource("Bell", ofType: "m4a")!)

    var audioPlayer = AVAudioPlayer()
    var timer = NSTimer()
    var counter: Int = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        audioPlayer = AVAudioPlayer(contentsOfURL: bellSound, error: nil)
        audioPlayer.prepareToPlay()
        
        
        
        refreshUI()
    }

    func refreshUI() {
    }
    
    /******** BUTTONS *********/
    @IBAction func startSession(sender: AnyObject) {
        session = SessionModel()
        summaryText.text = ""
        
        // audio will play to completion once, even if you keep mashing it
        audioPlayer.play()
        timerLabel.text = String(counter)
        timer = NSTimer.scheduledTimerWithTimeInterval(
            EVERY_SECOND,
            target: self,
            selector: Selector("tick"),
            userInfo: nil,
            repeats: true
        )
    }
    
    
    @IBAction func endSession(sender: AnyObject) {
        timer.invalidate()
        
        summaryText.text = "Your session lasted \(counter-1) seconds"
        counter = 0
    }
    
    
    /******* util *******/
    func tick() {
        timerLabel.text = String(counter++)
        
    }

}

