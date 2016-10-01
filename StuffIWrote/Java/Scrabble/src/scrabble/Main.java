package scrabble;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import scrabble.view.ScrabbleScene;
import scrabble.view.ViewController;

public class Main extends Application {

    public static ScrabbleScene scrabbleScene;

    /** Application entry point */
    public static void main(String[] args) {
        // leads to start() [below] being called
        launch(args);
    }

    /** Create the scene (which maintains the game model), and show it */
    @Override public void start(Stage mainWindow) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/baseLayout.fxml"));
        BorderPane rootDomNode = loader.load();
        ViewController viewController = loader.getController();
        scrabbleScene = new ScrabbleScene(rootDomNode, 900, 900, viewController);
        viewController.setScrabbleScene(scrabbleScene);
        mainWindow.setScene(scrabbleScene);
        // remove resizability so we can exert more control over the user
        mainWindow.setResizable(false);
        mainWindow.setTitle("Make Words");
        mainWindow.show();
    }
}
