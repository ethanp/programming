package visuals.barGraph;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    /* This class is used to issue draw calls to a Canvas using a buffer.
     *
     * Each call pushes the necessary parameters onto the buffer where they will be later rendered
     * onto the image of the Canvas node by the rendering thread at the end of a pulse.
     *
     *  Once a Canvas node is attached to a scene, it must be modified on the JavaFX Application
     *  Thread.
     *
     *  Calling any method on the GraphicsContext is considered modifying its corresponding Canvas
     *  and is subject to the same threading rules.
     *
     *  A GraphicsContext also manages a stack of state objects that can be saved or restored at
     *  anytime.
     *
     * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/canvas/GraphicsContext.html
     */
    private static GraphicsContext graphicsContext;

    public static void main(String[] args) {
        // calls start() below I guess
        /* The launch method DOES NOT RETURN until the application has exited,
         * either via a call to Platform.exit or all of the application windows
         * have been closed. */
        launch(args);
    }

    public static void drawStuff() {
        graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillRect(10, 10, graphicsContext.getCanvas()
            .getHeight(), graphicsContext.getCanvas().getWidth());
    }

    private static void clearCanvas() {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, graphicsContext.getCanvas()
            .getHeight(), graphicsContext.getCanvas().getWidth());
    }

    /* Eventually (after some stack reflection mumbo-jumbo) called by the framework on
     * line 821 of LauncherImpl.java because this is an "instanceof Application"
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Non-negative bar graph");

        /* The StackPane layout pane places all of the nodes within a single stack with each new
         * node added on top of the previous node. This layout model provides an easy way to
         * overlay text on a shape or image or to overlap common shapes to create a complex shape.
         * Figure 1-6 shows a help icon that is created by stacking a question mark on top of a
         * rectangle with a gradient background.
         *
         * The alignment property can be set to manage how children are positioned in the stack
         * pane. This property affects all children, so margins can be set to adjust the position
         * of individual children in the stack.
         *
         * http://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
         */
        StackPane root = new StackPane();
        final int HEIGHT = 700;
        final int WIDTH = 700;

        /* The JavaFX scene graph, shown as part of the top layer in Figure 2-1, is the starting
         * point for constructing a JavaFX application. It is a hierarchical tree of nodes that
         * represents all of the visual elements of the application's user interface. It can handle
         * input and can be rendered.
         *
         * A single element in a scene graph is called a node. Each node has an ID, style class,
         * and bounding volume. With the exception of the ROOT node of a scene graph, each node in
         * a scene graph has a single parent and zero or more children.
         *
         * https://docs.oracle.com/javase/8/javafx/get-started-tutorial/jfx-architecture.htm#BABDFFDG
         */
        Scene s = new Scene(root, WIDTH, HEIGHT, Color.BLACK);

        /* Canvas is an image that can be drawn on using a set of graphics commands provided
         * by a GraphicsContext.
         */
        Canvas canvas = new Canvas(WIDTH, HEIGHT);

        // get the single graphics context associated with this Canvas
        graphicsContext = canvas.getGraphicsContext2D();
        clearCanvas();
        root.getChildren().addAll(canvas);
        ObservableList<Double> data = FXCollections.observableArrayList(
            4.0, 3.0, 5.0, 2.0
        );
        NonNegativeBarGraph lineGraph = new NonNegativeBarGraph(graphicsContext);
        lineGraph.drawBasedOn(data);
        primaryStage.setScene(s);
        primaryStage.show();
        primaryStage.show();
    }
}
