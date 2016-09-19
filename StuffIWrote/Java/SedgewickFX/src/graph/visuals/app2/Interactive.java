package graph.visuals.app2;

import graph.visuals.app2.graph.VisualGraph;
import javafx.collections.ObservableList;
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
    private final VisualGraph visualGraph;

    Interactive(Scene scene) {
        this.scene = scene;
        visualGraph = new VisualGraph(rootGroup());
        createNodeOnSceneClick();
        addAlgoButtons();
    }

    private Group rootGroup() {
        return (Group) scene.getRoot();
    }

    private ObservableList<Node> visualChildren() {
        return rootGroup().getChildren();
    }

    private void addAlgoButtons() {
        Button shortestPathButton = new Button("Shortest Paths");
        shortestPathButton.setOnMouseClicked(event -> {
            System.out.println("shortest paths button clicked");
        });
        visualChildren().add(shortestPathButton);
    }

    private void createNodeOnSceneClick() {
        scene.setOnMouseClicked(click -> visualGraph.addNodeAt(locationOf(click)));
    }

    private Point2D locationOf(MouseEvent click) {
        return new Point2D(click.getX(), click.getY());
    }
}

