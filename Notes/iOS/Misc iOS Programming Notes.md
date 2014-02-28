latex input:		mmd-article-header
Title:		Misc iOS Programming Notes
Author:		Ethan C. Petuchowski
Base Header Level:		2
latex mode:		memoir
Keywords:		Stuff
CSS:		http://fletcherpenney.net/css/document.css
xhtml header:		<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
</script>
copyright:			2014 Ethan C. Petuchowski
latex input:		mmd-natbib-plain
latex input:		mmd-article-begin-doc
latex footer:		mmd-memoir-footer

# The [URL Loading System](https://developer.apple.com/library/mac/documentation/Cocoa/Conceptual/URLLoadingSystem/URLLoadingSystem.html)

### NSURL

An `NSURL` object lets you manipulate URLs and the resources they reference. You
can use an `NSURL` object to refer to a file, which is the preferred way to do
so. An `NSURL` object is composed of two parts—a potentially nil base URL and a
string that is resolved relative to the base URL. An `NSURL` object whose string
is fully resolved without a base is considered absolute; all others are
considered relative. If you are creating `NSURL` objects using file system paths,
you should use `fileURLWithPath:` or `initFileURLWithPath:`, which handle the
subtle differences between URL paths and file system paths.

Known to work:

    NSURL *url = [NSURL URLWithString:@"http://www.vickiwenderlich.com/2011/06/game-art-pack-monkey-platformer/"];

### UIWebView

You use the UIWebView class to embed web content in your application. To do so,
you simply create a UIWebView object, attach it to a window, and send it a
request to load web content. You can also use this class to move back and
forward in the history of webpages, and you can even set some web content
properties programmatically.

Snazzy trick to get HTML from web view

    NSString *html = [webView stringByEvaluatingJavaScriptFromString:@"document.documentElement.outerHTML"];


# Assorted Things to Note

### Screen Size

* **iPhone 5** --- 320 x 568 pts

# NSUserDefaults

### [mobile.tuts+ Tutorial](http://mobile.tutsplus.com/tutorials/iphone/nsuserdefaults_iphone-sdk/)

* **Save user data, settings, and properties** for your application
    * E.g. profile image, or color scheme
* **Saved persistently** in the iOS "defaults system"
* You can save the "standard" set of class types
    * `NSData, NSString, NSNumber, NSDate, NSArray, NSDictionary`

Storing the data

    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:firstName forKey:@"firstName"];
    [defaults setObject:lastName forKey:@"lastname"];
    [defaults setInteger:/*(int)*/age forKey:@"age"];
    [defaults setObject:imageData forKey:@"image"];
    [defaults synchronize];
    NSLog(@"Data saved");

Retrieving the data

    - (void)viewDidLoad
    {
        // Get the stored data before the view loads
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        NSString *firstName = [defaults objectForKey:@"firstName"];
        NSString *lastName = [defaults objectForKey:@"lastname"];
        int age = [defaults integerForKey:@"age"];
        NSString *ageString = [NSString stringWithFormat:@"%i",age];
        NSData *imageData = [defaults dataForKey:@"image"];
        UIImage *contactImage = [UIImage imageWithData:imageData];
        /* update UI with the retrieved data, then */
        [super viewDidLoad];
    }


# [Implementing NSCoding](https://www.mikeash.com/pyblog/friday-qa-2010-08-12-implementing-nscoding.html)

Objects in memory can't be directly saved or moved to other programs. They
contain data, such as pointers, which are only valid in the context of your
process's memory space. Move the contents of an object into another program and
all of those pointers suddenly make no sense. Serialization is the process of
converting the non-portable, in-memory representation of an object into a
portable stream of bytes that can be stored and moved between processes.

The rest must be some other time.

# Notifications

* Per-process singleton, `NSNotificationCenter`
* Object posts notification there, which broadcasts it to anyone who registered
  to receive it

### To Observe

Get a `NSNotificationCenter` instance and send it

    [instance addObserver:(id)observer // you (the object to get notified)
                 selector:(SEL)methodToSendIfSomethingHappens
                     name:(NSString *)name // what you’re observing (a constant somewhere)
                   object:(id)sender] // whose changes you’re interested in (nil is anyone’s)

And implement

    - (void)methodToSendIfSomethingHappens:(NSNotification *)notif


#### E.g.
Put this in `viewDidAppear:`

    NSNotificationCenter *center = [NSNotificationCenter defaultCenter];
    [center addObserver:self
               selector:@selector(documentChanged:)
                   name:UIDocumentStateChangedNotification
                 object:self.document];

Put this in `viewWillDisappear:`

    [center removeObserver:self];

### To Post

First make a global string constant for the name

    NSString *PrefixGlobalStringNotificationName = @"NameOfMyNotification";

