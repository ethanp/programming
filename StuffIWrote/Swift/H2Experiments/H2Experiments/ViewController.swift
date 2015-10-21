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
    
    
    lazy var dateFormatter: NSDateFormatter = {
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateStyle = NSDateFormatterStyle.NoStyle
        dateFormatter.timeStyle = NSDateFormatterStyle.ShortStyle
        return dateFormatter
    }()
    
    @IBOutlet weak var startButton: UIButton!
    @IBOutlet weak var statusLabel: UILabel!
    @IBOutlet weak var logTextView: UITextView!
    
    @IBAction func startTest(sender: AnyObject) {
        http2Tester.delegate = self
        
        // update UI
        startButton.setTitle("Running...", forState: .Normal)
        startButton.enabled = false
        logTextView.text = ""
        statusLabel.text = "Starting..."
        
        // start requests
        dispatch_async(dispatch_get_global_queue(QOS_CLASS_USER_INITIATED, 0)) { () -> Void in
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
    
    func http2Tester(http2Tester: HTTP2Tester, notifiesAboutEvent msg: String) {
        NSLog(msg)
        dispatch_async(dispatch_get_main_queue()) {
            self.statusLabel.text = msg
            self.logTextView.text = "[\(self.dateFormatter.stringFromDate(NSDate()))]: \(msg)\n\(self.logTextView.text)"
        }
    }
}

