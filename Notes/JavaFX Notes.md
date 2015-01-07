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
3. JavaFX Scene Builder for designing UIs without writing code, generates
   editable FXML
4. Cross-platform compatibility, consistent maintenance ensured by Oracle
5. API designed to be friendly with Scala
6. It is possible to integrate with JavaScript and HTML5 via WebView
7. You can embed Swing content into JavaFX applications
8. I believe the whole system was **written in vanilla Java!** (that's the
   coolest part yet)
9. Only a subset of the whole framework is actually Public API
10. It allows drag-and-drop and everything else you wish was easier in
    JavaScript

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
5. "Pulses" are events, throttled to 60 frames per second, telling the scene
   graph to synchronize with the "Prism" renderer. They are scheduled whenever
   the scene graph changes.


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

## Concurrency

[From Oracle's Concurrency Docs][ocd]

[ocd]: http://docs.oracle.com/javase/8/javafx/interoperability-tutorial/concurrency.htm

1. Look at `javafx.concurrent` as opposed to the standard Java `Runnable`
2. Keep the interface responsive by backgrounding time-consuming tasks
3. The JavaFX scene graph is *not thread-safe* and hence can only be accessed
   & modified from the UI thread called "JavaFX Application thread"
    * You must keep this thread un-clogged from long tasks
4. `interface Worker` --- provides APIs for background worker to communicate
   with the UI
    1. `class Task` --- fully *observable*, for doing work on a background thread
    2. `class Service` --- *executes* `Task`s
5. A `WorkerStateEvent` fires when a `Worker` changes state, both `Task` and
   `Service` can listen for these.
    1. See the **worker states** listed in the **table** (likely somewhere below here)
6. `Worker` progress can be obtained via `totalWork`, `workDone`, and `progress`
7. Here's what it looks like

        Task<Integer> task = new Task<Integer>() {
            @Override protected Integer call() throws Exception {
                int iterations;
                // ...
                return iterations;
            }
        };
8. Don't try to modify the an active scene graph from `call()`---you will
   throw a `RuntimeException`
9. You can call `updateProgress`, `updateMessage` (this is a *property* on
   `Worker` potentially useful to the UI thread, and `updateTitle` (similar to
   `message` property) as appropriate
10. Start it via `task.start()` or `ExecutorService.submit(task)`
11. You're supposed to check `isCancelled()` every now and then and stop
    processing if `true`
12. To have a progressBar you'd call `updateProgress(...)` *inside* the
    `Task`, and then roughly the following

        new ProgressBar().progressProperty().bind(task.progressProperty());
13. `Service` facilitates interaction between background threads and the UI thread
14. So what you do is the following

        class MyService extends Service<String> {
            @Override protected Task<String> createTask() {
                return new Task<String>() {
                    @Override protected String call() {
                        return "Hello, World!";
                    }

                    // optional...
                    @Override protected void succeeded() {...}
                    @Override protected void cancelled() {...}
                    @Override protected void failed() {...}
                }
            }
        }
15. Now you can start the service with an `Executor` or a daemon thread, and
    restart it automatically by using a `ScheduledService` which allows a
    `backoffStrategy` and a `maximumFailureCount`

| **Worker state**  | **When** |
| ---------------:  | :------- |
| `READY`           | when just starting out                |
| `SCHEDULED`       | after being scheduled for execution   |
| `RUNNING`         | while executing                       |
| `SUCCEEDED`       | successfully completed; `value` set to *result* |
| `FAILED`          | threw exception; `exception` set to exception type |
| `CANCELLED`       | if it gets inturrupted via `cancel()` |

## Scene Builder 2.0

[From an incredible tutorial](http://code.makery.ch/java/javafx-8-tutorial-part2/)

### To hook code into the view created in scene builder

1. Say in package `view` you use Scene Builder to create a view called
   `PersonOverview.fxml`
2. Now *in that same package*, create the file `PersonOverviewController.java`
3. All private fields & methods to be accessed in the fxml file must have the
   `@FXML` annotation
4. The `@FXML private void initialize(){...}` method will be automatically
   called after the fxml file has been loaded, at which point the FXML fields
   should have been already initialized
5. In the tutorial, the controller has a field

        @FXML private TableColumn<Person, String> lastNameColumn;
    which requires that we provide a way to map given `Person` instances to `String`.
    We provide this in the `initialize()` method mentioned above via a *Lambda*

        lastNameColumn.setCellValueFactory(
            cellData -> cellData.getValue().lastNameProperty());
6. We load the controller into the main Application class via the `Loader`
   which points to the `view` package

        PersonOverviewController controller = loader.getController();
        controller.setMainApp(this);
7. We hook the view to the controller by selecting the proper Java class from
   the dropdown in SceneBuilder under \\(Document \rightarrow Controller
   \rightarrow Controller class\\) in the left pane
9. We hook each of the UI elements into their corresponding Controller fields
   by selecting the element in the SceneBuilder element Hierarchy, going to
   \\(Code \rightarrow Identity \rightarrow fx:id\\) in the right pane and
   selecting the Controller's field name

## Other features

1. Multimedia support via `javafx.scene.media` APIs for video (FLV) and audio (MP3, AIFF, WAV)
2. Web Browser
    1. Via "Web Component", based on Webkit, provides full browser via API,
       supporting HTML5, CSS, JavaScript, DOM, and SVG, Back/Forward
       navigation, etc.
    2. Use the `WebEngine` and a `WebView`

## Refs

1. [Oracle's Hello World][ohw]

[ohw]: http://docs.oracle.com/javafx/2/get_started/hello_world.htm
