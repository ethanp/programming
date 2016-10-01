package scrabble.view;

import javafx.scene.control.Label;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import scrabble.model.LetterModel;
import scrabble.model.Player;
import scrabble.model.ScrabbleGame;
import scrabble.model.TileModel;

/**
 * 9/30/16 3:06 AM
 *
 * Each tile on the board is one of these. The underlying TileModel state
 * may or may not have an "occupantLetterModel".
 */
class BoardTileView extends StackPane {
    /** underlying model */
    private final TileModel tileModel;

    /* view parameters */
    private double width;
    private double height;

    /* view elements */
    private Rectangle rectangle;

    BoardTileView(double width, double height, TileModel tileModel) {
        this.width = width;
        this.height = height;
        this.tileModel = tileModel;
        renderBaseRectangle();

        /* mark this node as eligible for dropping on (required to receive dropped event) */
        this.setOnDragOver(e -> e.acceptTransferModes(TransferMode.ANY));

        this.setOnDragDropped(dragEvent -> {
            LetterModel letterModel = LetterModel.extractFromDragEvent(dragEvent);

            /* add letter to this tile, if one is not already present */
            if (setLetterModel(letterModel)) {
                // go to far sights to get the game
                ScrabbleGame game = tileModel.getBoardModel().getGame();

                /* add dropped letter to the list of nodes that may be "reset" at player's whim */
                game.setPendingConfirmation(tileModel);

                /* add dropped letter to the game board, and remove it from the player's rack */
                Player currentPlayer = game.getCurrentPlayer();
                currentPlayer.playLetterAtSquare(letterModel, tileModel.row, tileModel.col);
            }
            dragEvent.setDropCompleted(true);
            dragEvent.consume();
        });
    }

    private void renderBaseRectangle() {
        this.rectangle = new Rectangle(width, height);
        rectangle.setFill(tileModel.getColor());
        rectangle.setStrokeWidth(4);
        rectangle.setStroke(Color.BLACK);
        getChildren().add(rectangle);
    }

    boolean setLetterModel(LetterModel letterModel) {
        if (tileModel.getOccupantLetterModel() != null) {
            System.out.println("there's already a letter here");
            return false;
        }
        tileModel.placeLetter(letterModel);

        // TODO this should be a method, because it's duplicated in the rack view
        Label letterLabel = new Label(" " + letterModel.charLetter);
        Label pointsLabel = new Label("" + letterModel.points);
        letterLabel.setFont(new Font(height/1.3));
        pointsLabel.setFont(new Font(height/(letterModel.points < 10 ? 2.3 : 3)));
        HBox hBox = new HBox();
        // hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(letterLabel, pointsLabel);
        getChildren().add(hBox);

        rectangle.setFill(Color.LIGHTYELLOW);
        return true;
    }
}
