package scrabble;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import scrabble.view.ScrabbleScene;
import scrabble.view.ViewController;

public class Main extends Application {

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
        ScrabbleScene scrabbleScene = new ScrabbleScene(rootDomNode, 900, 900, viewController);
        viewController.setScrabbleScene(scrabbleScene);
        mainWindow.setScene(scrabbleScene);
        mainWindow.setTitle("Make Words");
        mainWindow.show();
    }
}
