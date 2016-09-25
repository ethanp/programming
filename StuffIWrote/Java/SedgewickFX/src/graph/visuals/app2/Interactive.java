package graph.visuals.app2;

import graph.visuals.app2.graph.VisualGraph;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Ethan Petuchowski 9/18/16
 */
public class Interactive {

    private final Pane sceneRoot;
    private final VisualGraph visualGraph;

    Interactive(Pane sceneRoot) {
        this.sceneRoot = sceneRoot;
        visualGraph = new VisualGraph(this);
        setOnBackgroundClickCreateNode();
        addAlgoButtons();
    }

    public ObservableList<Node> visualChildren() {
        return sceneRoot.getChildren();
    }

    private void addAlgoButtons() {
        Button shortestPathButton = new Button("Shortest Paths");
        shortestPathButton.setOnMouseClicked(event -> {
            System.out.println("shortest paths button clicked");
            try {
                new ShortestPath(visualGraph).highlightPath();
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        });
        visualChildren().add(shortestPathButton);
    }

    private void setOnBackgroundClickCreateNode() {
        sceneRoot.setOnMouseClicked(click -> visualGraph.addNodeAt(locationOf(click)));
    }

    private Point2D locationOf(MouseEvent click) {
        return new Point2D(click.getX(), click.getY());
    }
}
