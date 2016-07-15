package ch4;

import java.util.Collection;

/**
 * 7/10/16 10:53 PM
 */
class WeightedIndirectGraph extends WeightedEdgeGraph {

    WeightedIndirectGraph(int numNodes, Collection<ReversibleWeightedDirectedEdge> edges) {
        super(numNodes, edges);
    }

    @Override void addEdge(ReversibleWeightedDirectedEdge edge) {
        adjList[edge.from].add(edge);
        adjList[edge.to].add(edge.reverse());
    }
}
