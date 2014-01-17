Misc iOS Programming Notes
==========================

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

Core Data
---------

* A wrapper around a database so you only see objects, not DB

### [From Apple](https://developer.apple.com/library/ios/documentation/Cocoa/Conceptual/CoreData/Articles/cdBasics.html)

**Think of a managed object context as an intelligent scratch pad**.

1. You fetch objects from a persistent store, bringing temporary copies onto the scratch pad
2. They form an object graph (or a collection of object graphs).
3. If modify those objects without *saving* the changes, the persistent store
   doesn't change.

### [From Techotopia](http://www.techotopia.com/index.php/Working_with_iOS_7_Databases_using_Core_Data)

#### Managed Objects

* `NSManagedObject`
* Object you create to store data
* Like a DB row
* Maintained and managed by the **managed object context**
* You make one to store data, and you get one when you retrieve data

#### Managed Object Context

* Manages relationship between managed objects defined by
    * The managed object model
    * Your app
    * The underlying data
* You tell the context a bunch of things to do, then you tell it to execute them
    * E.g. save data, etc.

#### Managed Object Model

* Defines *entities*

#### Entity

* The **schema** for *managed objects*
* Can contain:
    * Attributes
        * E.g. name, phone number, etc.
    * Relationships
        * one-to-one
        * one-to-many
        * many-to-many
    * Fetched property
        * Weak, one-way relationships (??, no me importa)
    * Fetch request
        * Predefined query
        * Referenced to retrieve objects corresponding to the query

#### Persistent Store Coordinator

* Coordinates access to multiple persistent object stores
* A programmer needn't care much about this thing

#### Persistent Object Store

* Regardless of type, your code looks the same
* Disk-Based
    * SQLite (**default**)
    * XML
    * Binary
* Memory-Based
    * There aren't multiple options

#### Defining an Entity Description

* I'ma read this when I'm actually creating one of these,
  I think I'ma pass on that for now though.

### [Adding Core Data to existing project in XCode 4](http://stackoverflow.com/questions/6821719/adding-core-data-to-existing-project-in-xcode-4)

1. Create a new *Empty Project* with Core Data support
1. Copy over the relevant new code to your original project and compile
1. Add `#import <CoreData/CoreData.h>` to the `.pch` file or to the relevant `.m` files
1. Add the CoreData Framework via
    * Project --> Targets --> Summary --> `+` in Linked Frameworks and Libraries

### Getting the managedObjectContext out of the AppDelegate

    FocusTestAppDelegate *appDel = (FocusTestAppDelegate *)[UIApplication sharedApplication].delegate;
    NSManagedObjectContext moc = appDel.managedObjectContext;

### [Saving Image to Core Data](http://stackoverflow.com/questions/10311271/saving-image-to-core-data)

##### To save:

    NSData *imageData = UIImagePNGRepresentation(myUIImage);
    [newManagedObject setValue:imageData forKey:@"imageKey"];

##### To Retrieve Image:

    NSManagedObject *selectedObject = [[self fetchedResultsController] objectAtIndexPath:indexPath];
    UIImage *image = [UIImage imageWithData:[selectedObject valueForKey:@"imageKey"]];
    [[newCustomer yourImageView] setImage:image];

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
