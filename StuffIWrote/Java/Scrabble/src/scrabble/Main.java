package scrabble;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scrabble.model.ScrabbleGame;

public class Main extends Application {

    static Scene scrabbleScene;
    static ScrabbleGame scrabbleGame = new ScrabbleGame();

    public static void main(String[] args) {
        launch(args);
    }

    @Override public void start(Stage mainWindow) throws Exception {
        Parent rootDomNode = FXMLLoader.load(getClass().getResource("view/baseLayout.fxml"));
        scrabbleScene = new ScrabbleScene(rootDomNode, 600, 600, scrabbleGame);
        mainWindow.setTitle("Make Words");
        mainWindow.setScene(scrabbleScene);
        mainWindow.show();
    }


    private class ScrabbleScene extends Scene {
        private final ScrabbleGame scrabbleGame;

        ScrabbleScene(Parent root, int width, int height, ScrabbleGame scrabbleGame) {
            super(root, width, height);
            this.scrabbleGame = scrabbleGame;
        }
    }
}
