Objective C Notes
=================

[Atomic vs nonatomic properties](http://stackoverflow.com/questions/588866/atomic-vs-nonatomic-properties)
--------------------------------

Assuming that you are allowing setters/getters to be @synthesized, the
atomic vs. non-atomic changes their generated code. If you are writing your own
setter/getters, atomic/nonatomic/retain/assign/copy are merely advisory.

With "atomic", the synthesized setter/getter will ensure that a whole value is
always returned from the getter or set by the setter, regardless of setter
activity on any other thread. That is, if thread A is in the middle of the
getter while thread B calls the setter, an actual viable value -- an
autoreleased object, most likely -- will be returned to the caller in A.

In nonatomic, no such guarantees are made. Thus, nonatomic is considerably
faster than "atomic".

What "atomic" does not do is make any guarantees about thread safety. If thread
A is calling the getter simultaneously with thread B and C calling the setter
with different values, thread A may get any one of the three values returned.
Ensuring data integrity -- one of the primary challenges of multi-threaded
programming -- is achieved by other means. Atomicity does not generally
contribute to thread safety.

[Blocks](https://developer.apple.com/library/ios/documentation/Cocoa/Conceptual/ProgrammingWithObjectiveC/WorkingwithBlocks/WorkingwithBlocks.html#//apple_ref/doc/uid/TP40011210-CH8-SW1)
--------

    // declare
    void (^simpleBlock)(void) = ^{
        NSLog(@"This is a block");
    };

    // call
    simpleBlock();

    // specifying the return type is optional
    double (^multiplyTwoValues)(double, double) =
        ^(double firstValue, double secondValue) {
            return firstValue * secondValue;
        };

    double result = multiplyTwoValues(2,4);

##### Best practice:
* use only one block argument to a method
* the block argument should come last (to make the method call easier to read)
* declare `__weak wkSlf = self` before the block, and use it inside instead of `self`

[Protocols](https://developer.apple.com/library/ios/documentation/General/Conceptual/DevPedia-CocoaCore/Protocol.html#//apple_ref/doc/uid/TP40008195-CH45-SW1)
----------

A protocol declares a programmatic interface that any class may choose to
implement. Protocols make it possible for two classes distantly related by
inheritance to communicate with each other to accomplish a certain goal. They
thus offer an alternative to subclassing.

### [Working with Protocols](https://developer.apple.com/library/ios/documentation/Cocoa/Conceptual/ProgrammingWithObjectiveC/WorkingwithProtocols/WorkingwithProtocols.html#//apple_ref/doc/uid/TP40011210-CH11-SW1)

In the world of object-oriented programming, it’s important to be able to
define a set of behavior that is expected of an object in a given situation. As
an example, a table view expects to be able to communicate with a data source
object in order to find out what it is required to display. This means that the
data source must respond to a specific set of messages that the table view
might send.

The data source could be an instance of any class, such as a view controller (a
subclass of NSViewController on OS X or UIViewController on iOS) or a dedicated
data source class that perhaps just inherits from NSObject. In order for the
table view to know whether an object is suitable as a data source, it’s
important to be able to declare that the object implements the necessary
methods.

##### Syntax to define a protocol:

    @protocol ProtocolName
    // list of methods and properties
    @end

Can include instance methods, class methods, and @properties

#### Simple E.g.

The minimum information needed by a `PieChartView` might be the number of
segments, the relative size of each segment, and the title of each segment.

##### Syntax to declare a Delegate @property:

    @interface XYZPieChartView : UIView

    @property (weak) id <XYZPieChartViewDataSource> dataSource;
    ...

    @end

This example declares a weak property for a generic object pointer that
conforms to the `XYZPieChartViewDataSource` protocol.

##### Warning: Delegate properties should usually be `(weak)` pointers

#### @optional @protocol methods

You must check whether an object implements that method before attempting to
call it, so use

    if ([protoCaller respondsToSelector:@selector(optlMethod)])

#### Protocol Inheritance

This is a way of indicating that any object that adopts the custom protocol
will also provide implementations for each of the *super protocol's* methods.

You should probably **always inherit from NSObject's protocol** to give you
the freedom to call its methods on the implementer.

    @protocol MyProtocol <NSObject>
    ...
    @end

[Categories](https://developer.apple.com/library/ios/documentation/General/Conceptual/DevPedia-CocoaCore/Category.html#//apple_ref/doc/uid/TP40008195-CH5-SW1)
----------

You typically use a category to add methods to an existing class, such as one
defined in the Cocoa frameworks. The added methods are inherited by subclasses
and are indistinguishable at runtime from the original methods of the class.

You can also use categories of your own classes to:

* Distribute the implementation of your own classes into separate source
  files. For example, you could group the methods of a large class into several
  categories and put each category in a different file.
* Declare private methods.

You add methods to a class by declaring them in an interface file under a
category name and defining them in an implementation file under the same name.
The category name indicates that the methods are an extension to a class
declared elsewhere, not a new class.

##### Declaration

The declaration of a category interface looks very much like a class interface
declaration—except that the category name is listed within parentheses after
the class name and the superclass isn’t mentioned. A category must import the
interface file for the class it extends:

    #import "SystemClass.h"

    @interface SystemClass (CategoryName)
    // method declarations
    @end

A common naming convention is that the base file name of the category is the
name of the class the category extends followed by “+” followed by the name of
the category. This category might be declared in a file named
`SystemClass+CategoryName.h`.

##### Implementation

If you use a category to extend a class to which you don’t have source code, or
to distribute the implementation of your own class, you put the implementation
in a file named `<ClassName>+CategoryName.m`. As usual, the implementation
imports its own interface. For example:

    #import "SystemClass+CategoryName.h"

    @implementation SystemClass ( CategoryName )
    // method definitions
    @end

If your category's **method-name clashes** with any other method belonging to
the class, the resulting behavior is undefined!

#### [Class extensions](https://developer.apple.com/library/ios/documentation/Cocoa/Conceptual/ProgrammingWithObjectiveC/CustomizingExistingClasses/CustomizingExistingClasses.html#//apple_ref/doc/uid/TP40011210-CH6-SW3)

* This is simply the real name for the area at the top of the .m file with the whole
   `@interface ClassName ()`
* Also called "anonymous categories" (because the parens are empty)

@property
---------

Setter and Getter auto-synthesized [except with `(readonly)`]

##### Dot notation
"purely a convenient wrapper around accessor method calls."

* `a.b = c` is *exactly* the same as `[a setB:c]`

##### ivar (instance variable)

    @interface Class : NSObject {
        BOOL instanceVar;
    }

does *not* create the setter/getter, and does *not* add it to the ARC system.

Forward Declarations
--------------------

From [StackOverflow](http://stackoverflow.com/questions/11532888/objective-c-protocol-forward-declarations)

**You cannot forward declare a superclass or a protocol that it conforms to.**

* In those cases, you must include the header. This is so that the moethods and
  properties of those items actually become a part of your class.

**You *can* forward declare a class that will be the type of one of your
variables.**

* This is because object pointers are all the same size and there is
  no difference between different types of object pointers at runtime.

Variable Declarations in header files
-------------------------------------

From [StackOverflow](http://stackoverflow.com/questions/9732481/declaring-variable-above-vs-below-interface)

##### Global variable
*Bad Idea*

    int helloness;
    @interface test : NSObject
    @end


##### Instance variable

    @interface test : NSObject {
        int helloness;
    }
    @end

Forward private method declarations in the private category
-----------------------------------------------------------

This used to be required, but [now it is not](http://stackoverflow.com/questions/9414410/private-method-declaration-objective-c)

> As of the LLVM Compiler version shipped with Xcode 4.3, if you try to call a
> method that the compiler has not previously seen, it will look in the rest of
> the current @implementation block to see if that method has been declared
> later. If so, then it uses that, and you don't get a warning.
