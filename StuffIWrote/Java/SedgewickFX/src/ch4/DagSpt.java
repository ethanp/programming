package ch4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

/**
 * 7/14/16 12:43 PM
 *
 * Linear-time shortest-path-tree algorithm only suitable for directed acyclic graphs. We do NOT
 * require edge weights to be non-negative.
 */
public class DagSpt {
    private final WeightedDirectGraph graph;
    private double[] distTo;

    public DagSpt(WeightedDirectGraph graph) {
        this.graph = graph;
        distTo = new double[graph.V()];
    }

    public static void main(String[] args) {
        givenTest();
    }

    private static void givenTest() {
        WeightedDirectGraph graph = tinyEWDAG();
        DagSpt dagSpt = new DagSpt(graph);
        String shortestPaths = dagSpt.printShortestPathsFrom(5);
        String expectedString = expectedShortestPathsToFive();
    }

    private static WeightedDirectGraph tinyEWDAG() {
        String stringRepr = "" +
            "8\n" +
            "13\n" +
            "5 4 0.35\n" +
            "4 7 0.37\n" +
            "5 7 0.28\n" +
            "5 1 0.32\n" +
            "4 0 0.38\n" +
            "0 2 0.26\n" +
            "3 7 0.39\n" +
            "1 3 0.29\n" +
            "7 2 0.34\n" +
            "6 2 0.40\n" +
            "3 6 0.52\n" +
            "6 0 0.58\n" +
            "6 4 0.93\n";

        Scanner sc = new Scanner(stringRepr);
        int numNodes = sc.nextInt();
        int numEdges = sc.nextInt();
        Collection<ReversibleWeightedDirectedEdge> edges = new ArrayList<>();
        for (int i = 0; i < numEdges; i++)
            edges.add(new ReversibleWeightedDirectedEdge(sc.nextInt(), sc.nextInt(), sc.nextDouble()));

        return new WeightedDirectGraph(numNodes, edges);
    }

    private static String expectedShortestPathsToFive() {
        return "" +
            "5 to 0 (0.73): 5->4 0.35  4->0 0.38\n" +
            "5 to 1 (0.32): 5->1 0.32\n" +
            "5 to 2 (0.62): 5->7 0.28  7->2 0.34\n" +
            "5 to 3 (0.62): 5->1 0.32  1->3 0.29\n" +
            "5 to 4 (0.35): 5->4 0.35\n" +
            "5 to 5 (0.00):\n" +
            "5 to 6 (1.13): 5->1 0.32  1->3 0.29  3->6 0.52\n" +
            "5 to 7 (0.28): 5->7 0.28\n";
    }

    private String printShortestPathsFrom(int node) {
        buildFrom(node);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < graph.V(); i++) {
            sb.append(node + " to " + i + " (" + String.format("%.2f", distTo[i]) + "):");
            if (distTo[i] < Double.POSITIVE_INFINITY) {

            }
        }
        return null;
    }

    private void buildFrom(int node) {
        distTo = new double[graph.V()];
        Arrays.fill(distTo, Double.POSITIVE_INFINITY);
        distTo[node] = 0;
    }
}
