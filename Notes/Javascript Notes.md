this keyword
------------
#### 4/14/14
[Nice Tutorial](http://javascriptissexy.com/understand-javascripts-this-with-clarity-and-master-it/)

**`this` is the object that invokes the function where `this` is used.**

(When the code is executing in the browser...) **All global variables and functions are defined on the `window` object**. Therefore, `this` in a global function refers to the global `window` object (not in `strict` mode though).

bind(), call(), apply()
-----------------------

#### 4/14/14

* [Nice Tutorial](http://javascriptissexy.com/javascript-apply-call-and-bind-methods-are-essential-for-javascript-professionals/)
* [MDN](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Function/bind)

Recall that **functions** are **objects**, and **objects** have **prototypes**, and **prototypes** provide **methods**. So the methods `bind()`, `call()`, and `apply()` belong to `Function.prototype`.

**Allows you to easily set which specific object will be bound to *`this`* when a function or method is invoked.** 

The problem is that when we use the `this` keyword in methods, and we
call said methods from a receiver object, sometimes this is not bound
to the object that we intended. Said again, when we execute an object's
method from within another object, the `this` keyword now refers to calling
object, *not* the object where `this` was originally defined. This will
cause errors, and requires us to use one of the methods in this § to
specifically set the value of `this`.

### .apply()

    instance.method.apply(object2, array)
    
We call the `method()` method of `instance`, such that `this` (inside
`method()`) refers to `object2`. We are passing `array` to `method()`.
So if `method()` assigns any new attributes to `this`, they will
become attributes of `object2`.

event.which
-----------

**Indicates which key was pressed, via its *unicode* value.**

Doesn't seem to do anything for non-alphanumeric-character keys.

#### 4/13/14

### To check what a character key's value is

**How about entering the following in to the interpreter, then going back to
the browser and hitting the key you want the value of?**

    document.onkeypress = function(myEvent) { // note: event needn't be called "e"
        console.log(myEvent.which);
    };

Navigator
---------

**contains information about the visitor's browser.**

#### 3/25/14

[W3 Schools](http://www.w3schools.com/js/js_window_navigator.asp)

Technically, it's the `window.navigator` object, but the `window.`
prefix is not required.

E.g. (from `http://webaudiodemos.appspot.com/AudioRecorder/index.html`,
`./Audio Recorder_files/main.js`, function initAudio, [currently]
lines 162-173)

    if (!navigator.getUserMedia)
        navigator.getUserMedia = navigator.webkitGetUserMedia 
                                || navigator.mozGetUserMedia;

Using their example, you can get all sorts of other possibly-useful stuff as well:

    Browser Code-Name:     navigator.appCodeName
    Browser Name:          navigator.appName
    Browser Version:       navigator.appVersion
    Cookies Enabled:       navigator.cookieEnabled
    Platform:              navigator.platform
    User-agent header:     navigator.userAgent
    User-agent language:   navigator.systemLanguage

