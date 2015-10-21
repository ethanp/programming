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
    func http2Tester(http2Tester: HTTP2Tester, notifiesAboutEvent: String)
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
        delegate?.http2Tester(self, notifiesAboutEvent:"Resetting cache")
        
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
        
        resetHttpCache() // avoid returning cached responses
        
        self.delegate?.http2Tester(self, notifiesAboutEvent:"Starting HTTP request via \(api.rawValue)")
        
        let url = NSURL(string: "https://http2.akamai.com")!	// the Akamai HTTP/2 test url
        
        // send the request and meassure its time - this is a synchronous call
        let duration = meassure { (endMeassuring) in
            // send via NSURLSession -> should use HTTP/2
            if api == .NSURLSession {
                let task = self.session.dataTaskWithURL(url) { (data, res, error) in
                    self.evalResponse(res, data:data, error: error)
                    endMeassuring()
                }
                task.resume()
                
                // send via NSURLConnection -> should not use HTTP/2
            } else {
                NSURLConnection.sendAsynchronousRequest(NSURLRequest(URL: url), queue: self.http1CallbackQueue) { (res, data, error) in
                    self.evalResponse(res, data:data, error: error)
                    endMeassuring()
                }
            }
        }
        
        self.delegate?.http2Tester(self, notifiesAboutEvent:"Completed HTTP/2 test request via \(api.rawValue) in \(duration) seconds.")
        
        return duration
    }
    
    /** Sends multiple HTTP requests to load the Akamai HTTP/2 test resources (361 32x32 pixel PNG images) via the specified API (NSURLConnection or NSURLSession) and meassures the time until all requests finished. This method blocks the current thread until all requests finished. This ensures a serial execution of tests. */
    func sendMultipleHTTPRequests(api: NetworkingAPI) -> NSTimeInterval {
        resetHttpCache()	// reset the cache to avoid returning cached responses!
        
        self.delegate?.http2Tester(self, notifiesAboutEvent:"Starting multiple HTTP requests via \(api.rawValue)")
        
        // Akamai provides a 19x19 tile grid of images (32x32 pixel PNGs) => 361 single images numbered 0...18, 10...28, ... , 360...378
        var tileIds = [Int]()
        for var row in 0..<19 {
            for var col in 0..<19 {
                tileIds.append(row * 20 + col)
            }
        }
        let numRequests = tileIds.count
        
        // send all 361 requests and meassure the time until they finished - this is a synchronous call
        let duration = meassure { (endMeassuring) in
            var finishedRequestsCount = 0
            for var tileId in tileIds {
                let url = NSURL(string: "https://http\(api == .NSURLSession ? 2 : 1).akamai.com/demo/tile-\(tileId).png")!
                
                // send via NSURLSession -> should use HTTP/2
                if api == .NSURLSession {
                    let task = self.session.dataTaskWithURL(url) { (data, res, error) in
                        self.evalResponse(res, data:data, error: error)
                        
                        finishedRequestsCount++;
                        if finishedRequestsCount == numRequests {
                            endMeassuring()
                        }
                    }
                    task.resume()
                    
                    // send via NSURLConnection -> should not use HTTP/2
                } else {
                    NSURLConnection.sendAsynchronousRequest(NSURLRequest(URL: url), queue: self.http1CallbackQueue) { (res, data, error) in
                        self.evalResponse(res, data:data, error: error)
                        
                        finishedRequestsCount++;
                        if finishedRequestsCount == numRequests {
                            endMeassuring()
                        }
                    }
                }
            }
        }
        
        self.delegate?.http2Tester(self, notifiesAboutEvent:"Completed \(numRequests) requests via \(api.rawValue) in \(duration) seconds.")
        
        return duration
    }
    
    /** Evaluates the response. Logs any errors and if it's a string response it checks whether the string "You are using HTTP/2 right now!" is contained in it. */
    private func evalResponse(response: NSURLResponse?, data: NSData?, error: NSError?) {
        if error != nil {
            self.delegate?.http2Tester(self, notifiesAboutEvent:"error: \(error!.localizedDescription): \(error!.userInfo)")
        } else if let data = data {
            if let str = NSString(data: data, encoding: NSUTF8StringEncoding) {
                let httpProtocol: String
                if str.containsString("You are using HTTP/2 right now!") {
                    httpProtocol = "HTTP/2"
                } else {
                    httpProtocol = "HTTP 1.1"
                }
                self.delegate?.http2Tester(self, notifiesAboutEvent:"Finished request to \(response!.URL!) - Used \(httpProtocol)")
            } else {
                //self.delegate?.http2Tester(self, notifiesAboutEvent:"Finished request to \(response!.URL!)")
            }
        }
    }
    
    /** Meassures the time between the invocation of the operationToMeassureBlock and the call of endMeassuring inside this block. It blocks the current thread until endMeassuring is called. This ensures a serial execution of tests. */
    private func meassure(operationToMeassureBlock: (endMeassuring: () -> ()) -> ()) -> NSTimeInterval {
        let semaphore = dispatch_semaphore_create(0)
        var duration = Double.infinity
        
        let starttime = NSDate().timeIntervalSince1970
        operationToMeassureBlock {
            duration = NSDate().timeIntervalSince1970 - starttime
            dispatch_semaphore_signal(semaphore)
        }
        
        dispatch_semaphore_wait(semaphore, DISPATCH_TIME_FOREVER)
        return duration
    }
}
