latex input:        mmd-article-header
Title:              Testing Notes
Author:             Ethan C. Petuchowski
Base Header Level:  1
latex mode:         memoir
Keywords:           programming, testing, TDD, BDD, unit tests
CSS:                http://fletcherpenney.net/css/document.css
xhtml header:       <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
Copyright:          2015 Ethan Petuchowski
latex input:        mmd-natbib-plain
latex input:        mmd-article-begin-doc
latex footer:       mmd-memoir-footer

## Meta-Note
* It appears that testing is highly philosophical and controversial
* Seems to me though most agree it is ***really useful***
* So even if I'm not a *testing guru*, I can use it for my benefit

## Sample opinions on unit testing

* "Usually, for every pound of production code, I would like to see about two
  pounds of unit tests and two ounces of functional tests (a little bit goes a
  long ways). The problem I see in too many shops is zero unit tests and a
  pound of functional tests." ([Javaranch][eut])

## Bibliography

* [xUnit Wikipedia][xw]
* [Javaranch - Evil unit testing][eut]
* [Phawk Blog: Testing Sinatras APIs][tsa]
* *Effective Unit Testing: A guide for Java developers*, Lasse Koskela,
  Manning, 2013.

[xw]: http://en.wikipedia.org/wiki/XUnit
[eut]: http://www.javaranch.com/unit-testing.jsp
[tsa]: http://phawk.co.uk/blog/testing-sinatra-apis/

## Vocab

### Types of Tests in decreasing amount of code tested order
1. **Acceptance test** --- end-to-end, test that the system as a whole *works*
    * These are a pain to write and maintain for constantly-changing user
      interfaces
    * But are great to have in place for RESTful web-services/APIs
    * "Allows for" complete refactoring of the internal architecture
2. **System test** --- all components run together, as would happen in a normal
   usage scenario
3. **Integration test** --- testing 2+ components working together
4. **Component test** --- running one component (defined by the application) by
   itself
5. **Functional test** --- bigger than **unit**, smaller than **component** test
    * Exercises several methods/functions/classes working together
    * Allowed to take much longer than unit tests (which should be blazing
      fast)
6. **Unit testing** --- testing fundamental *units* of software, checking
   outputs against expected outputs for given inputs
    * A *"Unit"* is commonly a single method
    * **Assert** that the method and the object it operates on behaves as-
      expected *in that case*
    * If your app is small enough, just get an **acceptance test** (below) to
      pass *first*, then worry about this if you feel you have to

### Other vocab
* **xUnit** --- the collective name of frameworks for unit testing that use a
  particular popular architecture described by the collection of vocab terms
  herein
* **Regression suite** --- collection of tests that can all be run at once;
  could be unit or functional tests
* **Regression tests** --- make sure that fixing a bug didn't break something
  that used to work
* **Stress test** --- go bonkers; try to test concurrent code.
* **Double / Stub / Fake / Mock** --- a fake version of an object that would
  have had to be used to test another object

## xUnit architecture

1. **Test runner** --- executable program that runs tests and reports their
   results
2. **Test case** --- elemental test case class
    * A set of conditions or variables set up to determine whether some code is
      doing what it is supposed to
    * Could be a requirement, a use case, a heuristic, etc.
3. **Text fixture** *(context)* --- preconditions (state) needed to run the
   test repeatable/consistently
    * Loading a database with a specific, known set of data
    * Creation of fake/mock objects
4. **Test suite** --- a set of tests that all share the same fixture. The order
   of the tests shouldn't matter.
5. **Test execution** --- each individual unit test is run in the following way

        setup(); // create text fixture
        execute_test_case()
        teardown(): // destroy fixture to avoid disturbing other tests
6. **Results formatter** --- can output *plain text* or *XML* to integrate with
   build tools like Jenkins
7. **Assertions** --- express a logical condition that must be true in a
   correct environment.

## The Case for Testing

1. People used to not worry about testing, and would just write the code until everything seemed to work
2. More and more people are doing some form of test-first development
3. More tests means less time debugging
4. Don't worry about if you're testing *everything*
5. One of the *key* benefits of testing is it forces you to think through what
   it looks like to *use* your code. In other words, writing tests is a good
   **design tool**
6. The longer your bug lives, the more it costs to fix, so testing helps you
   find it earlier before it becomes (uh-oh...) too late to fix
7. An advantage I keep reading about TDD is that you don't write code for
   something you'll never use; because if there's no test case for it, you
   don't write the code.
8. TDD forces you to make your code less complex so that it is more testable
9. Writing tests forcces you to think about what the *components* of your
   software are
10. Readable tests can be used to verify the *direction* of your code complies
    with the requirements

## Basics of Testing
1. Your tests should not have duplicated code, so you'll have to refactor the
   tests themselves as you write them. This makes the tests way more
   maintainable.
2. Tests *must* be **reliable and repeatable**
    * So don't use `Thread.sleep(1000)` to test asynchronous logic
3. **Tests must check for intended behavior *not* a specific implementation**
4. Don't rely on test order
5. Tautological tests are useless
6. Make the tests run when your project is built by your automated build tool

## Doubles / Stubs / Fakes / Mocks

1. They're a substitute for a class, used for testing
2. They can make running your test code faster, and more repeatable & reliable
