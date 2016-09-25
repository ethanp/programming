package graph.visuals.app2;

import graph.visuals.app2.graph.VisualGraph;
import graph.visuals.app2.graph.VisualGraphNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * 9/19/16 2:21 AM
 */
class ShortestPath {
    private final VisualGraph visualGraph;
    private final VisualGraphNode from;
    private final VisualGraphNode to;
    private List<VisualGraphNode> path;

    ShortestPath(VisualGraph graph) {
        List<VisualGraphNode> activeNodes = graph.getActiveNodes();
        if (activeNodes.size() != 2) {
            throw new IllegalArgumentException(
                  "there must be exactly 2 active nodes for this algorithm");
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
        // TODO once we add weights, we can't bfs. I think one option is that we Dijkstra's.
        Queue<BFSNode> frontier = new ArrayDeque<>();
        Set<VisualGraphNode> seen = new HashSet<>();
        seen.add(from);
        frontier.add(new BFSNode(from, null));
        while (!frontier.isEmpty()) {
            BFSNode curr = frontier.poll();
            for (VisualGraphNode nbr : visualGraph.adj(curr.node)) {
                /* if we found the target */
                if (nbr == to) {
                    /* return the path */
                    List<VisualGraphNode> path = new ArrayList<>();
                    path.add(nbr);
                    for (BFSNode b = curr; b.previous != null; b = b.previous) {
                        path.add(b.node);
                    }
                    path.add(from);
                    Collections.reverse(path);
                    return path;
                } else if (!seen.contains(nbr)) {
                    seen.add(nbr);
                    frontier.add(new BFSNode(nbr, curr));
                }
            }
        }
        return null;
    }
}
