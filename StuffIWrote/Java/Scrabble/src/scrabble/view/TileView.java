package scrabble.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import scrabble.model.LetterModel;
import scrabble.model.TileModel;

/**
 * 9/30/16 3:06 AM
 */
class TileView extends StackPane {
    /** underlying model */
    private final TileModel tileModel;

    /* view parameters */
    private double width;
    private double height;

    /* view elements */
    private Rectangle rectangle;

    TileView(double width, double height, TileModel tileModel) {
        this.width = width;
        this.height = height;
        this.tileModel = tileModel;
        renderBaseRectangle();
    }

    private void renderBaseRectangle() {
        this.rectangle = new Rectangle(width, height);
        rectangle.setFill(tileModel.getColor());
        rectangle.setStrokeWidth(4);
        rectangle.setStroke(Color.BLACK);
        getChildren().add(rectangle);
    }

    void setLetterModel(LetterModel letterModel) {
        if (tileModel.getOccupantLetterModel() != null) {
            System.err.println("there's already a letter here");
            return;
        }
        tileModel.setOccupantLetterModel(letterModel);
        Label letterLabel = new Label(letterModel.charLetter + "");
        Label pointsLabel = new Label(letterModel.points + "");
        letterLabel.setFont(new Font(height/2));
        pointsLabel.setFont(new Font(height/4));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(letterLabel, pointsLabel);
        getChildren().add(hBox);

        rectangle.setFill(Color.LIGHTYELLOW);
    }
}
