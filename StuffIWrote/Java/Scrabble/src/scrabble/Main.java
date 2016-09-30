package scrabble;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import scrabble.view.ScrabbleScene;
import scrabble.view.ViewController;

public class Main extends Application {

    static ScrabbleScene scrabbleScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override public void start(Stage mainWindow) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/baseLayout.fxml"));
        BorderPane rootDomNode = loader.load();
        ViewController viewController = loader.getController();
        scrabbleScene = new ScrabbleScene(rootDomNode, 900, 900, viewController);
        viewController.setScrabbleScene(scrabbleScene);
        mainWindow.setTitle("Make Words");
        mainWindow.setScene(scrabbleScene);
        mainWindow.show();
    }


}
