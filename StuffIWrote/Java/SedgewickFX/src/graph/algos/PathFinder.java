package graph.algos;

import graph.core.Graph;
import graph.core.GraphEdge;
import graph.core.GraphNode;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

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
        class SearchState {
            private final GraphEdge currentEdge;
            private final Queue<GraphEdge> frontier = new ArrayDeque<>();

            private SearchState(GraphEdge edge) {
                currentEdge = edge;
            }
        }
        List<GraphEdge> edges = new ArrayList<>();
        Stack<SearchState> searchStack = new Stack<>();
        Set<GraphNode> seenSet = new HashSet<>();
        SearchState baseState = new SearchState(GraphEdge.from(null).to(fromNode));
        //noinspection Convert2streamapi
        for (GraphNode node : graph.neighborsOf(fromNode))
            baseState.frontier.add(GraphEdge.from(fromNode).to(node));

        // TODO THIS IS NOT FINISHED/CORRECT
        while (!searchStack.peek().frontier.isEmpty()) {
            GraphEdge edge = searchStack.peek().frontier.poll();
            if (edge.getToNode().equals(toNode)) {
                return searchStack.stream()
                    .map((SearchState state) -> state.currentEdge)
                    .collect(Collectors.toList());
            }
            else throw new NotImplementedException();
        }
        return Collections.emptyList();
    }
}
