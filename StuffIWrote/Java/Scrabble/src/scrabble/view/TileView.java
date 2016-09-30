package scrabble.view;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import scrabble.model.LetterModel;
import scrabble.model.TileModel;

/**
 * 9/30/16 3:06 AM
 */
class TileView extends StackPane {
    /* underlying model */
    private final TileModel tileModel;

    /* view parameters */
    private double width;
    private double height;

    /* view elements */
    private Rectangle rectangle;

    public TileView(double width, double height, TileModel tileModel) {
        this.width = width;
        this.height = height;
        this.tileModel = tileModel;
        this.rectangle = new Rectangle(width, height, tileModel.getColor());
        rectangle.setStrokeWidth(4);
        rectangle.setStroke(Color.BLACK);
        getChildren().add(rectangle);
    }

    public void setLetterModel(LetterModel letterModel) {
        if (tileModel.getOccupantLetterModel() != null) {
            System.err.println("there's already a letter here");
            return;
        }
        tileModel.setOccupantLetterModel(letterModel);
        Label letterLabel = new Label(letterModel.charLetter + "");
        Label pointsLabel = new Label(letterModel.points + "");
        letterLabel.setFont(new Font(height/2));
        pointsLabel.setFont(new Font(height/4));

        // this is a pretty sloppy-looking way to do it
        VBox vBox = new VBox();
        vBox.getChildren().addAll(letterLabel, pointsLabel);
        getChildren().add(vBox);

        rectangle.setFill(Color.LIGHTYELLOW);
    }
}
