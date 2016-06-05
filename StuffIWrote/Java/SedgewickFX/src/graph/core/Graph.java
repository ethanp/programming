package graph.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Ethan Petuchowski 5/31/16
 */
public class Graph<NodeT extends GraphNode> {
    private final List<NodeT> nodes = new ArrayList<>();
    private final List<GraphEdge> edges = new ArrayList<>();

    public Graph() {
    }

    Graph(List<NodeT> nodes, List<GraphEdge> edges) {
        this.nodes.addAll(nodes);
        this.edges.addAll(edges);
    }

    private GraphNode getRandomNode() {
        return nodes.get(new Random().nextInt(nodes.size()));
    }

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

    public List<GraphNode> neighborsOf(GraphNode node) {
        return getAdjacencyList().get(node);
    }

    public void addNodes(List<NodeT> nodes) {
        this.nodes.addAll(nodes);
    }

    public void addRandomEdges(int numEdges) {
        for (int edgeNum = 0; edgeNum < numEdges; edgeNum++) {
            GraphEdge randomEdge = GraphEdge.from(getRandomNode()).to(getRandomNode());
            System.out.println("adding edge: "+randomEdge);
            edges.add(randomEdge);
        }
    }

    public List<GraphEdge> getEdges() {
        return edges;
    }

    public List<NodeT> getNodes() {
        return nodes;
    }
}
