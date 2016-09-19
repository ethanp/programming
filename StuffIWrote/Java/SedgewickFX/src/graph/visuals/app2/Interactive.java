package graph.visuals.app2;

import graph.visuals.app2.graph.VisualGraph;
import graph.visuals.app2.graph.VisualGraphNode;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Ethan Petuchowski 6/5/16
 */
public class Interactive {

    private final Scene scene;
    private final VisualGraph visualGraph;

    Interactive(Scene scene) {
        this.scene = scene;
        visualGraph = new VisualGraph(this);
        createNodeOnSceneClick();
        addAlgoButtons();
    }

    private Group rootGroup() {
        return (Group) scene.getRoot();
    }

    public ObservableList<Node> visualChildren() {
        return rootGroup().getChildren();
    }

    private void addAlgoButtons() {
        Button shortestPathButton = new Button("Shortest Paths");
        shortestPathButton.setOnMouseClicked(event -> {
            System.out.println("shortest paths button clicked");
            new ShortestPath(visualGraph).highlightPath();
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

class ShortestPath {
    private final VisualGraph visualGraph;
    private final VisualGraphNode from;
    private final VisualGraphNode to;
    private List<VisualGraphNode> path;

    ShortestPath(VisualGraph graph) {
        List<VisualGraphNode> activeNodes = graph.getActiveNodes();
        if (activeNodes.size() != 2) {
            System.err.println("there must be only 2 active nodes for this algorithm");
        }
        this.from = activeNodes.get(0);
        this.to = activeNodes.get(1);
        visualGraph = graph;
    }

    public void highlightPath() {
        if (path == null) path = findShortestPath();
        visualGraph.setOnlyTheseNodesActive(path);
    }

    /**
     * @return the path of nodes, or null if none is found
     */
    private List<VisualGraphNode> findShortestPath() {
        class BFSNode {
            final VisualGraphNode node;
            final BFSNode previous;

            BFSNode(VisualGraphNode node, BFSNode previous) {
                this.node = node;
                this.previous = previous;
            }
        }
        // TODO since there are no weights, we can simply bfs, no?
        Queue<BFSNode> frontier = new ArrayDeque<>();
        frontier.add(new BFSNode(from, null));
        while (!frontier.isEmpty()) {
            BFSNode curr = frontier.poll();
            // intentional reference equality comparison
            for (VisualGraphNode nbr : visualGraph.adj(curr.node)) {
                if (nbr == to) {
                    List<VisualGraphNode> ret = new ArrayList<>();
                    ret.add(nbr);
                    for (BFSNode b = curr; b.previous != null; b = b.previous) {
                        ret.add(b.node);
                    }
                    return ret;
                } else {
                    frontier.add(new BFSNode(nbr, curr));
                }
            }
        }
        return null;
    }
}
