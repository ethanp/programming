Objective C Notes
=================

### @property

Setter and Getter* are synthesized automatically [*unless you use `(readonly)`]

**Dot notation** is "purely a convenient wrapper around accessor method calls."

* `a.b = @"c";` is exactly the same as `[a setB:@"c"];`


### Forward Declarations

From [StackOverflow](http://stackoverflow.com/questions/11532888/objective-c-protocol-forward-declarations)

**You cannot forward declare a superclass or a protocol that it conforms to.**

* In those cases, you must include the header. This is so that the moethods and
  properties of those items actually become a part of your class.

**You *can* forward declare a class that will be the type of one of your
variables.**

* This is because object pointers are all the same size and there is
  no difference between different types of object pointers at runtime.

### Variable Declarations in header files

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

