package graph.visuals.app2;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.scene.shape.Line;

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
    private final ObservableMap<VisualGraphNode, ObservableList<VisualGraphEdge>> adjList =
          FXCollections.observableHashMap();
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

    VisualGraph(Group sceneRoot) {
        this.rootChildren = sceneRoot.getChildren();
        adjList.addListener(renderGraphNodeChanges);
    }

    void addNodeAt(Point2D point) {
        ObservableList<VisualGraphEdge> edges = FXCollections.observableArrayList();

        // Represents a report of a changes done to an ObservableList.
        /* we use this listener to ensure that new edges are reflected in the rendering */
        ListChangeListener<VisualGraphEdge> edgeListChangeListener = change -> {
            // May consist of one or more actual changes and must be iterated by next() method.
            while (change.next()) {
                // this change was ONLY a permutation (rearranging of existing items)
                if (change.wasPermutated()) {
                    /* I think this requires no UI update, and I don't expect it to even happen */
                    System.out.println("a list 'permutation' happened; just so you know");
                }
                // this change was ONLY an update change
                else if (change.wasUpdated()) {
                    /* I think this requires no UI update, and I don't expect it to even happen */
                    System.out.println("a list 'update' happened; just so you know");
                }

                // this change was ONLY an add inclusive-OR remove change
                else {
                    // update rootChildren to reflect whatever adds and removes occurred
                    for (VisualGraphEdge e : change.getRemoved()) {
                        rootChildren.remove(e.getLine());
                    }
                    for (VisualGraphEdge e : change.getAddedSubList()) {
                        rootChildren.add(e.getLine());
                    }
                }
            }
        };
        edges.addListener(edgeListChangeListener);
        adjList.put(new VisualGraphNode(point), edges);
    }
}

/** representation of a user-created graph node */
class VisualGraphNode {
    private final Circle circle;

    VisualGraphNode(Point2D location) {
        circle = new Circle(location.getX(), location.getY(), 25, Color.BLUE);
    }

    public void setInactive() {
        circle.setFill(Color.BLUE);
    }

    public void setActive() {
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

class VisualGraphEdge {
    private final Line line;
    private final VisualGraphNode from;
    private final VisualGraphNode to;

    public VisualGraphEdge(VisualGraphNode from, VisualGraphNode to) {
        this.line = new Line(from.xLoc(), from.yLoc(), to.xLoc(), to.yLoc());
        this.line.setFill(Color.LIGHTBLUE);
        this.from = from;
        this.to = to;
    }

    public Line getLine() {
        return line;
    }
}
