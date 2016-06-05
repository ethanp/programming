package graph.algos;

import graph.core.Graph;
import graph.core.GraphEdge;
import graph.core.GraphNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Ethan Petuchowski 5/31/16
 */
public class PathFinder {

    private final GraphNode fromNode;
    private final GraphNode toNode;
    private final Graph<? extends GraphNode> graph;

    public PathFinder(GraphNode fromNode, GraphNode toNode, Graph<? extends GraphNode> graph) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.graph = graph;
    }

    /** @return empty list when no path is found */
    public List<GraphEdge> dfs() {
        Set<GraphNode> seenSet = new HashSet<>();
        seenSet.add(fromNode);
        for (GraphEdge edge : graph.edgesOutOf(fromNode)) {
            if (edge.getToNode().equals(toNode)) {
                return Collections.singletonList(edge);
            }
            List<GraphEdge> res = dfs(seenSet, edge.getToNode());
            if (res != null) {
                res.add(edge);
                Collections.reverse(res);
                return res;
            }
        }
        return Collections.emptyList();
    }
    private List<GraphEdge> dfs(Set<GraphNode> seenSet, GraphNode startNode) {
        seenSet.add(startNode);
        for (GraphEdge edge : graph.edgesOutOf(startNode)) {
            if (seenSet.contains(edge.getToNode()))
                continue;
            if (edge.getToNode().equals(toNode)) {
                List<GraphEdge> ret = new ArrayList<>();
                ret.add(edge);
                return ret;
            }
            List<GraphEdge> res = dfs(seenSet, edge.getToNode());
            if (res != null) {
                res.add(edge);
                return res;
            }
        }
        return null;
    }
}
