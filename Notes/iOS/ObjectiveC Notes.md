Asynchronous Programming
========================

Resources I'm quoting/paraphrasing/notetaking-upon here:

* [NSHipster](http://nshipster.com/nsoperation/)
* [The Docs](https://developer.apple.com/library/mac/documentation/Cocoa/Reference/NSOperation_class/Reference/Reference.html)
* [From Concurrency Guide](https://developer.apple.com/library/mac/documentation/General/Conceptual/ConcurrencyProgrammingGuide/OperationObjects/OperationObjects.html)
* [Ray Wenderlich on GCD](http://www.raywenderlich.com/4295/multithreading-and-grand-central-dispatch-on-ios-for-beginners-tutorial)
* [Ray Wenderlich on NSOperation](http://www.raywenderlich.com/19788/how-to-use-nsoperations-and-nsoperationqueues)

The traditional way for an application to use multiple cores is to create
multiple threads. However, as the number of cores increases, there are problems
with threaded solutions. The biggest problem is that threaded code does not
scale very well to arbitrary numbers of cores. You cannot create as many
threads as there are cores and expect a program to run well. The amount of work
performed by a single application also needs to be able to scale dynamically to
accommodate changing system conditions.

Instead of relying on threads, OS X and iOS take an asynchronous design
approach to solving the concurrency problem. Typically, this work involves
acquiring a background thread, starting the desired task on that thread, and
then sending a notification to the caller (usually through a callback function)
when the task is done. OS X and iOS provide technologies to allow you to
perform any task asynchronously without having to manage the threads yourself.

On iOS, the methods you’re used to implementing (like viewDidLoad, button tap
callbacks, etc.) all run on the main thread. You don’t want to perform time
intensive work on the main thread, or else you’ll get an unresponsive UI.

##### Killing a thread is a no-no, according to everyone

> You should not ever need to "kill" a thread. You should signal to the thread
that it should exit. The thread itself should be structured so that it
periodically checks for this notification from the main UI thread. The
notification can be as simple as a global flag, or some other data that both
threads can reference.

NSOperation
-----------

### Overview

Whereas dispatch queues always execute tasks in first-in, first-out order,
operation queues take other factors into account when determining the execution
order of tasks. You configure dependencies when defining your tasks and can use
them to create complex execution-order graphs for your tasks. The tasks you
submit to an operation queue must be instances of the NSOperation class.
Operation objects generate key-value observing (KVO) notifications, which can
be a useful way of monitoring the progress of your task.

### Notes:

* Represents a single unit of computation wrapped up into an object
* Good for repeatable, structured, long-running tasks that return processed data
    * E.g. network requests, image resizing, language processing
* Use `NSOperationQueue` which is a priority queue for objects that inherit
  from `NSOperation`
    * `queuePriority` is settable on `NSOperation` objects
        * E.g. `NSOperationQueuePriorityVeryHigh`, `NSOperationQueuePriorityLow`, etc.
    * It executes operations concurrently, according to its settable
      `maxConcurrentOperationCount`
* Start it by calling its `-start` method, or by adding it to an `NSOperationQueue`
  which starts it automatically
* `NSOperations` are *state machines* that go from `isReady -> isExecuting -> isFinished`
    * But these are determined implicitly by KVO notifications on those keypaths
        * So when your `NSOperation` is ready to be executed, make it send a KVO
          notification for the `isReady` keypath, whose corresponding property
          then returns `YES`
* When an `NSOperation` finishes, it will execute its `@prop Block completionBlock`
  exactly once. This is a good way to customize its behavior within a Model or VC
* *This is quite cool:* you can express *depencies* between `NSOperations`
    * An operation won't start until all of its dependencies return `YES` to `isFinished`

E.g.

    -[resizePicture addDependency:downloadPicture];
     [operationQueue addOperation:downloadPicture];
     [operationQueue addOperation:resizePicture];

Grand Central Dispatch
----------------------

### Overview

A C-based mechanism for executing custom tasks. All you have to do is define
the tasks you want to execute and add them to an appropriate dispatch queue.
GCD takes care of creating the needed threads and of scheduling your tasks to
run on those threads. A **serial dispatch queue runs only one task at a time**,
waiting until that task is complete before dequeuing and starting a new one. By
contrast, a **concurrent dispatch queue starts as many tasks as it can without
waiting** for already started tasks to finish. The asynchronous dispatching of
tasks to a dispatch queue cannot deadlock the queue.

### Singleton using dispatch_once

So you have a class, and you want to access it throughout your app as a
Singleton object that is always available to be sent a message.  Let the name
of the object's class be `SingletonObject`, and let its getter be named
`singletonGetter`.  Add the method in the following manner:

    + (instancetype)singletonGetter
    {
        static dispatch_once_t once;
        static id singletonGetter;
        dispatch_once(&once, ^{
            singletonGetter = [[self alloc] init];
        });
        return singletonGetter;
    }

So now we could access the Singleton like so:

    SingletonObject *myGloballyAccessibleInstance = [SingletonObject singletonGetter];


Automatic Reference Counting
============================

@autoreleasepool blocks
-----------------------

[From the Advanced Memory Management Docs](https://developer.apple.com/library/mac/documentation/Cocoa/Conceptual/MemoryMgmt/Articles/mmAutoreleasePools.html#//apple_ref/doc/uid/20000047-CJBFBEDI)

At the end of the `@autoreleasepool` block, objects created within the block
are sent a `release` message for each time it was sent an `autorelease` message
within the block.

You can decleare one `@autoreleasepool` block within another. E.g. there is one
wrapping the whole program in `main.m`, and you may have another one within one
of your source files.

I believe that Apple's ARC system sends an `autorelease` message to every
object that you send an `alloc] init]` message to.

Since AppKit and UIKit frameworks process each event-loop iteration within an
`@autoreleasepool` block, you typically don't have to create these yourself.

However, there are 3 occasions when you may want to manually create your own
`@autoreleasepool` block:

1. In a command-line tool or other program not based on a UI framework
2. **In a loop that creates many temporary objects** to reduce peak memory footprint
3. In a thread you spawn inside a Foundation-only program
   or in a "detached" thread that makes Cocoa calls

Here's an example of using it inside a giant `for`-loop

    NSArray *urls = <# An array of file URLs #>;
    for (NSURL *url in urls) {
        @autoreleasepool {
            NSError *error;
            NSString *fileContents = [NSString stringWithContentsOfURL:url
                                            encoding:NSUTF8StringEncoding error:&error];
            /* Process the string, creating and autoreleasing more objects. */
        }
    }

__bridge cast
---------------

[ARC Docs#ref](http://clang.llvm.org/docs/AutomaticReferenceCounting.html#bridged-casts)

**"These casts are required in order to transfer objects in and out of ARC control."**

`(__bridge Type) operand` casts `operand` to `Type`,
where `operand.isRetainable != Type.isRetainable`

> In general, a program which attempts to implicitly or explicitly convert a
> value of retainable object pointer type to any non-retainable type, or
> vice-versa, is ill-formed. For example, an Objective-C object pointer shall
> not be converted to `void*`. As an exception, cast to `intptr_t` is allowed
> because such casts are not transferring ownership. The bridged casts may be
> used to perform these conversions where necessary.

> *Rationale*

> We cannot ensure the correct management of the lifetime of objects if they
> may be freely passed around as unmanaged types. The bridged casts are
> provided so that the programmer may explicitly describe whether the cast
> transfers control into or out of ARC.

Encoding dict into JSON
-----------------------

[From SOQ](http://stackoverflow.com/questions/11207483/how-to-correctly-convert-an-nsdictionary-to-a-json-format-in-ios)

    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:myNSDictionary options:NSJSONWritingPrettyPrinted error:&error];
    NSString *str = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];


[Collection Operators](https://developer.apple.com/library/ios/documentation/cocoa/conceptual/KeyValueCoding/Articles/CollectionOperators.html)
----------------------

* Perform action on items in collection using key-path notation and an action operator

##### Normal
avg, count, max, min, sum

##### Unions
distinctUnionOfObjects, unionOfObjects (a 'bag')

##### Nested Collections
distinctUnionOfArrays, unionOfArrays, distinctUnionOfSets (takes Set, makes Set)



#### Format

    keypathToCollection.@collectionOperator.keypathToProperty

#### Examples

You have an NSArray of Transactions, and each has an `amount`, so you do

    NSNumber *avg = transactions.@avg.amount

You want the date of the latest transaction

    NSNumber *latestDate = transactions.@max.date

You have an NSArray of Customers, and each has a `lastname`, so you do

    NSArray *lastnames = customers.@distinctUnionOfObjects.lastname

You have an NSArray of NSArrays of Customers, and each has a `lastname`, so you do

    NSArray *lastnames = myArrayOfArraysOfCustomers.@distinctUnionOfArrays.lastname

[Collections](https://developer.apple.com/library/ios/documentation/Cocoa/Conceptual/Collections/Collections.html#//apple_ref/doc/uid/10000034i)
-----------

Primarily

* `NSArray`
* `NSSet`
* `NSDictionary`

Also

* `NSPointerArray`
    * mutable
    * can hold `NULL` pointers
    * pointers in array, and @count can be set manually
* `NSHashTable`
* `NSMapTable`
* `NSCountedSet` --- same as Python's `collections.Counter()`


### [NSSets](http://stackoverflow.com/questions/3826789/getting-an-object-from-an-nsset)

There are several use cases for a set. You could enumerate through (e.g. with
`enumerateObjectsUsingBlock` or `NSFastEnumeration`), call `containsObject` to test
for membership, use `anyObject` to get a member (not random), or convert it to an
array (in no particular order) with `allObjects`.

A set is appropriate when you don't want duplicates, don't care about order,
and want fast membership testing.

Properties
==========

@dynamic
------------------
These are where you *would* see `@synthesize`, inside the `@implementation`.

#### From Stanford Lecture 12, slide 52.

It says “I do not implement the setter or getter for this property, but send me
the message anyway and I’ll use the Objective-C runtime to figure out what to
do.”

There is a mechanism in the Objective-C runtime to “trap” a message sent to you
that you don’t implement.

`NSManagedObject` does this and calls
`valueForKey:` or `setValue:forKey:`. Pretty cool.

The declarations are really just there to suppress compiler warnings.

[Strong vs. Weak Retain vs. Copy vs. Assign](http://stackoverflow.com/questions/9859719/objective-c-declared-property-attributes-nonatomic-copy-strong-weak)
----------------------------

* **Strong** --- for pointers to objects, adds one to reference count of the object
* **Weak** --- like *strong*, but doesn't add to reference count
* **Assign** --- [I think *was*] default, to be used for non-pointer attributes
* **Retain** --- old word for strong, before ARC
* **Copy** --- cause the setter for that property to create a copy of the object
    * when object is mutable, use this to make it not show changes made
      by object's other owners

[This SOQ](http://stackoverflow.com/questions/4995254/nsmutablestring-as-retain-copy/5002646)
looks like a good place to find out more about when/why/how to use `(copy)`

[Atomic vs nonatomic properties](http://stackoverflow.com/questions/588866/atomic-vs-nonatomic-properties)
--------------------------------

* **atomic** --- setter/getter ensure that a whole value is always set/gotten,
                 regardless of setter activity on any other thread.
    * If `thread A` is inside the getter when `thread B` calls the setter, a
      viable value will still be returned to `thread A`.

* **nonatomic** --- no such guarantees are made.
    * Thus, *nonatomic is considerably faster than atomic*.

**Atomic does not do is make any guarantees about thread safety**. If thread
A is calling the getter simultaneously with thread B and C calling the setter
with different values, thread A may get any one of the three values returned.
Ensuring data integrity is achieved by other means. Atomicity does not generally
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

##### For Concurrency
Instead of defining blocks in their own lexical scope, you typically define
blocks inside another function or method so that they can access other
variables from that function or method. Blocks can also be moved out of their
original scope and copied onto the heap, which is what happens when you submit
them to a dispatch queue. All of these semantics make it possible to implement
very dynamic tasks with relatively little code.

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

However, categories *cannot* have instance variables (doesn't count `@props`).

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
