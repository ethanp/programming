package graph.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Ethan Petuchowski 5/31/16
 */
public class Graph<NodeT extends GraphNode> {
    private final List<NodeT> nodes = new ArrayList<>();
    private final List<GraphEdge> edges = new ArrayList<>();

    public Graph() {
    }

    /**
     * Given nodes and edges will by _copied_ into an internal list; references to the given lists
     * will not be saved by this Graph.
     */
    public Graph(List<NodeT> nodes, List<GraphEdge> edges) {
        this.nodes.addAll(nodes);
        this.edges.addAll(edges);
    }

    private GraphNode getRandomNode() {
        return nodes.get(new Random().nextInt(nodes.size()));
    }

    /**
     * The adjacency list is always built from scratch and returned by this method
     */
    private Map<GraphNode, List<GraphNode>> getAdjacencyList() {
        Map<GraphNode, List<GraphNode>> adjList = new HashMap<>();
        for (GraphEdge e : edges) {
            GraphNode fromNode = e.getFromNode();
            GraphNode toNode = e.getToNode();
            if (!adjList.containsKey(fromNode)) {
                adjList.put(fromNode, new ArrayList<>());
            }
            adjList.get(fromNode).add(toNode);
        }
        return adjList;
    }

    public List<GraphEdge> edgesOutOf(GraphNode node) {
        return edges.stream()
            .filter(e -> e.getFromNode().equals(node))
            .collect(Collectors.toList());
    }

    public List<GraphNode> neighborsOf(GraphNode node) {
        return new ArrayList<>(getAdjacencyList().get(node));
    }

    /**
     * Given nodes and edges will by _copied_ into an internal list; references to the given lists
     * will not be saved by this Graph.
     */
    public void addNodes(List<NodeT> nodes) {
        this.nodes.addAll(nodes);
    }

    public void addRandomEdges(int numEdges) {
        for (int edgeNum = 0; edgeNum < numEdges; edgeNum++) {
            GraphEdge randomEdge = GraphEdge.from(getRandomNode()).to(getRandomNode());
            System.out.println("adding edge: " + randomEdge);
            edges.add(randomEdge);
        }
    }

    public List<GraphEdge> getEdges() {
        return new ArrayList<>(edges);
    }

    public List<NodeT> getNodes() {
        return new ArrayList<>(nodes);
    }
}
