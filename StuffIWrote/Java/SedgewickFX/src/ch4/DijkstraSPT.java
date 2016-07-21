package ch4;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 7/10/16 10:53 PM
 *
 * They call the Shortest Paths Tree centered at a node n the "SPT of n"
 */
public class DijkstraSPT {
    final WeightedDirectGraph graph;
    final double[] distTo;
    final ReversibleWeightedDirectedEdge[] parentLink;

    public DijkstraSPT(WeightedDirectGraph graph) {
        this.graph = graph;

        distTo = new double[graph.V()];
        Arrays.fill(distTo, Double.POSITIVE_INFINITY);
        parentLink = new ReversibleWeightedDirectedEdge[graph.V()];
        distTo[0] = 0;
        build();
    }

    public static void main(String[] args) {
        runGivenTest();
    }

    private static void runGivenTest() {
        WeightedDirectGraph graph = WeightedDirectGraph.parseFromString(tinyEWDtxt());
        String actualOutput = new DijkstraSPT(graph).getPrintString();
        System.out.println(actualOutput);
        System.out.println(expectedOutput().equals(actualOutput));
    }

    @NotNull private static String tinyEWDtxt() {
        return "" +
            "8\n" +
            "15\n" +
            "4 5 0.35\n" +
            "5 4 0.35\n" +
            "4 7 0.37\n" +
            "5 7 0.28\n" +
            "7 5 0.28\n" +
            "5 1 0.32\n" +
            "0 4 0.38\n" +
            "0 2 0.26\n" +
            "7 3 0.39\n" +
            "1 3 0.29\n" +
            "2 7 0.34\n" +
            "6 2 0.40\n" +
            "3 6 0.52\n" +
            "6 0 0.58\n" +
            "6 4 0.93";
    }

    @NotNull private static String expectedOutput() {
        return "" +
            "0 to 0 (0.00):\n" +
            "0 to 1 (1.05): 0->4 0.38  4->5 0.35  5->1 0.32\n" +
            "0 to 2 (0.26): 0->2 0.26\n" +
            "0 to 3 (0.99): 0->2 0.26  2->7 0.34  7->3 0.39\n" +
            "0 to 4 (0.38): 0->4 0.38\n" +
            "0 to 5 (0.73): 0->4 0.38  4->5 0.35\n" +
            "0 to 6 (1.51): 0->2 0.26  2->7 0.34  7->3 0.39  3->6 0.52\n" +
            "0 to 7 (0.60): 0->2 0.26  2->7 0.34\n";
    }

    private void build() {
        Queue<Integer> nodeQueue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override public int compare(Integer o1, Integer o2) {
                return (int) Math.signum(distTo[o1] - distTo[o2]);
            }
        });
        nodeQueue.add(0);
        while (!nodeQueue.isEmpty()) {
            int node = nodeQueue.remove();
            for (ReversibleWeightedDirectedEdge edge : graph.adj(node)) {
                // if we've never seen this node before, we should explore it at some point
                if (distTo[edge.to] == Double.POSITIVE_INFINITY) nodeQueue.add(edge.to);
                double candyTotal = distTo[edge.from] + edge.weight;
                if (candyTotal < distTo[edge.to]) {
                    distTo[edge.to] = candyTotal;
                    parentLink[edge.to] = edge;
                }
            }
        }
    }

    String getPrintString() {
        StringBuilder sb = new StringBuilder("0 to 0 (0.00):\n");
        for (int i = 1; i < graph.V(); i++) {
            sb.append("0 to " + i + " (" + String.format("%.2f", distTo[i]) + "): ");
            if (distTo[i] < Double.POSITIVE_INFINITY) {
                int curr = i;
                List<ReversibleWeightedDirectedEdge> pathEdges = new ArrayList<>();
                while (parentLink[curr] != null) {
                    pathEdges.add(parentLink[curr]);
                    curr = parentLink[curr].from;
                }
                Collections.reverse(pathEdges);
                pathEdges.forEach(e -> sb.append(e).append("  "));
                sb.setLength(sb.length() - 2);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}

