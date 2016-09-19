package graph.visuals.app2.graph;

import graph.visuals.app2.Interactive;
import javafx.geometry.Point2D;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/** representation of a user-created graph node */
class VisualGraphNode {
    private final Circle circle;

    VisualGraphNode(Point2D location) {
        circle = new Circle(location.getX(), location.getY(), 25, Color.BLUE);
        circle.setOnDragDetected(event -> {
            System.out.println("starting edge creation");
            ClipboardContent content = new ClipboardContent();
            content.putString("circle drag"); // this is REQUIRED (and useless!)

            // Confirms a potential drag and drop gesture that is recognized over this Node.
            // `ANY` indicates that we'll support the copying, linking, or moving of data.
            Dragboard dragboard = circle.startDragAndDrop(TransferMode.ANY);

            // dragboard is used to transfer data during the drag and drop gesture.
            // TODO Placing this Node's data on the Dragboard also identifies this Node
            // as the source of the drag and drop gesture. Maybe that would help us
            // cleanly get all the references we need for rendering the edge into one place.
            dragboard.setContent(content);

            setColorActive();

            // prevent further event propagation
            event.consume();
        });
        circle.setOnDragOver(event -> {
            setColorActive();
            event.acceptTransferModes(TransferMode.ANY);
        });
        circle.setOnDragExited(event -> setColorInactive());

        // TODO there must a better place for this
        // I'd like to find a non-hacky way to pull this off
        circle.setOnDragDropped(event -> {
            System.out.println("circular drag dropped");
            VisualGraphNode from = null;
            VisualGraphNode to = this;
            Interactive interactive = null;
            // TODO something like this:
            // interactive.drawEdge(from, to);
            setColorInactive();
            event.setDropCompleted(true);
            event.consume();
        });
    }

    public void setColorInactive() {
        circle.setFill(Color.BLUE);
    }

    public void setColorActive() {
        circle.setFill(Color.RED);
    }

    public Circle getCircle() {
        return circle;
    }

    public double xLoc() {
        return circle.getCenterX();
    }

    public double yLoc() {
        return circle.getCenterY();
    }
}
