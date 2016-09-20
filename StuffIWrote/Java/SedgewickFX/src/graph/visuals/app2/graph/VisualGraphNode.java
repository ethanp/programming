package graph.visuals.app2.graph;

import javafx.geometry.Point2D;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/** representation of a user-created graph node */
public class VisualGraphNode {
    // is there a better place for this?
    private static VisualGraphNode draggedFrom = null;
    private final Circle circle;
    private final VisualGraph visualGraph;
    // probably best to use ints in practice
    private double weight = 0;
    private boolean active = false;
    private boolean tempActive = false;

    VisualGraphNode(Point2D location, VisualGraph visualGraph) {
        this.visualGraph = visualGraph;
        circle = new Circle(location.getX(), location.getY(), 25, Color.BLUE);
        setDragToCreateEdge();
        setClickHandlers();
    }

    @SuppressWarnings("UnnecessaryReturnStatement") private void setClickHandlers() {
        circle.setOnMouseClicked(event -> {
            // on left-click, toggle activation
            if (event.getButton() == MouseButton.PRIMARY) {
                active = !active;
                computeColor();
                event.consume();
                return;
            }
            // on right-click, delete the node
            if (event.getButton() == MouseButton.SECONDARY) {
                visualGraph.removeNode(this);
                event.consume();
                return;
            }
        });
    }

    private void setDragToCreateEdge() {
        circle.setOnDragDetected(event -> {
            System.out.println("starting edge creation");

            // set static state
            draggedFrom = this;

            // I think for some reason there needs to be actual content transmitted
            ClipboardContent content = new ClipboardContent();
            content.putString("circle drag"); // I think this is REQUIRED too

            // Confirms a potential drag and drop gesture that is recognized over this Node.
            // `ANY` indicates that we'll support the copying, linking, or moving of data.
            Dragboard dragboard = circle.startDragAndDrop(TransferMode.ANY);

            // dragboard is used to transfer data during the drag and drop gesture.
            // LowPriorityTodo Placing this Node's data on the Dragboard also identifies this Node
            // as the source of the drag and drop gesture. Maybe that would help us
            // cleanly get all the references we need for rendering the edge into one place.
            dragboard.setContent(content);

            setTemporarilyActive(true);

            // prevent further event propagation
            event.consume();
        });
        circle.setOnDragOver(event -> {
            setTemporarilyActive(true);
            event.acceptTransferModes(TransferMode.ANY);
        });

        // the drag exited this node
        circle.setOnDragExited(event -> {
            System.out.println("drag exited node");
            setTemporarilyActive(false);
        });

        // A click was released on this Node during drag and drop gesture.
        // Transfer of data from the DragEvent's dragboard should happen in this function.
        circle.setOnDragDropped(event -> {
            if (draggedFrom != null) {
                System.out.println("creating undirected edge");
                visualGraph.addEdge(draggedFrom, this);
                visualGraph.addEdge(this, draggedFrom);
            } else {
                System.err.println("couldn't create node: didn't drag from anywhere?");
            }
            setTemporarilyActive(false);
            event.setDropCompleted(true);
            event.consume();
        });
    }

    public void setIsActive(boolean b) {
        active = b;
        computeColor();
    }

    private void computeColor() {
        if (isTempActive()) {
            circle.setFill(Color.DARKSALMON);
        } else if (isActive()) {
            circle.setFill(Color.RED);
        } else {
            circle.setFill(Color.BLUE);
        }
    }

    public void setTemporarilyActive(boolean b) {
        tempActive = b;
        computeColor();
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

    public boolean isActive() {
        return active;
    }

    public boolean isTempActive() {
        return tempActive;
    }
}
