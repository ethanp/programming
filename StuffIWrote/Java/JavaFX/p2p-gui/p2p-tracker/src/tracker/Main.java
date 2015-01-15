package tracker;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Ethan Petuchowski 1/14/15
 */
public class Main extends Application {
    public Main() { /* I could put stuff in here, but at this point there is no need */ }
    public static void main(String[] args) { launch(args); }
    public static Stage getPrimaryStage() { return primaryStage; }
    private static Stage primaryStage;
    private BorderPane rootLayout;

    @Override public void start(Stage primaryStage) throws Exception {

    }
}
