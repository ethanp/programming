package scrabble.view;

import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import scrabble.model.LetterModel;

/**
 * 9/30/16 7:51 PM
 */
class RackTileView extends StackPane {

    private static RackTileView dragOriginator;
    private LetterModel letter;
    private Rectangle rectangle;
    private HBox hBox = new HBox();

    RackTileView(int width, int height, LetterModel letter) {
        this.letter = letter;
        addRectangle(width, height);
        getChildren().add(hBox);
        drawLetter();
        add_DragToPlace_Handler();
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
        /* maybe this is a bit sloppy */
        Label ch = new Label(String.valueOf(letter.charLetter));
        Label points = new Label(String.valueOf(letter.points));
        ch.setFont(new Font(rectangle.getHeight()/1.3));
        points.setFont(new Font(rectangle.getHeight()/2.3));
        ch.setTextFill(Color.BLACK);
        points.setTextFill(Color.BLACK);
        hBox.getChildren().clear();
        hBox.getChildren().addAll(ch, points);
    }

    private void add_DragToPlace_Handler() {
        this.setOnDragDetected(event -> {
            System.out.println("dragging " + letter);

            // set static state
            dragOriginator = this;

            // Confirms a potential drag and drop gesture that is recognized over this Node.
            // `ANY` indicates that we'll support the copying, linking, or moving of data.
            Dragboard dragboard = this.startDragAndDrop(TransferMode.ANY);

            // this is how we transmit content
            ClipboardContent content = new ClipboardContent();
            // let's transmit the LetterModel
            content.putString(letter.serializeToString());

            // dragboard is used to transfer data during the drag and drop gesture.
            // LowPriorityTodo Placing this Node's data on the Dragboard also identifies this Node
            // as the source of the drag and drop gesture. Maybe that would help us
            // cleanly get all the references we need for rendering the edge into one place.
            dragboard.setContent(content);

            // prevent further event propagation
            event.consume();
        });

        /* mark this node as eligible for dropping on */
        this.setOnDragOver(e -> e.acceptTransferModes(TransferMode.ANY));

        // ignore that the drag exited this node
        // this.setOnDragExited(event -> {
        //    System.out.println("drag exited node");
        // });

        /* swap content with the node that was dropped */
        this.setOnDragDropped(event -> {
            LetterModel tmp = dragOriginator.letter;
            dragOriginator.letter = letter;
            letter = tmp;
            dragOriginator.drawLetter();
            drawLetter();
            dragOriginator = null;
            event.setDropCompleted(true);
            event.consume();
        });
    }
}
