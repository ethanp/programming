package graph.algos;

import graph.core.BasicGraphNode;
import graph.core.Graph;
import graph.core.GraphEdge;
import graph.core.GraphNode;
import javafx.geometry.Point2D;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
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

    /** @return empty list when no path is found */
    public static List<GraphEdge> findPath(GraphNode fromNode, GraphNode toNode, Graph<? extends GraphNode> graph) {
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

    public static void main(String[] args) {
        System.out.println("testing PathFinder.findPath()");
        List<BasicGraphNode> nodes = Arrays.asList(
            new BasicGraphNode(new Point2D(10, 10)),
            new BasicGraphNode(new Point2D(20, 20)),
            new BasicGraphNode(new Point2D(30, 30)),
            new BasicGraphNode(new Point2D(40, 40))
        );
        List<GraphEdge> edges = new ArrayList<>();
        for (int i = 0; i < nodes.size() - 1; i++) {
            edges.add(GraphEdge
                .from(nodes.get(i))
                .to(nodes.get(i + 1)));
        }
        Graph<BasicGraphNode> graph = new Graph<BasicGraphNode>(nodes, edges);
        List<GraphEdge> result = PathFinder.findPath(nodes.get(0), nodes.get(nodes.size()-1), graph);
        System.out.println("results:");
        result.stream().forEach(System.out::println);
    }
}
