package ch4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * 7/10/16 10:53 PM
 */
abstract class WeightedEdgeGraph {
    final Collection<ReversibleWeightedDirectedEdge>[] adjList;

    static Collection<ReversibleWeightedDirectedEdge> edgesFromString(String s) {
        Scanner sc = new Scanner(s);
        int numNodes = sc.nextInt();
        int numEdges = sc.nextInt();
        Collection<ReversibleWeightedDirectedEdge> edges = new ArrayList<>();
        for (int i = 0; i < numEdges; i++)
            edges.add(new ReversibleWeightedDirectedEdge(sc.nextInt(), sc.nextInt(), sc.nextDouble()));
        return edges;
    }

    WeightedEdgeGraph(int numNodes, Collection<ReversibleWeightedDirectedEdge> edges) {
        this.adjList = new Collection[numNodes];
        for (int i = 0; i < numNodes; i++)
            adjList[i] = new LinkedList<>();
        for (ReversibleWeightedDirectedEdge edge : edges)
            addEdge(edge);
    }

    abstract void addEdge(ReversibleWeightedDirectedEdge edge);

    public int V() {
        return adjList.length;
    }

    public Collection<ReversibleWeightedDirectedEdge> adj(int node) {
        return adjList[node];
    }
}
