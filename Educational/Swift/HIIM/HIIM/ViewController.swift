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
    let tipCalc = SessionModel()
    
    var bellSound = NSURL(fileURLWithPath: NSBundle.mainBundle().pathForResource("Bell", ofType: "m4a")!)
    var audioPlayer = AVAudioPlayer()
    

    @IBAction func startSession(sender: AnyObject) {
        // audio will play to completion once, even if you keep mashing it
        audioPlayer.play()
    }
    
    func refreshUI() {
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        audioPlayer = AVAudioPlayer(contentsOfURL: bellSound, error: nil)
        audioPlayer.prepareToPlay()
        
        refreshUI()
    }
}

