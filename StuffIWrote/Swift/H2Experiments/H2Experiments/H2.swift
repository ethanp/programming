//
//  H2.swift
//  H2Experiments
//
//  Created by Ethan Petuchowski on 10/21/15.
//
//  Based on (Apache 2.0)  HTTP2Tester.swift by Florian Goessler
//   github.com/FGoessler/iOS-HTTP2-Test/blob/master/http2test/HTTP2Tester.swift
//   I've included the Apache license text in this project. I'm not sure if
//   there's anything else I have to do....
//
//  Copyright © 2015 Ethanp. All rights reserved.
//

import Foundation

// in Java a `protocol` is called an `interface`
protocol HTTP2TesterDelegate {
    func log(http2Tester: HTTP2Tester, msg: String)
}

// values for an `enum` in Swift can be a string, character, int, or float
// both of these are provided by `import Foundation`
enum NetworkingAPI: String {
    case NSURLConnection = "NSURLConnection" // does not support HTTP/2; from iOS 2.0
    case NSURLSession = "NSURLSession"		 // does support HTTP/2 under iOS 9 and above; from iOS 7.0
}

class HTTP2Tester {
    
    
    /*** FIELDS ***/
    
    /// The shared session uses the currently set global NSURLCache,
    ///      NSHTTPCookieStorage, and NSURLCredentialStorage objects
    ///      and is based on the default configuration.
    ///
    /// The NSURLSession class and related classes provide an API
    ///      for downloading content via HTTP.
    ///
    let session = NSURLSession.sharedSession()
    
    /// This is like a Java `Executor`. It executes `NSOperation` objects
    ///      according to priorities and dependencies between operations.
    ///
    /// You can cancel tasks still in the queue (or even running, but then
    ///      you have to check for cancellation explicitly).
    ///
    let http1CallbackQueue = NSOperationQueue()
    
    /// This means we (optionally) have a reference to an object that
    ///      implements the above `protocol HTTP2TesterDelegate`.
    ///
    /// This syntax (i.e. not using `!` instead) means we have to *explicitly*
    ///      unwrap the `delegate!` before we can access it. Though what we're
    ///      actually supposed to do is
    ///
    ///          if let d = delegate { /* code */ }
    ///
    var delegate: HTTP2TesterDelegate?
    
    
    /*** PUBLIC FUNCTIONS ***/
    
    /**
        Reset iOS's global shared cache of Map[NSURLRequest, NSCachedURLResponse] 
    */
    func resetHttpCache() {
        
        // only calls the method if the `delegate` exists
        delegate?.log(self, msg:"Resetting cache")
        
        // The `NSURLCache` class implements the caching of responses to URL
        //      load requests by mapping `NSURLRequest` objects to
        //      `NSCachedURLResponse` objects.
        NSURLCache.sharedURLCache().removeAllCachedResponses()
    }
    
    /** Sends a synchronous HTTP request to the Akamai HTTP/2 test page via
        the specified API (NSURLConnection or NSURLSession) and evaluates 
        whether this request was performed via HTTP/2 or not. This method 
        blocks the current thread until the request finished. This ensures
        a serial execution of tests. 
    */
    func sendHTTP2TestRequest(api: NetworkingAPI) -> NSTimeInterval {
        
        // avoid returning cached responses
        resetHttpCache()
        
        // log on which API the request is about to be sent
        self.delegate?.log(self, msg:"Starting HTTP request via \(api.rawValue)")
        
        let url = NSURL(string: "https://http2.akamai.com")!	// the Akamai HTTP/2 test url
        
        /* 
        Send the request and measure its time. This is a synchronous call.
        This will start a timer as soon as this code is invoked. It will stop
        and record the timer when `endMeasuring()` is called. You can still
        execute whatever code you want after that, but it will not be measured.
        There is no way to add non-measured code to the beginning of this block,
        but you could just call measure() later if that's what you wanted.
        `duration` becomes the resulting time interval recorded on the timer.
        */
        let duration = measure { (endMeasuring) in
            
            // if caller desired it, send via NSURLSession -> should use HTTP/2
            // (unless that's unavailable on the `url` being requested)
            if api == .NSURLSession {
                
                // Creates an HTTP GET request for the specified URL, 
                // then and stops timer upon completion.
                let task = self.session.dataTaskWithURL(url) { (data, res, error) in
                    self.evalResponse(res, data:data, error: error)
                    endMeasuring()
                }
                task.resume()
            }
                
            // otherwise, send via NSURLConnection -> will use HTTP/1.1
            else {
                
                // Load the data for a URL request and execute the handler block
                // on the operation `queue` when the request completes or fails.
                NSURLConnection.sendAsynchronousRequest(
                    NSURLRequest(URL: url),
                    queue: self.http1CallbackQueue
                ) {
                    (res, data, error) in
                    self.evalResponse(res, data:data, error: error)
                    endMeasuring()
                }
            }
        }
        
        self.delegate?.log(self, msg:"Completed request via \(api.rawValue) in \(duration) seconds.")
        
        return duration
    }
    
