Notes on iOS Programming
========================

Notes on `UIViewController`
-----------------------------------------
### From the [Class Reference](https://developer.apple.com/library/ios/documentation/uikit/reference/UIViewController_Class/Reference/Reference.html)

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

### From the [ViewController Programming Guide](https://developer.apple.com/library/IOS/featuredarticles/ViewControllerPGforiPhoneOS/Introduction/Introduction.html)

Notes from Lecture
------------------
* For a `UIView`, `self.hidden = YES;` means:
    1. it still has its place in the view hierarchy,
    2. it still belongs to the superview,
    3. it still lives in its frame,
    4. it **is not on screen**,
    5. it does not handle events.
    * He said "you probably won't need this for the homework, but you could."

