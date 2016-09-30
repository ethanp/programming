package scrabble;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import scrabble.model.ScrabbleGame;
import scrabble.view.ViewController;

/**
 * 9/30/16 2:12 AM
 */
public class ScrabbleScene extends Scene {
    private final ScrabbleGame scrabbleGame;
    private final ViewController viewController;
    private final Pane midPane;

    ScrabbleScene(Parent root, int width, int height, ViewController viewController) {
        super(root, width, height);
        this.viewController = viewController;
        this.scrabbleGame = new ScrabbleGame();
        this.midPane = viewController.midPane;
        paintTheBoard();
    }

    private void paintTheBoard() {
        GridPane boardGrid = new GridPane();
        boardGrid.add(new Rectangle(20, 20, Color.BLUE), 0, 0);
        boardGrid.add(new Rectangle(20, 20, Color.BLUE), 0, 2);
        boardGrid.add(new Rectangle(20, 20, Color.BLUE), 3, 1);
        midPane.getChildren().add(boardGrid);
    }
}
