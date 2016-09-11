package ch4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * 7/15/16 10:31 AM
 */
class DirectGraph {
    private Collection<DirectedEdge>[] adjList;

    DirectGraph(int numNodes, Collection<DirectedEdge> edges) {
        this.adjList = new Collection[numNodes];
        for (int i = 0; i < numNodes; i++)
            adjList[i] = new LinkedList<>();
        for (DirectedEdge edge : edges)
            adjList[edge.from].add(edge);
    }

    public int V() {
        return adjList.length;
    }

    public Collection<DirectedEdge> adj(int node) {
        return adjList[node];
    }

    public DirectGraph copyGraph() {
        Collection<DirectedEdge> edges = new ArrayList<>();
        for (Collection<DirectedEdge> lst : adjList) edges.addAll(lst);
        return new DirectGraph(adjList.length, edges);
    }
}
