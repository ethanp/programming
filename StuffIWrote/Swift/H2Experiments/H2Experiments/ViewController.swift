//
//  ViewController.swift
//  H2Experiments
//
//  Created by Ethan Petuchowski on 10/21/15.
//
//  Based on (Apache 2.0) code by Florian Goessler at:
//  github.com/FGoessler/iOS-HTTP2-Test/blob/master/http2test/ViewController.swift
//  I've included the Apache license text in this project. I'm not sure if
//  there's anything else I have to do....
//

import UIKit

// this class `extends UIViewController` and `implements HTTP2TesterDelegate`
class ViewController: UIViewController, HTTP2TesterDelegate {
    
    // create an instance of the Tester class we defined in H2.swift
    let http2Tester = HTTP2Tester()
    
    // `lazy var` means that this closure will be executed to initialize
    //      the `dateFormatter` whenever it is first accessed.
    //
    // For such a small project, this is a rather silly optimization....
    //
    lazy var dateFormatter: NSDateFormatter = {
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "HH:mm:ss:SSS"
        return dateFormatter
    }(/* these parens are part of the Swift syntax! */)
    
    
    /*** Connect to the UI elements in the storyboard ***/
    
    @IBOutlet weak var startButton: UIButton!
    @IBOutlet weak var statusLabel: UILabel!
    @IBOutlet weak var logTextView: UITextView!
    
    /** When the user hits the "Start" button */
    @IBAction func startTest(sender: AnyObject) {
        http2Tester.delegate = self
        
        /* update UI */
        startButton.setTitle("Running...", forState: .Normal)
        startButton.enabled = false
        logTextView.text = ""
        statusLabel.text = "Starting..."
        
        /* start requests */
        
        // QOS_CLASS_USER_INTERACTIVE: 
        //      Highest priority. Use only for critical interactive elements.
        //
        // QOS_CLASS_USER_INITIATED: 
        //      Priority below critical, but higher than other work. NOTE: Not energy efficient!
        //
        // QOS_CLASS_DEFAULT: 
        //      Default used by `pthread_create()`. Higher than utility and background tasks.
        //
        // QOS_CLASS_UTILITY: 
        //      Work will be done in an energy efficient manner. User unlikely waiting for results.
        //
        // QOS_CLASS_BACKGROUND: 
        //      User doesn't even know this work is happening. Most energy efficient of all.
        //
        // 0: 
        //      this is the required value for the second parameter. (fools.)
        //
        // So first we get the global queue for 2nd highest priority level.
        // Then we add the work in the closure to the task which will be added
        //      to that queue.
        //
        dispatch_async(dispatch_get_global_queue(QOS_CLASS_USER_INITIATED, 0)) {
            
            // just noting that this closure has no arguments and no return value
            () -> Void in
            
            // send single test requests to test that NSURLSession actually uses HTTP/2 and NSURLConnection uses HTTP 1.1
            self.http2Tester.sendHTTP2TestRequest(.NSURLConnection)
            self.http2Tester.sendHTTP2TestRequest(.NSURLSession)
            
            // send multiple requests at once to demonstrate the advantage of HTTP/2
            self.http2Tester.sendMultipleHTTPRequests(.NSURLConnection)
            self.http2Tester.sendMultipleHTTPRequests(.NSURLSession)
            
            // update UI after finishing the requests
            dispatch_async(dispatch_get_main_queue()) {
                self.statusLabel.text = "Finished"
                self.logTextView.text = "[\(self.dateFormatter.stringFromDate(NSDate()))]: Finished\n\(self.logTextView.text)"
                self.startButton.setTitle("Restart it!", forState: .Normal)
                self.startButton.enabled = true
            }
        }
        
    }
    
    /** Implements the `protocol HTTP2TesterDelegate` 
        by logging the given string to the consol, setting it (temporarily)
        as the Label on screen, and adding it (with the current time) to the
        TextView on screen.
    */
    func log(http2Tester: HTTP2Tester, msg: String) {
        NSLog(msg)
        
        // do UI work on the applications main_queue
        dispatch_async(dispatch_get_main_queue()) {
            self.statusLabel.text = msg
            self.logTextView.text = "[\(self.dateFormatter.stringFromDate(NSDate()))]: \(msg)\n\(self.logTextView.text)"
        }
    }
}

