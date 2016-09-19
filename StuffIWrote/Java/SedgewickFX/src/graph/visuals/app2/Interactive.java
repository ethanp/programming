package graph.visuals.app2;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;

/**
 * Ethan Petuchowski 6/5/16
 */
class Interactive {

    private final Scene scene;
    VisualGraph visualGraph = new VisualGraph(root());

    Interactive(Scene scene) {
        this.scene = scene;
        createNodeOnSceneClick();
        addAlgoButtons();
    }

    private Group root() {
        return (Group) scene.getRoot();
    }

    private ObservableList<Node> visualChildren() {
        return root().getChildren();
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
        scene.setOnMouseClicked(click -> visualGraph.addNodeAt(locationOf(click)));
    }

    private Point2D locationOf(MouseEvent click) {
        return new Point2D(click.getX(), click.getY());
    }
}

/** representation of the whole user-created graph container */
class VisualGraph {
    private final ObservableMap<VisualGraphNode, ObservableList<VisualGraphEdge>> adjList;
    private final ObservableList<Node> rootChildren;
    private MapChangeListener<VisualGraphNode, ObservableList<VisualGraphEdge>> renderGraphNodeChanges =
          new MapChangeListener<VisualGraphNode, ObservableList<VisualGraphEdge>>() {
              @Override public void onChanged(Change<
                    ? extends VisualGraphNode,
                    ? extends ObservableList<VisualGraphEdge>> change) {
                  // NB: added AND removed will BOTH be true if an item was REPLACED
                  if (change.wasAdded() && change.wasRemoved()) {
                      /* IGNORE */
                  } else if (change.wasAdded()) {
                      rootChildren.add(change.getKey().getCircle());
                  } else if (change.wasRemoved()) {
                      rootChildren.remove(change.getKey().getCircle());
                  }
              }
          };

    {
        Map<VisualGraphNode, ObservableList<VisualGraphEdge>> underlying = new HashMap<>();
        adjList = FXCollections.observableMap(underlying);
        adjList.addListener(renderGraphNodeChanges);
    }

    VisualGraph(Group sceneRoot) {
        this.rootChildren = sceneRoot.getChildren();
    }

    void addNodeAt(Point2D point) {

    }
}

/** representation of a user-created graph node */
class VisualGraphNode {
    private Circle circle;

    VisualGraphNode(Point2D location) {
        circle = new Circle(location.getX(), location.getY(), 25, Color.BLUE);
    }

    public Circle getCircle() {
        return circle;
    }
}

class VisualGraphEdge {

}
