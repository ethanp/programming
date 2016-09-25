package graph.visuals.app2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Ethan Petuchowski 9/18/16
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    // The start() method is called when the system is ready for
    // the application to begin running.
    @Override public void start(Stage primaryStage) throws Exception {

        // A Group node contains an ObservableList of children that are
        // rendered in order whenever this node is rendered.
        // Group root = new Group();
        Pane root = new Pane();
        root.setMinSize(500, 500);
        // seems like we have to use CSS here, not the Color.BLACK constant
        root.setStyle("-fx-background-color: black;");

        // Scene is the container for all content in a scene graph.
        // Its size and fill are initialized during construction.
        Scene scene = new Scene(root, 500, 500, Color.BLACK);

        // Interactive is the prime mover of app-specific activity
        Interactive interactive = new Interactive(root);
        primaryStage.setTitle("Graph Algo Runner");

        // sceneProperty: The Scene to be rendered on this Stage.
        // There can only be one Scene on the Stage at a time,
        // and a Scene can only be on one Stage at a time.
        primaryStage.setScene(scene);

        // Sets visibility to `true`.
        primaryStage.show();
    }
}

