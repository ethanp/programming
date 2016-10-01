package scrabble.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class ViewController {

    @FXML public Pane midPane;
    private ScrabbleScene scrabbleScene;

    public void closeMenuItemClicked(ActionEvent event) {
        System.exit(0);
    }

    public void setScrabbleScene(ScrabbleScene scrabbleScene) {
        this.scrabbleScene = scrabbleScene;
    }
}
