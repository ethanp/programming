package scrabble.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import scrabble.model.ScrabbleGame;

public class ViewController {

    @FXML public Pane midPane;
    private ScrabbleScene scrabbleScene;

    public void setScrabbleScene(ScrabbleScene scrabbleScene) {
        this.scrabbleScene = scrabbleScene;
    }

    @FXML void makeMovePressed(ActionEvent event) {
        System.out.println("make move pressed " + event);
        ScrabbleGame game = scrabbleScene.getScrabbleGame();
        game.confirmPendingTiles();
        game.getCurrentPlayer().refillRackFromBag();
        game.nextPlayersTurn();
    }

    @FXML void resetLettersPressed(ActionEvent event) {
        System.out.println("reset letters pressed " + event);
        scrabbleScene.getScrabbleGame().resetPendingTiles();
    }
}