Now post the notification that looks like (some variation of the following header)

    [[NSNotificationCenter instance]
     postNotificationName:PrefixGlobalStringNotificationName
     object:??
     userInfo:??]

# Delegates

The **delegate is the object informed when specific events happen to the delegator**.
This gives the delegate the chance to respond to whatever happened.

In other words, it "declares that it is registered as an observer of the
notifications posted by the observing object."

Similarly, a data sourcer asks its **data source** for data it needs.

###Implementation ([Docs](https://developer.apple.com/library/ios/documentation/general/conceptual/CocoaEncyclopedia/DelegatesandDataSources/DelegatesandDataSources.html))

#### Initialize
**Delegator.h**

    - (id)delegate;
    - (void)setDelegate:(id)newDelegate;

**Delegator.m**

    - (id)delegate { return delegate; }
    - (void)setDelegate:(id)newDelegate { delegate = newDelegate; }

#### Ask delegate whether `operationShouldProcede`
**Delegator.m**

    - (void)someMethod {
        if ( [delegate respondsToSelector:@selector(operationShouldProceed)] ) {
            if ( [delegate operationShouldProceed] ) { /*ok, so do this*/ } } }

[About iOS App Programming](https://developer.apple.com/library/ios/documentation/iPhone/Conceptual/iPhoneOSProgrammingGuide/Introduction/Introduction.html)
---------------------------

> This document is the starting point for creating iOS apps. It describes the
> fundamental architecture of iOS apps, including how the code you write fits
> together with the code provided by iOS. This document also offers practical
> guidance to help you make better choices during your design and planning
> phase and guides you to the other documents in the iOS developer library that
> contain more detailed information about how to address a specific task.

> The contents of this document apply to all iOS apps running on all types of
> iOS devices, including iPad, iPhone, and iPod touch. The starting point for
> any new app is identifying the design choices you need to make and
> understanding how those choices map to an appropriate implementation.

[Model View Controller](https://developer.apple.com/library/ios/documentation/General/Conceptual/CocoaEncyclopedia/Model-View-Controller/Model-View-Controller.html)
---------------------

### Advantages

* More **reusable** objects
* Better-defined **interfaces**
* More easily **extensible**

### Sometimes, Models must be Observed

* The Model doesn't *only* update when it receives stuff from the View via
  the Controller, it may also
    * Receive stuff from *network*
    * Have stuff that happens on a *timer*
* This is when you need to add *observers* and notifications or something

[Document-Based Apps](https://developer.apple.com/library/ios/documentation/DataManagement/Conceptual/DocumentBasedAppPGiOS/Introduction/Introduction.html)
---------------------

Manage multiple documents, with each document containing a unique set of data
that is stored in a file located either in the application sandbox or in
iCloud.

Must create a subclass of UIDocument that loads document data into its
in-memory data structures and supplies UIDocument with the data to write to the
document file.

UIDocument takes care of many details related to document management for you.
Besides its integration with iCloud, UIDocument reads and writes document data
in the background so that your application’s user interface does not become
unresponsive during these operations. It also saves document data automatically
and periodically, freeing your users from the need to explicitly save.

Instances of subclasses of `UIDocument` are "model controllers". They manage
model objects that represent what the user is viewing and editing. A document
object is in turn managed by a view controller that presents a document to
users.

NSCoding
----------
[NSHipster 2013](http://nshipster.com/nscoding/)

[Ray Wenderlich 2010](http://www.raywenderlich.com/1914/nscoding-tutorial-for-ios-how-to-save-your-app-data)

For apps with heavy data requirements, `Core Data` is often the best way to go.
However, for apps with light data requirements, `NSCoding` with `NSFileManager` can
be a nice way to go because it’s such a simple alternative.

You can use `NSCoding` to persist your normal app data, and use `NSFileManager` to
store large files for efficiency.

**NSCoding is a protcol** that *you can implement on your data classes to support
encoding and decoding your data into a data buffer, which can then be persisted
to disk*. Implementing NSCoding is actually ridiculously easy.

We have to include the `<NSCoding>` protocol, and implement two methods

* `(void)encodeWithCoder:(NSCoder *)encoder`
* `(id)initWithEncoder:(NSCoder *)decoder`

##### Example

    @prop NSString *title;
    @prop float rating;
    ...
    - (void) encodeWithCoder:(NSCoder *)encoder {
        [encoder encodeObject:self.title forKey:@"title"];
        [encoder encodeFloat:self.rating forKey:@"rating"];
    }

    - (id)initWithCoder:(NSCoder *)decoder {
        NSString *title = [decoder decodeObjectForKey:@"title"];
        float rating = [decoder decodeFloatForKey:@"rating"];
        return [self initWithTitle:title rating:rating];
    }

##### Moving on

We provide methods to:

* Load data from disk
* Save data to disk
* Delete document
* Create new docuement


*corner*        | Core Data | NSKeyedArchiver
----------------|-----------|----------------
Persists State  | Yes       | Yes
Pain in the Ass | Yes       | No
Entity Modeling | Yes       | No
Querying        | Yes       | No
Speed           | Fast      | Slow
Serialization Formats | SQLite, XML, NSData | NSData
Migrations      | Automatic | Manual
Undo Manager    | Automatic | Manual

[AVFoundation](https://developer.apple.com/library/ios/documentation/AudioVideo/Conceptual/AVFoundationPG/Articles/00_Introduction.html)
--------------

#### AVFoundation Fundamentals
**AVFoundation** is one of several **frameworks** that you can use to **play and create
time-based audiovisual media**. It provides an Objective-C interface you use to
work on a detailed level with time-based audiovisual data. For example, you can
use it to **examine, create, edit, or reencode media files**. You can also get
input streams from devices and *manipulate video during realtime capture and playback*.

**AVAsset** is an **abstract class** to represent **timed audiovisual media such as
videos and sounds**. Each asset contains a collection of tracks that are intended
to be presented or processed together, each of a uniform media type, including
but not limited to audio, video, text, closed captions, and subtitles.

The primary class that the AV Foundation framework uses to represent media is AVAsset.
AVAsset provides information about the collection as a whole, such as its
title, duration, natural presentation size, and so on. AVAsset is not tied to
particular data format. AVAsset is the superclass of other classes used to
create asset instances from media at a URL.

Each of the individual pieces of media data in the asset is of a uniform type
and called a track. In a typical simple case, one track represents the audio
component, and another represents the video component; in a complex
composition, however, there may be multiple overlapping tracks of audio and
video. Assets may also have metadata.

A vital concept in AV Foundation is that **initializing an asset or a track does
not necessarily mean that it is ready for use**. It may require some time to
calculate even the duration of an item (an MP3 file, for example, may not
contain summary information). Rather than blocking the current thread while a
value is being calculated, **you ask for values and get an answer back
asynchronously through a callback that you define using a block**.

**To create thumbnail images of video presentations**, you initialize an instance
of AVAssetImageGenerator using the asset from which you want to generate thumbnails.

Recording input from cameras and microphones is managed by a capture session. A
capture session coordinates the flow of data from input devices to outputs such
as a movie file. You can configure multiple inputs and outputs for a single
session, even when the session is running. You send messages to the session to
start and stop data flow.  In addition, you can use an instance of preview
layer to show the user what a camera is recording.

Callouts from AV Foundation—invocations of blocks, key-value observers, and
notification handlers—are not guaranteed to be made on any particular thread or
queue. Instead, AV Foundation invokes these handlers on threads or queues on
which it performs its internal tasks.

#### [Playback](https://developer.apple.com/library/ios/documentation/AudioVideo/Conceptual/AVFoundationPG/Articles/02_Playback.html)

#### [Media Capture](https://developer.apple.com/library/ios/documentation/AudioVideo/Conceptual/AVFoundationPG/Articles/04_MediaCapture.html)

An `AVCaptureDevice`, an `AVCaptureInput`, an `AVCaptureOutput`, and an `AVCaptureSession`

A connection between a capture input and a capture output in a capture session
is represented by an `AVCaptureConnection` object.

You can add and remove outputs as you want while the session is running.

[File System](https://developer.apple.com/library/ios/documentation/FileManagement/Conceptual/FileSystemProgrammingGuide/FileSystemOverview/FileSystemOverview.html)
-------------

* The file systems in OS X and iOS are both based on the UNIX file system.
* All of the disks attached *in any way* to the computer contribute space to
  create a **single collection of files**.
* Directories create a hierarchical organization.
* There are differences in the way OS X and iOS each system organize apps and user data.

* Each app has its own "sandbox" which includes
    * The `AppName.app`
    * `Documents/`
    * `Library/`
    * `tmp/`

### AppName.app

[**Bundle**](https://developer.apple.com/library/ios/documentation/CoreFoundation/Conceptual/CFBundles/AboutBundles/AboutBundles.html)
directory containing the app itself. Don't write to this or your app will not be
allowed to launch anymore.

### Documents/

Directory for app that can't be recreated on load by your app, e.g. user-generated
content. It is backed up by iTunes.

#### Documents/Inbox

`Mail` puts email attachments associated with your app in here. Your app may only
read and delete files in here unless you move them somewhere else.

### Library/

Files that are *not* user-data (stuff you don't even want them to know about).
There are several standard subdirectories one may want to use.

### tmp/

Temporary files that needn't persist between launches of the app.
You should still delete them when you're done with them,
though the system may also do so when your app's not running.

### OS X and iOS provide support for encrypting files on disk

### Thread Safety

For most tasks, it is safe to use the default `NSFileManager` object
simultaneously from multiple background threads. Unless your tasks interact
with the file manager's delegate, in which case use a *unique* instance from
one thread at a time.

Don't use the same `NSFileHandle`'s or `NSData`'s from multiple threads at the
same time.

`NSURL`s and `NSString`s are immutable, so you can use them in multiple threads
simultaneously.

`NSEnumerator`s (i.e. "Collections", e.g. `NSArray`, `NSDictionary`, `NSSet`)

Bundle
------

* A bundle is a directory in the file system that groups executable code and
  related resources such as images and sounds together in one place.
* In iOS and OS X, applications, frameworks, plug-ins, and other types of
  software are bundles.
* A bundle is a directory with a standardized hierarchical structure that holds
  executable code and resources used by that code.
* Foundation and Core Foundation include facilities for locating and loading
  code and resources in bundles.
* Most types of Xcode projects create a bundle for you when you build the executable.

@class AVCaptureSession
-----------------------
Coordinates flow of data from AV input devices to outputs.

1. `alloc init` it
2. Add `AVCaptureDeviceInput`(s)
3. Add output(s), e.g. `AVCapture(StillImage|MovieFile)Output`
4. Invoke `startRunning` to start flow of data from inputs to outputs
5. Invoke `stopRunning` to stop the flow (I have it in `viewDidDisappear`)

<a name="lifecycle_diagram"></a>
[VIEW CONTROLLER LIFECYCLE DIAGRAM!!](http://rdkw.files.wordpress.com/2013/02/uiviewcontroller_lifecycle.jpg?w=630)
-------------------------------------

![Must be removed before LaTeXiFying][fig1]

[fig1]: http://rdkw.files.wordpress.com/2013/02/uiviewcontroller_lifecycle.jpg?w=630 "Title"


viewWillAppear: vs. viewDidLoad
-------------------------------

#### See [diagram](#lifecycle_diagram)

* **`viewDidLoad`**
    * When a view has finished loading for the first time,
      immediately after the `init` method
    * Before the view is drawn on screen
        * Don't do calculations of geometry and visuals of the view
    * Calculations you have to do only once
        * E.g. set `UILabel` texts

* **`viewWillAppear** — called every time the view appears
    * Reset where in the `scrollView` the user is looking at every time the view appears
    * Don't put code that takes a lot of main-thread usage in here
    * Called again whenever ___ is displayed and removed
        * Alert View / Actionsheet
        * UIActivityController
        * Popover / Modal View


[Key-Value Coding](https://developer.apple.com/library/ios/documentation/Cocoa/Conceptual/KeyValueCoding/Articles/KeyValueCoding.html)
------------------

Key-value coding is a mechanism for indirectly accessing an object’s
"properties" (instance variables) using string identifiers. It underpins
several mechanisms in Cocoa, such as Core Data, application scriptability (OS
X), the bindings technology (OS X), and 'declared properties'.  Key-value
coding can simplify your program code.

Key's are located using the variable name or its accessor method as a string.

`NSKeyValueCoding` is an *informal protocol*, allowing Apps to access
`properties` (instance variables) of an `object` inderictly by *name* ('key'),
rather than directly through an accessor method.

The most important methods are `valueForKey:` (getter), and `setValue:forKey:` (setter).
They have default implementations that work fine.

The easiest way to make a property KVC is to declare that property (lol),
though it's more complicated for "to-many" relationships.

[Key-Value Observing](https://developer.apple.com/library/ios/documentation/Cocoa/Conceptual/KeyValueObserving/KeyValueObserving.html)
---------------------

* A mechanism that allows objects to be notified of changes to specific properties
  of other objects
* Particularly useful for communication between model and controller layers of the App
    * The controller would observe properties of model objects
    * Therefore view observes model through controller
    * Also, one model might observe another model, or even itself

Here's how you set it up

**Step 1.** Register as an observer

    .[changingInstance addObserver:observingInstance
                        forKeyPath:@"observedProperty"
                           options:(NSKeyValueObservingOptionNew|NSKeyValueObservingOld)
                           context:NULL];

* **Note:** Only the *instances* are now connected, not the classes.

The options say that the `obserValue...` method will be passed a dictionary containing
the new and old values. More on that in the bookmarked page.

**Step 2.** Implement the method to describe how observing class responds to
            the change notification

    - (void) observeValueForKeyPath:(NSString *)keyPath
                           ofObject:(id)object
                             change:(NSDictionary *)change
                            context:(void *)context
    {
        [super observe...]; // if superclass implements it too

        // custom implementation
    }

This gets called whenever the value of the observed property is changed, or a
key upon which it depends is changed.

Unlike using `NSNotificationCenter`, there is no central object that provides
change notification for all observers. Instead, notifications are sent directly
to the observing objects when changes are made. `NSObject` provides this base
implementation of key-value observing, and you should rarely need to override
these methods.
