latex input:        mmd-article-header
Title:              JavaFX Notes
Author:         Ethan C. Petuchowski
Base Header Level:      1
latex mode:     memoir
Keywords:           Java, programming language, syntax, GUI, desktop, application development
CSS:                http://fletcherpenney.net/css/document.css
xhtml header:       <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
copyright:      2014 Ethan Petuchowski
latex input:        mmd-natbib-plain
latex input:        mmd-article-begin-doc
latex footer:       mmd-memoir-footer

## Start and Run a JavaFX GUI Project in IntelliJ

New Project \\(\rightarrow\\) JavaFX \\(\rightarrow\\) Finish
\\(\rightarrow\\) `src/sample/Main` \\(\rightarrow\\) Run

## Intro to JavaFX

From [Oracle's Getting Started Tutorial][ogst]

[ogst]: http://docs.oracle.com/javase/8/javafx/get-started-tutorial/jfx-overview.htm

### What is JavaFX?

1. Look & feel customizable with CSS
2. FXML scripting language optional for UI presentation development
3. JavaFX Scene Builder for designing UIs without writing code, generates editable FXML
4. Cross-platform compatibility, consistent maintenance ensured by Oracle
5. API designed to be friendly with Scala
6. It is possible to integrate with JavaScript and HTML5 via WebView
7. You can embed Swing content into JavaFX applications
8. I believe the whole system was **written in vanilla Java!** (that's the coolest part yet)
9. Only a subset of the whole framework is actually Public API

### Libraries & APIs

3D Graphics library, Camera API, Canvas API for drawing directly into a scene,
Printing API Rich Text support, Multitouch support, Hi-DPI (nice displays)
support, hardware-accelerated graphics, high-performance media engine supports
web multimedia, self-contained application deployment model, animation.

### Structure

1. The scene graph is a hierarchical tree of nodes that represents all of the visual elements of the application's user interface
2. Each node of a scene graph besides the root has a single parent and 0+ children
3. Each **Scene node** can have --- Effects, blurs, shadows, opacity,
   transforms, event handles (e.g. mouse & keyboard), application-specific
   *state*
4. **Nodes** may be --- (2D & 3D), images, media, **embedded web browser**, text,
   UI controls, charts, groups, containers
5. etc.

## Beginner Classes

1. The "main class" (i.e. the one that you "run") `extends javafx.application.Application`
2. In this main class, you must override the `start` method

        @Override public void start(Stage primaryStage) throws Exception {/*my app*/}
    1. If you use the JavaFX Launcher, you don't need a `main()` method (otw you *do*)
3. A `Stage` is the GUI window
4. A `Scene` is a box that holds content inside a window
5. A `StackPane` is a resizable layout node whose size tracks the size of its containing window
6. A `Button` is a button

## Some useful classes

1. `FXCollections.observable[Collection]([collectionInstance])` --- turns your
   existing `Collection` into an *"observable"* one on which you can install
   `Listener`s
2. `FXCollections.<String>observableArrayList()` --- observable collection
   constructor that doesn't require an existing instance
3. `interface Property` --- `getProp`, `setProp`, `addListener`,
   `removeListener`, `bind` (see § below)
    1. `class SimpleStringProperty` --- normal string methods, listeners, `fireValueChangedEvent`,

## Properties and Binding

From [Oracle's Binding Tutorial][obt]

[obt]: http://docs.oracle.com/javafx/2/binding/jfxpub-binding.htm

1. JavaFX represents object properties using the "JavaBeans component
   architecture", which is both an API and a design pattern
2. Binding allows you to express direct relationships between variables --
   changes in one object are automatically reflected in another
3. A binding "observers" its list of "dependencies" for changes, and updates
   itself accordingly
4. A property has simple `getProp()`/`setProp(obj)` modifier naming
   conventions
5. `Observable` (does *not* wrap a value) and `ObservableValue` (wraps a
   value) interfaces *fire* change notifications
6. *Receive* change notifications in `InvalidationListener` (from
   `Observable`) and `ChangeListener` (from `ObservableValue`) interfaces
7. JavaFX bindings and properties all support lazy evaluation -- they're only
   computed when the value is *requested*---*unless* you install a
   `ChangeListener`, which forces eager computation so that the
   `ChangeListener` knows if a change has actually taken place
8. When any of a binding's dependencies are changed, it is marked `invalid`,
   but it is not actually recalculated until someone calls `getValue()`
    * You can install a listener on `invalidated()`
        * Further changes to an invalid property will *not* trigger the listener again

## Refs

1. [Oracle's Hello World][ohw]

[ohw]: http://docs.oracle.com/javafx/2/get_started/hello_world.htm
