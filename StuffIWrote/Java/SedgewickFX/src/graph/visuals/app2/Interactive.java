package graph.visuals.app2;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * Ethan Petuchowski 6/5/16
 */
class Interactive {

    private final Scene scene;
    private Group root() {
        return (Group) scene.getRoot();
    }
    private ObservableList<Node> visualChildren() {
        return root().getChildren();
    }

    private EventHandler<MouseEvent> createNode =
          click -> addNodeAt(locationOf(click));

    Interactive(Scene scene) {
        this.scene = scene;
        createNodeOnSceneClick();
        addAlgoButtons();
    }

    private void addAlgoButtons() {
        Button shortestPathButton = new Button("Shortest Paths");
        shortestPathButton.setOnMouseClicked(event -> {
            System.out.println("shortest paths button clicked");
        });
        visualChildren().add(shortestPathButton);
    }

    private void createNodeOnSceneClick() {
        // Defines a function to be called when a mouse button
        // has been clicked (pressed and released) on this Node.
        scene.setOnMouseClicked(createNode);
    }

    private Point2D locationOf(MouseEvent click) {
        return new Point2D(click.getX(), click.getY());
    }

    void addNodeAt(Point2D point) {

    }
}
