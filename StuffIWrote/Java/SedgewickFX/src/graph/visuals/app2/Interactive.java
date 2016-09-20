package graph.visuals.app2;

import graph.visuals.app2.graph.VisualGraph;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Ethan Petuchowski 6/5/16
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
            new ShortestPath(visualGraph).highlightPath();
        });
        visualChildren().add(shortestPathButton);
    }

    private void setOnBackgroundClickCreateNode() {
        // Setting onMouseClicked on the rootGroup() instead doesn't work.
        // This is probably because "a Group will take on the collective bounds of its children",
        // so when there are no children, the Group gives you no area on the Scene to click on.
        sceneRoot.setOnMouseClicked(click -> visualGraph.addNodeAt(locationOf(click)));
    }

    private Point2D locationOf(MouseEvent click) {
        return new Point2D(click.getX(), click.getY());
    }
}
