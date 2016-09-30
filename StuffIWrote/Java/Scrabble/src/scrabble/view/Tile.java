package scrabble.view;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import scrabble.model.Letter;

/**
 * 9/30/16 3:06 AM
 */
class Tile extends StackPane {
    private double width;
    private double height;
    private Letter letter;
    private Rectangle rectangle;

    public Tile(double width, double height) {
        this.width = width;
        this.height = height;
        this.rectangle = new Rectangle(width, height, Color.BURLYWOOD);
        rectangle.setStrokeWidth(4);
        rectangle.setStroke(Color.BLACK);
        getChildren().add(rectangle);
    }

    public void setLetter(Letter letter) {
        if (this.letter != null) {
            System.err.println("there's already a letter here");
            return;
        }
        this.letter = letter;

        Label letterLabel = new Label(letter.charLetter + "");
        Label pointsLabel = new Label(letter.points + "");
        letterLabel.setFont(new Font(height/2));
        pointsLabel.setFont(new Font(height/4));

        // this is a pretty sloppy-looking way to do it
        VBox vBox = new VBox();
        vBox.getChildren().addAll(letterLabel, pointsLabel);
        getChildren().add(vBox);

        rectangle.setFill(Color.LIGHTYELLOW);
    }
}
