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
10. To hook a `Button` in the UI up to a *handler* in the `Controller`
    1. Create the handler method

            @FXML private void handleDeletePerson() {
                int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
                personTable.getItems().remove(selectedIndex);
            }
    2. Select the UI elementin the SceneBuilder, and on the right pane go to
       \\(Code \rightarrow Main \rightarrow On Action\\) and select the
       `handleDeletePerson` method from the dropdown selector

## Tables

### In General

1. `class Cell<T>`, `T` is the type of the `itemProperty` contained within
   the cell
    1. A `Labeled` `Control` used for rendering
        1. a single "row" inside a `ListView`, `TreeView`, or `TableView`
        2. *also* used for each individual "cell" inside a `TableView`
    2. Has an `ObjectProperty<T> itemProperty` containing the data value
       associated with this cell (you should not set this property
       directly)
    3. Responsible for rendering the `itemProperty` (above), and sometimes
       editing it
    4. Could be text, or another control such as a `CheckBox`, or any
       other UI scene `Node`, like `HBox`
    5. Extremely large data sets are actually represented using a few
       recycled `Cells`
2. `class Control`
    1. base class for UI controls
    2. A `Node` in the scene-graph which can be manipulated by the user
3. `class Labeled` --- a sublcass of `Control` (above) that has textual
   content associated with it (e.g. a `Button`, `Label`, or `Tooltip`)


#### Cell Factories

from `javafx.scene.control.Cell` docs

1. `Cell` items are rendered by the container's skins, e.g. by default a
   `ListView` will convert it to a `String` and basically render that as text
   within a `Label`
2. To set how to render your type within your `Cell` within your *thingy*
   (`ListView`, `TableColumn`, `TreeView`, `TableView`, or `ListView`), you
   must provide an implementation of the `cellFactory` callback function
   defined on the *thingy*

        ObjectProperty<Callback<Thingy<T>,ThingyCell<T>> cellFactoryProperty
3. A `CellFactory` gets called when creating a new `Cell`
4. Cell factories create the `Cell` *and* configure it to react properly to
   changes in its state

##### There Example's Pretty Good

The main thing is the last line, though I have replaced their mess with a
lambda. The `updateItem` method is called whenever the item in the cell
changes (e.g. goes off-screen and therefore gets re-filled with a new data
element), so there is no need to explicitly manage bindings, though you
*could* if you want to.

To format a `java.lang.Number` as a *currency*, do

    public class MoneyFormatCell extends ListCell<Number> {
        public MoneyFormatCell() {}
        @Override protected void updateItem(Number item, boolean empty) {
            super.updateItem(item, empty); // REQUIRED
            setText(item == null ? "" : Util.formatMy(item));
            if (item != null)
                setTextFill(isSellected() ? Color.WHITE :
                    item.doubleValue() >= 0 ? Color.BLACK : Color.RED);
        }
    }

    ListView<Number> listView = new ListView<>(observableMoneyList);
    listView.setCellFactory(list -> return new MoneyFormatCell());

Or you could define it inline with an anomymously overridden ListCell<Number>
factory-like piece of code like so

    ListView<Number> listView = new ListView<>(observableMoneyList);
    listView.setCellFactory(new Callback<ListView<<Number>, ListCell<Number>>() {
        @Override public ListCell<Number> call(final ListView<Number> p) {
            return new ListCell<Number>() {
                @Override protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    // my update code goes here (e.g. setText("random.nextInt()"); )
                }
            };
        }
    });

### TreeTableView

1. A `Control` conceptually similar to `TreeView` and `TableView`
2. Same `TreeItem` API as `TreeView`
3. Same `TableColumn`-based approach as `TreeTableView`, using
   `TreeTableColumn`
4. The user can sort by multiple columns by holding the Shift key while
   clicking on column headers
5. The `TreeTableView` automatically *observes* its root `TreeItem` instance
6. You can customize `class TreeTableRow<T>`, but "more often than not" [docs]
   it is easier to customize individual cells in a row rather than the row
   itself.
7. `class TreeItem<T>` --- *not* a Node; supplies a hierarchy of values to a
   `TreeView` or `TreeTableView`
    1. You can register listeners when there is a change in the number of
       items, their position, or value
    2. You can specify `getChildren` like they do in the docs, I think this is
       what I'll have to do

#### cellValueFactoryProperty

    TreeTableColumn.cellValueFactoryProperty [of type:]
        public final ObjectProperty<
            Callback<
                TreeTableColumn.CellDataFeatures<S,T>,
                OvservableValue<T>
            >
        >

##### What does that type mean?

1. `class ObjectProperty<T>` --- full implementation of a `Property<T>` (see
   above) wrapping an arbitrary object of static type `T`
2. `interface Callback<P,R>`
    1. One method: `R call(P param)`
    2. Implemented by e.g. `TreeItemPropertyValueFactory`
    3. A reusable interface for defining APIs that require a call back
3. `class TreeTableColumn.CellDataFeatures<S,T>`
    1. `S` --- `TableView` type
    2. `T` --- `TreeTableColumn` type
    3. Immutable wrapper class type to provide all necessary information
       for a particular `Cell`
4. `class TreeItemPropertyValueFactory<S,T>` is a convenience implementation
   of the `Callback` interface designed for use within the
   `TreeTableColumn.cellValueFactoryProperty`. I believe it means instead of

        firstNameCol.setCellValueFactory(
                p -> p.getValue().getValue().firstNameProperty());
    you would do

        firstNameCol.setCellValueFactory(
                new TreeItemPropertyValueFactory<Person,String>("firstName"));

    It's obviously not so useful anymore, now that there are lambdas.

## Other features

1. Multimedia support via `javafx.scene.media` APIs for video (FLV) and audio
   (MP3, AIFF, WAV)
2. Web Browser
    1. Via "Web Component", based on Webkit, provides full browser via API,
       supporting HTML5, CSS, JavaScript, DOM, and SVG, Back/Forward
       navigation, etc.
    2. Use the `WebEngine` and a `WebView`

## Refs

1. [Oracle's Hello World][ohw]

[ohw]: http://docs.oracle.com/javafx/2/get_started/hello_world.htm
