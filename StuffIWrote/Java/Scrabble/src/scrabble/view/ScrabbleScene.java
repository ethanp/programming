package scrabble.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import scrabble.model.ScrabbleGame;

/**
 * 9/30/16 2:12 AM
 */
public class ScrabbleScene extends Scene {
    private final ScrabbleGame scrabbleGame;
    private final ViewController viewController;
    private final Pane midPane;

    public ScrabbleScene(Parent root, int width, int height, ViewController viewController) {
        super(root, width, height);
        this.viewController = viewController;
        this.scrabbleGame = new ScrabbleGame();
        this.midPane = viewController.midPane;
        paintTheBoard();
    }

    private void paintTheBoard() {
        ScrabbleBoard scrabbleBoard = new ScrabbleBoard(
              scrabbleGame.getBoard(), getWidth() - 50, getHeight() - 100);
        midPane.getChildren().add(scrabbleBoard);
    }

}
