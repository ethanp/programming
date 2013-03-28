README file for Lisp Interpreter
Group: Simon Doty, Ethan Petuchowski, Justin Hust

Instructions:

Unzip zipped folder.

Run 'makefile' at command line. This is important since we use the 
makefile to preserve certain node classes and our visitor classes. 
If the project is recompiled without the makefile, then jjtree will overwrite
our custom classes with its default.

Then type 'java LispParser' at the command line to run program.

At the prompt, to test the program using our built in test suite (tests are
built into the LispParser.jjt file) type 'test' and press enter.

This should run our suite of 20 tests. 

If you want to test individual statements, just enter then at the prompt
after running java LispParser.


Static / Dynamic Scoping:

To toggle between static and dynamic scoping, type 'set scoping dynamic' 
or 'set scoping static'. The program should output what mode it is in after
setting it. 

