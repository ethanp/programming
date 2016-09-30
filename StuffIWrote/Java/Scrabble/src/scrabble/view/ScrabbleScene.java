package scrabble.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import scrabble.model.BoardModel;
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
        BoardModel model = scrabbleGame.getBoardModel();
        double width = getWidth() - 50;
        double height = getHeight() - 100;
        ScrabbleBoardView scrabbleBoardView = new ScrabbleBoardView(model, width, height);
        midPane.getChildren().add(scrabbleBoardView);
    }
}
