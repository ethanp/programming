package ch4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 7/15/16 10:23 AM
 *
 * This prints the right output, but not exactly in the same order as the printout in the book
 * because the sorting is not unique.
 */
public class TopologicalSort {
    final DirectGraph graph;
    final List<Integer> sorted;
    final boolean[] seen;

    public TopologicalSort(DirectGraph graph) {
        this.graph = graph;
        this.sorted = new ArrayList<>();
        this.seen = new boolean[graph.V()];
        sort();
    }

    public static void main(String[] args) {
        givenTest();
    }

    private static void givenTest() {
        DirectGraph graph = givenTopicGraphPg575();
        TopologicalSort elTopo = new TopologicalSort(graph);
        for (int i : elTopo.getSorted()) {
            System.out.println(i);
        }
    }

    private static DirectGraph givenTopicGraphPg575() {
        Collection<DirectedEdge> edges = new ArrayList<>();
        edges.add(new DirectedEdge(0, 1));
        edges.add(new DirectedEdge(0, 5));
        edges.add(new DirectedEdge(0, 6));
        edges.add(new DirectedEdge(2, 0));
        edges.add(new DirectedEdge(2, 3));
        edges.add(new DirectedEdge(3, 5));
        edges.add(new DirectedEdge(5, 4));
        edges.add(new DirectedEdge(6, 4));
        edges.add(new DirectedEdge(6, 9));
        edges.add(new DirectedEdge(7, 6));
        edges.add(new DirectedEdge(8, 7));
        edges.add(new DirectedEdge(9, 10));
        edges.add(new DirectedEdge(9, 11));
        edges.add(new DirectedEdge(9, 12));
        edges.add(new DirectedEdge(11, 12));
        return new DirectGraph(13, edges);
    }

    private void sort() {
        if (new DirectedCycle(graph).hasCycle())
            throw new IllegalStateException("cannot sort graph with cycle");
        for (int v = 0; v < graph.V(); v++)
            if (!seen[v])
                dfs(v);
        Collections.reverse(sorted);
    }

    private void dfs(int v) {
        seen[v] = true;
        for (DirectedEdge e : graph.adj(v))
            if (!seen[e.to])
                dfs(e.to);
        sorted.add(v);
    }

    public List<Integer> getSorted() {
        return new ArrayList<>(sorted);
    }
}
