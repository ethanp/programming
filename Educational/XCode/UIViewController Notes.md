ALL Lifecycle Stuff
===================

* If you're loading from a Storyboard, and you need to initialize/configure objects
  that aren't in the Storyboard, do it in `awakeFromNib`
* If you're loading Programmatically, implement `init`
    * Call `[super init]`
* `awakeFromNib` is called after its outlet & action connections have been established
    * Must call `super`

* `viewDidLayoutSubviews` gets called whenever the `UIViewController`'s base
  `@prop view`'s bounds change.
    * First the bounds of the view and views down the hierarchy are changed, then this
      method is called.
    * However each subview is reponsible for readjusting its own layout in response to
      the bounds change.
    * The default implementation does nothing, it doesn't say that you need to call `super`

* The first time the ViewController's view object is accessed, `loadView` is called
    * By default this loads it from the *Storyboard*, else an empty `UIView` is assigned

* Then `viewDidLoad` is called

* `viewDidLoad` is called after the view controller has loaded its view
  hierarchy into memory.
    * Called regardless of whether the view hierarchy was loaded from a nib file or
      created programmatically in the `loadView` method.
    * You usually override this method to perform additional initialization on views that
      were loaded from 'nib' files (I think this means the storyboard now).


From the [ViewController Programming Guide](https://developer.apple.com/library/IOS/featuredarticles/ViewControllerPGforiPhoneOS/Introduction/Introduction.html)
===========================================

UIViews **perform drawing**
-----------------------------

* Group related views by placing them in a common superview.
    * Subviews are positioned and drawn relative to their superview.
        * When the superview moves, its subviews move with it.

* When a change to a property value is animated, the value gradually changes
  over a defined period of time until it reaches the new value.
    * A single animation can change multiple properties across multiple views
        * I guess you just add all the changes you want to the `animations:^{block}`
    * Animations are nice because instantaneous transitions confuse the user.

* Generally, in MVC, Views rarely understand the role they play in your app.
    * E.g. `UIButtons` don't understand what their function is, they just send a message
      to the controller saying they've been pressed.
    * Instead, your `UIViewController`s are the brains that tie your app’s views together.

Content ViewControllers Display Content
---------------------------------------

* Don't use multiple content view controllers to manage the same view hierarchy.
* Similarly, don't use a single content view controller to manage multiple screens’
  worth of content.

UITableViewController
---------------------

* Manages a *table view*, specifically for managing *tabular* data
* Subclass of `UIViewController`
* Adds support for tably behaviors like selection management and row editing
* Of course you can also *subclass it to add your own custom behaviors*
* Has an `@prop UITableView *tableView`

### Side Notes about UITableViews

* **Displays a list of items in a single column**
* Subclass of `UIScrollView` to allow vertical scrolling (only)
* Items are `UITableViewCell` objects
    * Cells have content
        * titles,
        * images,
        * accessory views
            * something you click on to see more detail about the item
            * or sliders/switches
* Have an edit mode that can be switched on by user
    * Managed by a `UITableViewDelegate`
* Has sections, each with its set of rows accessibly by indexing the section

Container ViewControllers
-------------------------
***Contain/Arrange Content of its Children's ViewControllers***

### UINavigationController Presents *hierarchical* data

* Manages a *stack-based* collection of content ViewControllers
* Manages a navigation bar displayed at the top of its children's views

### UITabBarControllers Divide apps into distinct modes of operation

* **Selecting a tab causes the tab bar controller to display the associated
  ViewController’s view on the screen.**
* Each tab might display different data, or the same data in different ways.

### iPad Only: UISplitViewController for seeing multiple ViewControllers at once

* E.g. Mail app might have list view on side and detail view for main part
* Maybe in portrait mode the list view is displayed in a popover

### UIPageViewController for flipping between pages of content

* Each page is provided by a content ViewController

Coordination between Content Controllers
----------------------------------------

* ViewControllers communicate with their parents using `delegation`
    * When the source view controller configures the destination view
      controller’s other properties, it is also expected to provide an object
      that implements the delegate’s protocol.
    * This is so the destination ViewController doesn't have to know much about its
      parent, making both more re-usable

Storyboards Help You Design Your User Interface
-----------------------------------------------

* When you build your project, the storyboards in your project are processed
  and copied into the app bundle, where they are loaded by your app at runtime.

### Storyboard Terminology

* **Scene** — ViewController and its associated View hierarchy
* **Relationship** — arrow from one *scene* to another
* **Containment** — parent-child relationship between two scenes
* **Segue** — visual transition from one scene to another
    * Triggered by **actions**
    * **Push** — add destination to `NavigationController`'s *stack*
    * **Modal** — present destination
    * **Popover** — display in popover (iPad)
    * **Custom** — you design your own transition
    * **What Happens:**
        * The destination is instantiated by iOS
        * The source is called to configure the destination
        * The transition is performed

[Using View Controllers in Your App](https://developer.apple.com/library/IOS/featuredarticles/ViewControllerPGforiPhoneOS/UsingViewControllersinYourApplication/UsingViewControllersinYourApplication.html)
------------------------------------

* Either use **storyboards** or **alloc/init it programmatically**
* Be sure to give all segues identifiers so they can be invoked

### The Programmatic Route

* Trigger a segue (located in the storyboard) by calling the source's
  `performSegueWithIdentifier:sender:`
* There are details and sample code here for not using the storyboard at all, but they
  highly don't recommend it


[Creating Custom Content View Controllers](https://developer.apple.com/library/IOS/featuredarticles/ViewControllerPGforiPhoneOS/BasicViewControllers/BasicViewControllers.html)
------------------------------------------

* **All apps must have at least one.**
* Subclass `UIViewController`

### Anatomy of a Content View Controller

* Should manage resources efficiently
* Should properly layout its view hierarchy
* Handle Events, making necessary changes to views/data
    * Includes system notifications, timers, and events specific to the app
* Should **expose the fewest number of properties and methods necessary to allow
  its collaborators to communicate with it**
    * This makes it much easier to modify and reuse without modifying collaborators


### Designing a Content View Controller

Before you start to write the ViewController:

* Know what it does and what info it displays
* Know how it collaborates with other ViewControllers
* Don't need to know what it will look like or how to implement it yet

Now design the public interface

* Definitely try to to use the *Storyboard*
* Know all the ways the controller might be instantiated
* Know where it gets its data (user/other controller/DB/etc.)
* Know what it does with data it gets (sends to other controller/etc.)
    * Use a *data controller* to add data records
        * So when you get data, send it to the *data controller's* interface
* Know what it allows the user to do (view/create/edit data, navigate, perform tasks)
    * Remember to expose as little as possible of the options in here to the outside
* Know what it looks like onscreen
    * Including portrait and landscape, and how it switches between them
* Know how it collaborates with other Controllers
    * How it gets info from/to parents/children, container controllers
        * External API usage and exposure
        * Delegate protocols
* What outlets and actions to maintain
    * Keep these private if other classes don't need to access them
        * This means you can hook them up in the *Interface Builder*, but other
          classes can't see them

[Resource Management in View Controllers](https://developer.apple.com/library/IOS/featuredarticles/ViewControllerPGforiPhoneOS/ViewLoadingandUnloading/ViewLoadingandUnloading.html)
-----------------------------------------

* Instantiate only the parts that are needed
* Purge caches and large objects when available memory runs low
* Prefer lazy allocation
* If you're loading from a Storyboard, and you need to initialize/configure objects
  that aren't in the Storyboard, do it in `awakeFromNib`
* If you're loading Programmatically, implement `init`
    * Call `[super init]`


### Loading a ViewController's View from a Storyboard

From the [Class Reference](https://developer.apple.com/library/ios/documentation/uikit/reference/UIViewController_Class/Reference/Reference.html)
==========================

All displayed content is managed by a ViewController or a group of
ViewControllers coordinating with each other.

ViewControllers provide the skeletal framework on which you build your apps.

Where necessary, a view controller:

* resizes and lays out its views
* adjusts the contents of the views
* acts on behalf of the views when the user interacts with them

If the view controller’s view does not handle an event, the view controller has
the option of handling the event or it can pass the event to the superview.

If you cannot define your views in a storyboard or a nib file, override the
loadView method to manually instantiate a view hierarchy and assign it to the
view property.

If you create your views manually, you should never use the same view objects
with multiple view controllers.

### Autoresizing, Autolayout, Device Orientation

* When creating the `View`s for your `View` hierarchy, you should always set
  the `autoresizing` properties of your `View`s.

* When a `ViewController` is displayed on screen, its root `View` is typically
  resized to fit the available space.

* You can **configure the autoresizing properties** in *Interface Builder* using
  the *inspector* window or **programmatically** by modifying the
  `autoresizesSubViews` and `autoresizingMask` properties of each `View`.

* Setting these properties is also important if your `ViewController` supports
  both **portrait and landscape** orientations.

    * During an orientation change, the system uses these properties to
      reposition and resize the `View`s automatically to match the new orientation.

    * If your `ViewController` supports auto layout and is a child of another
      `View` controller, you should call the `View`’s
      `setTranslatesAutoresizingMaskIntoConstraints: method to disable these constraints.

* When a rotation occurs for a visible view controller, the

    * `willRotateToInterfaceOrientation:duration:`,

    * `willAnimateRotationToInterfaceOrientation:duration:`, and

    * `didRotateFromInterfaceOrientation:` methods are called during the rotation.

        * **Override this method to perform additional actions
          immediately after the rotation**.

            * For example, you might use this method to reenable view
              interactions or start media playback again.  By the time this
              method is called, the `interfaceOrientation` property is already
              set to the new orientation.

        * **Must call `super`** at some point.

* The `viewWillLayoutSubviews` method is also called after the view is resized
  and positioned by its parent.

* If a view controller is not visible when an orientation change occurs, then
  the rotation methods are never called.

* However, the `viewWillLayoutSubviews` method is called when the view becomes
  visible.

* Your implementation of this method can call the `statusBarOrientation` method
  to determine the device orientation.

Notes from Lecture
------------------
* For a `UIView`, `self.hidden = YES;` means:
    1. it still has its place in the view hierarchy,
    2. it still belongs to the superview,
    3. it still lives in its frame,
    4. it **is not on screen**,
    5. it does not handle events.
    * He said "you probably won't need this for the homework, but you could."

