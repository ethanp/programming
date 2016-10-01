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

    private static RackTileView draggedFrom;
    private final LetterModel letter;
    private Rectangle rectangle;

    public RackTileView(int width, int height, LetterModel letter) {
        this.letter = letter;
        addRectangle(width, height);
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

    private void add_DragToPlace_Handler() {
        this.setOnDragDetected(event -> {
            System.out.println("starting edge creation");

            // set static state
            draggedFrom = this;

            // this is how we transmit content
            ClipboardContent content = new ClipboardContent();
            // TODO let's transmit the LetterModel
            content.putString("letterView drag");

            // Confirms a potential drag and drop gesture that is recognized over this Node.
            // `ANY` indicates that we'll support the copying, linking, or moving of data.
            Dragboard dragboard = this.startDragAndDrop(TransferMode.ANY);

            // dragboard is used to transfer data during the drag and drop gesture.
            // LowPriorityTodo Placing this Node's data on the Dragboard also identifies this Node
            // as the source of the drag and drop gesture. Maybe that would help us
            // cleanly get all the references we need for rendering the edge into one place.
            dragboard.setContent(content);

            setTemporarilyActive(true);

            // prevent further event propagation
            event.consume();
        });
        this.setOnDragOver(event -> {
            setTemporarilyActive(true);
            event.acceptTransferModes(TransferMode.ANY);
        });

        // the drag exited this node
        this.setOnDragExited(event -> {
            System.out.println("drag exited node");
            setTemporarilyActive(false);
        });

        // A click was released on this Node during drag and drop gesture.
        // Transfer of data from the DragEvent's dragboard should happen in this function.
        this.setOnDragDropped(event -> {
            if (draggedFrom != null) {
                System.out.println("creating undirected edge");
            } else {
                System.err.println("couldn't create node: didn't drag from anywhere?");
            }
            setTemporarilyActive(false);
            event.setDropCompleted(true);
            event.consume();
        });
    }

    private void setTemporarilyActive(boolean b) {

    }
}
