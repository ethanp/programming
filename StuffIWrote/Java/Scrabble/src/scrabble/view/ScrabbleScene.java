package scrabble.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import scrabble.model.BoardModel;
import scrabble.model.LetterModel;
import scrabble.model.LetterRack;
import scrabble.model.ScrabbleGame;

import java.util.Iterator;

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
        paintAvailableLetters();
    }

    private void paintTheBoard() {
        BoardModel model = scrabbleGame.getBoardModel();
        double width = getWidth() - 150;
        double height = getHeight() - 200;
        ScrabbleBoardView scrabbleBoardView = new ScrabbleBoardView(model, width, height);
        midPane.getChildren().add(scrabbleBoardView);
    }

    private void paintAvailableLetters() {
        LetterRack currentRack = scrabbleGame.getCurrentPlayer().getLetterRack();
        LetterRackView rackView = new LetterRackView(currentRack);
        ((BorderPane) getRoot()).setBottom(rackView);
    }

    private static class LetterRackView extends HBox {
        public LetterRackView(LetterRack letterRack) {
            setSpacing(3);
            Iterator<LetterModel> it = letterRack.iterator();
            for (int i = 0; i < 7; i++) {
                LetterView view = new LetterView(90, 90, it.hasNext() ? it.next() : null);
                getChildren().add(view);
            }
        }
    }

    private static class LetterView extends StackPane {

        private final LetterModel letter;
        private Rectangle rectangle;

        public LetterView(int width, int height, LetterModel letter) {
            this.letter = letter;
            addRectangle(width, height);
            drawLetter();
        }

        private void addRectangle(int width, int height) {
            rectangle = new Rectangle(width, height, Color.BLANCHEDALMOND);
            rectangle.setStrokeWidth(1);
            rectangle.setStroke(Color.DARKSLATEGREY);
            getChildren().add(rectangle);
        }

        private void drawLetter() {
            if (letter == null) {
                return;
            }
            rectangle.setFill(Color.BURLYWOOD);
            HBox hBox = new HBox();
            Label ch = new Label(letter.charLetter + "");
            Label points = new Label(letter.points + "");
            ch.setFont(new Font(rectangle.getHeight()/1.3));
            points.setFont(new Font(rectangle.getHeight()/2.3));
            ch.setTextFill(Color.BLACK);
            points.setTextFill(Color.BLACK);
            hBox.getChildren().addAll(ch, points);
            getChildren().add(hBox);
        }
    }
}
