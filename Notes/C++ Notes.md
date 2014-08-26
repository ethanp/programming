latex input:        mmd-article-header
Title:              C++ Notes
Author:             Ethan C. Petuchowski
Base Header Level:  1
latex mode:         memoir
Keywords:           syntax, language, C++, OOP, old school garbage
CSS:                http://fletcherpenney.net/css/document.css
xhtml header:       <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
Copyright:          2014 Ethan Petuchowski
latex input:        mmd-natbib-plain
latex input:        mmd-article-begin-doc
latex footer:       mmd-memoir-footer

Many notes are from *C++ the Core Language*, by Gregory Satir & Doug Brown.

## Not Specific to Classes

### Overloading

C++ allows more than one function with the same name as long as their signatures [set of parameters in the definition] differ. **This is unlike C**.

### I/O

#### `cout` for `printf`

    #include <iostream.h>
    cout << (<output>);
    cout << myInt;
    cout << myFloat;
    cout << "a string\n";
    cout << "sqrt: " << sqrt(myInt) << '\n';
    
### Default parameters

The following can be called with *or* without 3rd parameter '`t`'.

    int fctn(int p1, int p2, char t='n')

### `new` & `delete` for `malloc` & `free`

* Single float
    
        // C
        float *fp = malloc(sizeof(float));
        free(fp);
        
        // C++
        float *fp = new float;
        delete fp;

* Float array        
    
        // C
        float *fa = malloc(sizeof(float) * 10);
        free(fa);
        
        // C++
        float *fa = new float[10];
        delete[] fa;
        
* Class instance

        Bar *bp = new Bar;
        delete bp;

### References

Declaration

    // C
    void plus_5(int *ip) {      *ip = *ip + 5;    }
    
    // C++
    void plus_5(int &ip) { // &amp      ip = ip + 5;			  // lack of *astrxs    }
Usage
    // C    int n = 3;
    plus_5(&n);
    
    // C++
    int n = 3;    plus_5(n);  // no &amp

## Classes

### Basic Class Example

    class Stack {

    public:
      void push(int i);
      int pop();
      Stack();   // instead of init(Stack *s) in C
      ~Stack();  // instead of cleanup(Stack *s) in C
    
    private:
      int items[STACKSIZE];
      int top;

    };
    
### Constructors & Destroyers

* Functions like `ABC()` and `~ABC()` get the compiler to handle object initialization and cleanup for you.
* The **constructor** is called when an object of `class ABC` is *declared*
    * Can be overloaded just like any other function
    * Doesn't have a `return` type
* The **destructor** is called when the object *leaves scope*

#### Copy constructor

    ABC(ABC &copy_from_me);

Allows you to say (not sure this is correct)

    ABC a2 = new ABC;
    ABC a1 = a2;

### "Member functions" (i.e. methods)

They can be defined *inside* or *outside* the class definition

    class ABC {
    public:
    
      void definedInside(int i) {
        cout << i + 1;
      }
      
      void definedOutside(int i);
    }
    
    void ABC::definedOutside(int i) {
      cout << i + 2;
    }
    
    
### Modifiers

#### public & private

* **public** --- can be accessed by any code
* **private** --- can be accessed by the class’s own member functions (and *"friends"*) only
* One object ***can*** access the `private` members of another object from the same `class`, but **can*not*** access `private` members of the `class` it subclasses.

#### Member variables

##### `static`

Only one copy of the data is maintained for all objects of the class (just like Java)


### Inheritance ("derived classes")

#### Access levels

* You define it right before you say which class you are deriving from.
* The *default* is `private`

        class DEF : public ABC {
          ...
        };
    

### Overloading operators

* E.g. the `assignment operator`

        // in class decl
        void operator=(ABC &other); 
        
        // then we do        void ABC::operator=(ABC &other) { /* code */ }### Virtual Functions
* **Virtual functions** behave according to the *run-time* type of
  the object they are called on
    1. Declare a pointer to the *base* class `ABC`
    2. Point it to an instance of the *derived* class `DEF`
    3. Call the *virtual* function, it will call the version as defined
       on the *derived* class `DEF::virtualFunction()
* **Non-virtual functions** behave according to the *static* type
  of the object they are called on
* The **syntax** is like so

        virtual void virtualFunction();
        
    * The **syntax is *required*** in the *declaration* in the
      *base class* `ABC`
    * The **syntax is *optional*** in the *derived class* `DEF`