    /*** Sends multiple HTTP requests to load the Akamai HTTP/2 test resources
     (361 32x32 pixel PNG images) via the specified API (`NSURLConnection` or
     `NSURLSession`) and measures the time until all requests finished.
     
     This method blocks the current thread until all requests finished. This
     ensures a serial execution of tests. 
     */
    func sendMultipleHTTPRequests(api: NetworkingAPI) -> NSTimeInterval {
        
        // avoid returning cached responses!
        resetHttpCache()
        
        // log on which API the requests are about to be sent
        self.delegate?.log(self, msg:"Starting multiple HTTP requests via \(api.rawValue)")
        
        // Akamai provides a 19x19 tile grid of images (32x32 pixel PNGs) => 
        // 361 single images numbered 0...18, 10...28, ... , 360...378.
        // So we create an array which all of these numbers
        var tileIds = [Int]()
        for row in 0..<19 {
            for col in 0..<19 {
                tileIds.append(row * 20 + col)
            }
        }
        
        // this is a bit silly...
        let numRequests = tileIds.count
        
        // Send all 361 requests and measure the time until they finished.
        // This is a synchronous call.
        let duration = measure { (endMeasuring) in
            var finishedRequestsCount = 0
            for tileId in tileIds {
                
                // choose the url for the correct HTTP protocol
                let httpVrsn = "http\(api == .NSURLSession ? 2 : 1)"
                let url = NSURL(string: "https://\(httpVrsn).akamai.com/demo/tile-\(tileId).png")!
                
                // send via NSURLSession -> should use HTTP/2
                if api == .NSURLSession {
                    self.session.dataTaskWithURL(url) { (data, res, error) in

                        // log which protocol was used
                        self.evalResponse(res, data:data, error: error)
                        
                        // count another img fetched
                        finishedRequestsCount++;
                        
                        // if this was the last img, stop the timer
                        if finishedRequestsCount == numRequests {
                            endMeasuring()
                        }
                    }.resume()
                }
                
                // send via NSURLConnection -> will use HTTP/1.1
                else {
                    NSURLConnection.sendAsynchronousRequest(NSURLRequest(URL: url), queue: self.http1CallbackQueue) { (res, data, error) in

                        // log which protocol was used
                        self.evalResponse(res, data:data, error: error)
                        
                        // count another img fetched
                        finishedRequestsCount++;
                        
                        // if this was the last img, stop the timer
                        if finishedRequestsCount == numRequests {
                            endMeasuring()
                        }
                    }
                }
            }
        }
        
        self.delegate?.log(self, msg:"Completed \(numRequests) requests via \(api.rawValue) in \(duration) seconds.")
        
        return duration
    }
    
    /** Evaluates the response. Logs any errors, and if it's a string response,
        checks whether the string "You are using HTTP/2 right now!" is
        contained in it. 
     */
    private func evalResponse(response: NSURLResponse?, data: NSData?, error: NSError?) {
        
        // error -> log it
        if error != nil {
            self.delegate?.log(self, msg:
                "error: \(error!.localizedDescription): \(error!.userInfo)")
        }
        
        // otw if there's data
        else if let data = data {
            
            // use UTF-8 to decode the response data from HTTP as a String
            if let str = NSString(data: data, encoding: NSUTF8StringEncoding) {
                
                let httpProtocol: String
                
                // if we connected to the Akamai site using HTTP/2, it would
                // have this string in it.
                if str.containsString("You are using HTTP/2 right now!") {
                    httpProtocol = "HTTP/2"
                } else {
                    httpProtocol = "HTTP 1.1"
                }
                self.delegate?.log(self, msg:"Finished request to \(response!.URL!) - Used \(httpProtocol)")
            }
            
            // there was neither an error nor data
            else {
                //self.delegate?.log(self, msg:"Finished request to \(response!.URL!)")
            }
        }
    }
    
    /** Measures the time between the invocation of the 
        `operationToMeasureBlock` and the call of `endMeasuring` inside this
        block. It blocks the current thread until `endMeasuring` is called. 
        This ensures a serial execution of tests.
     */
    private func measure(operationToMeasureBlock: (endMeasuring: () -> ()) -> ()) -> NSTimeInterval {
        
        // create a semaphore that's already "taken"
        let semaphore = dispatch_semaphore_create(0)
        
        var duration = Double.infinity
        let starttime = NSDate().timeIntervalSince1970
        
        // this is *calling* the (passed-in) `operationToMeasureBlock`
        // with the given ()->() closure passed in via curly-braces.
        operationToMeasureBlock {
            
            // our closure has captured the starttime from when the `measure()`
            // function was first called, so we're sending it (captured)
            // to the `operationToMeasureBlock`, and we're also saving the
            // `duration` as set at the time of the *execution* of
            // `operationToMeasureBlock`.  pretty nifty.
            duration = NSDate().timeIntervalSince1970 - starttime
            
            // increment the semaphore whenever this callback is executed
            dispatch_semaphore_signal(semaphore)
        }
        
        // wait for the `operationToMeasureBlock` to be executed before
        // returning to make sure it happens one-at-a-time.
        dispatch_semaphore_wait(semaphore, DISPATCH_TIME_FOREVER)
        
        if (duration == Double.infinity) {
            self.delegate?.log(self, msg: "error: duration is still infinity!")
        }
        
        return duration
    }
    
    // TODO I need to write the data somehow
    // this is obviously not what I want, but it's some sort of approximation
    func saveData(response: NSData?) {
        let path = "thePath"
        if !NSFileManager.defaultManager().fileExistsAtPath(path) {
            NSFileManager.defaultManager().createFileAtPath(path, contents: response, attributes: nil)
            NSLog("file created at \(path)")
        }
        let fileHandle = NSFileHandle.init(forWritingAtPath: path)!
        fileHandle.truncateFileAtOffset(fileHandle.seekToEndOfFile())
        fileHandle.writeData(response!)
    }
}
