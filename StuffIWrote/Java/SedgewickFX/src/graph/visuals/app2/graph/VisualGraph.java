package graph.visuals.app2.graph;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;

/** representation of the whole user-created graph container */
public class VisualGraph {
    private final ObservableMap<VisualGraphNode, ObservableList<VisualGraphEdge>> adjList =
          FXCollections.observableHashMap();
    private final ObservableList<Node> rootChildren;

    public VisualGraph(Group sceneRoot) {
        this.rootChildren = sceneRoot.getChildren();

        MapChangeListener<VisualGraphNode, ObservableList<VisualGraphEdge>> nodeChanges = change -> {
            // NB: added AND removed will BOTH be true if an item was REPLACED
            if (change.wasAdded() && change.wasRemoved()) {
                /* IGNORE */
            } else if (change.wasAdded()) {
                rootChildren.add(change.getKey().getCircle());
            } else if (change.wasRemoved()) {
                rootChildren.remove(change.getKey().getCircle());
            }
        };
        adjList.addListener(nodeChanges);
    }

    public void addNodeAt(Point2D point) {
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

                // this change was ONLY an 'add' inclusive-OR 'remove' change
                else {
                    /* update rootChildren to reflect whatever adds and removes occurred */
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

        // this triggers the listener on the adjList, which will cause the new node to be
        // rendered to the screen
        adjList.put(new VisualGraphNode(point), edges);
    }
}
