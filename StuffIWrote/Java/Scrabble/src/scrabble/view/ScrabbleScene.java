package scrabble.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import scrabble.model.BoardModel;
import scrabble.model.ScrabbleGame;

/**
 * 9/30/16 2:12 AM
 */
public class ScrabbleScene extends Scene {

    /** this is the "model" (like MVC) for the game */
    private final ScrabbleGame scrabbleGame;
    private final Pane midPane;

    /** initialize the board's visual and backend aspects */
    public ScrabbleScene(Parent root, int width, int height, ViewController viewController) {
        super(root, width, height);
        this.scrabbleGame = new ScrabbleGame();
        this.midPane = viewController.midPane;
        paintTheBoard();
        paintCurrentRack();
    }

    private void paintTheBoard() {
        BoardModel model = scrabbleGame.getBoardModel();
        double width = getWidth() - 150;
        double height = getHeight() - 225;
        midPane.getChildren().add(new ScrabbleBoardView(model, width, height));
    }

    private void paintCurrentRack() {
        LetterRackView rackView = new LetterRackView(scrabbleGame);
        ((BorderPane) getRoot()).setBottom(rackView);
    }

    ScrabbleGame getScrabbleGame() {
        return scrabbleGame;
    }
}
