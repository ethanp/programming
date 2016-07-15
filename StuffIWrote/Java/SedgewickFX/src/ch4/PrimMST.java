package ch4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/**
 * 7/10/16 9:21 PM
 */
public class PrimMST {
    private final WeightedIndirectGraph graph;

    public PrimMST(WeightedIndirectGraph graph) {
        this.graph = graph;
    }

    public static void main(String[] args) {
        Collection<ReversibleWeightedDirectedEdge> edges = new ArrayList<>();
        String tinyEWGtxt = "" +
            "8\n" +
            "16\n" +
            "4 5 0.35\n" +
            "4 7 0.37\n" +
            "5 7 0.28\n" +
            "0 7 0.16\n" +
            "1 5 0.32\n" +
            "0 4 0.38\n" +
            "2 3 0.17\n" +
            "1 7 0.19\n" +
            "0 2 0.26\n" +
            "1 2 0.36\n" +
            "1 3 0.29\n" +
            "2 7 0.34\n" +
            "6 2 0.40\n" +
            "3 6 0.52\n" +
            "6 0 0.58\n" +
            "6 4 0.93";

        Scanner rdr = new Scanner(tinyEWGtxt);
        int numNodes = rdr.nextInt();
        int numEdges = rdr.nextInt();
        for (int i = 0; i < numEdges; i++)
            edges.add(new ReversibleWeightedDirectedEdge(rdr.nextInt(), rdr.nextInt(), rdr.nextDouble()));

        WeightedIndirectGraph g = new WeightedIndirectGraph(numNodes, edges);
        PrimMST primMST = new PrimMST(g);
        String actualOutput = primMST.getPrintString();

        String expectedOutput = "" +
            "0-7 0.16\n" +
            "1-7 0.19\n" +
            "0-2 0.26\n" +
            "2-3 0.17\n" +
            "5-7 0.28\n" +
            "4-5 0.35\n" +
            "6-2 0.40\n" +
            "1.81";

        System.out.println(actualOutput);
        System.out.println(expectedOutput.equals(actualOutput));
    }

    String getPrintString() {
        StringBuilder sb = new StringBuilder();
        Collection<ReversibleWeightedDirectedEdge> mst = build();
        double totalWeight = 0;
        for (ReversibleWeightedDirectedEdge edge : mst) {
            sb.append(edge.toString().replace(">", "")).append('\n');
            totalWeight += edge.weight;
        }
        String weightString = String.format("%.2f", totalWeight);
        sb.append(weightString);
        return sb.toString();
    }

    public Collection<ReversibleWeightedDirectedEdge> build() {
        Forest remainingNodes = new Forest(graph.V());
        Collection<ReversibleWeightedDirectedEdge> finalTree = new ArrayList<>();
        Queue<ReversibleWeightedDirectedEdge> minEdges = new PriorityQueue<>(
            (a, b) -> (int) Math.signum(a.weight - b.weight)
        );
        graph.adj(0).forEach(minEdges::add);
        remainingNodes.mark(0);
        while (remainingNodes.hasNext()) {
            ReversibleWeightedDirectedEdge next = minEdges.poll();
            if (remainingNodes.mark(next.to)) {
                finalTree.add(next);
                graph.adj(next.to).forEach(minEdges::add);
            }
        }
        return finalTree;
    }
}

