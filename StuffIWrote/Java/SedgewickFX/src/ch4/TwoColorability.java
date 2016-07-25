package ch4;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 7/24/16 6:42 PM
 *
 * Support this query: Can the vertices of a given graph be assigned one of two colors in such a way
 * that no edge connects vertices of the same color?
 *
 * Which is equivalent to this question: Is the graph bipartite?
 */
public class TwoColorability {
    final static String[] bipartiteText = {
        "13",
        "0 1 2 5 6",
        "1 3",
        "3 5",
        "4 5 6",
        "6 7",
        "7 8",
        "8 10",
        "9 10 11",
        "10 12",
        "11 12"
    };
    final static String[] nonBipartiteText = {
        "13",
        "0 1 2 5 6",
        "1 2 3",
        "3 5",
        "4 5 6",
        "6 7",
        "7 8",
        "8 10",
        "9 10 11",
        "10 12",
        "11 12"
    };
    private final WeightedIndirectGraph graph;
    boolean[] marked;
    boolean[] isRed;
    private boolean isTwoColorable;

    public TwoColorability(WeightedIndirectGraph graph) {
        this.graph = graph;
        marked = new boolean[graph.V()];
        isRed = new boolean[graph.V()];
        checkColorability();
    }

    public static void main(String[] args) {
        testWorksForText(bipartiteText);  // should be true
        testWorksForText(nonBipartiteText);  // should be false
    }

    private static void testWorksForText(String[] bipartiteText) {
        Collection<ReversibleWeightedDirectedEdge> edges = new ArrayList<>();
        int numNodes = Integer.parseInt(bipartiteText[0]);
        for (int i = 1; i < bipartiteText.length; i++) {
            String[] split = bipartiteText[i].split(" ");
            for (int j = 1; j < split.length; j++) {
                edges.add(new ReversibleWeightedDirectedEdge(
                    Integer.parseInt(split[0]),
                    Integer.parseInt(split[j]),
                    0
                ));
            }
        }
        WeightedIndirectGraph graph = new WeightedIndirectGraph(numNodes, edges);
        TwoColorability colorability = new TwoColorability(graph);
        System.out.println(colorability.isTwoColorable());
    }

    private void checkColorability() {
        isTwoColorable = true;
        for (int v = 0; v < graph.V(); v++) {
            if (!marked[v] && !markWithColorsDFS(v)) {
                isTwoColorable = false;
                break;
            }
        }
    }

    private boolean markWithColorsDFS(int u) {
        marked[u] = true;
        for (ReversibleWeightedDirectedEdge edge : graph.adj(u)) {
            int v = edge.to;
            if (!marked[v]) {
                isRed[v] = !isRed[u];
                if (!markWithColorsDFS(v)) {
                    return false;
                }
            } else if (isRed[v] == isRed[u]) {
                return false;
            }
        }
        return true;
    }

    public boolean isTwoColorable() {
        return isTwoColorable;
    }
}
