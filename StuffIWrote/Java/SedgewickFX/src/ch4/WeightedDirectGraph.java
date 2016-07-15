package ch4;

import java.util.Collection;
import java.util.Scanner;

/**
 * 7/10/16 10:53 PM
 */
class WeightedDirectGraph extends WeightedEdgeGraph {

    WeightedDirectGraph(int numNodes, Collection<ReversibleWeightedDirectedEdge> edges) {
        super(numNodes, edges);
    }

    static WeightedDirectGraph parseFromString(String s) {
        return new WeightedDirectGraph(new Scanner(s).nextInt(), WeightedEdgeGraph.edgesFromString(s));
    }

    @Override void addEdge(ReversibleWeightedDirectedEdge edge) {
        adjList[edge.from].add(edge);
    }
}

