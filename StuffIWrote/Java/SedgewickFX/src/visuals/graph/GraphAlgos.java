package visuals.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Ethan Petuchowski 5/31/16
 */
public class GraphAlgos {

    /** @return empty list when no path is found */
    public static List<GraphEdge> findPath(GraphNode fromNode, GraphNode toNode, Graph<? extends GraphNode> graph) {
        List<GraphEdge> edges = new ArrayList<>();
        Stack<GraphEdge> searchStack = new Stack<>();
        Set<GraphNode> seenSet = new HashSet<>();
        Queue<GraphEdge> frontier = new ArrayDeque<>();

        //noinspection Convert2streamapi
        for (GraphNode node : graph.neighborsOf(fromNode))
            frontier.add(new GraphEdge(fromNode, node));

        // TODO THIS IS NOT FINISHED/CORRECT
        while (!frontier.isEmpty()) {
            GraphEdge edge = searchStack.pop();
            if (edge.getToNode().equals(toNode)) {
                return searchStack;
            }
        }
        return Collections.emptyList();
    }
}
