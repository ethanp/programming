package graph.visuals.app;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Ethan Petuchowski 5/30/16
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 500, 500, Color.BLACK);
        Interactive interactive = new Interactive(scene);
        stage.setTitle("Interactive Graph Application");
        stage.setScene(scene);
        stage.show();
    }
}

