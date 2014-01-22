Misc iOS Programming Notes
==========================

[AVFoundation](https://developer.apple.com/library/ios/documentation/AudioVideo/Conceptual/AVFoundationPG/Articles/00_Introduction.html#//apple_ref/doc/uid/TP40010188-CH1-SW3)
--------------

#### AVFoundation *Fun*damentals
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

#### [Playback](https://developer.apple.com/library/ios/documentation/AudioVideo/Conceptual/AVFoundationPG/Articles/02_Playback.html#//apple_ref/doc/uid/TP40010188-CH3-SW1)

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

[**Bundle**](https://developer.apple.com/library/ios/documentation/CoreFoundation/Conceptual/CFBundles/AboutBundles/AboutBundles.html#//apple_ref/doc/uid/10000123i-CH100)
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
[VIEW CONTROLLER LIFECYCLE DIAGRAM!!](http://rdkw.wordpress.com/2013/02/24/ios-uiviewcontroller-lifecycle/)
-------------------------------------

![](http://rdkw.files.wordpress.com/2013/02/uiviewcontroller_lifecycle.jpg?w=630)


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

    [changingInstance
     addObserver:observingInstance
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
