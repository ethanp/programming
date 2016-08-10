package algorithms.graphTheory.medium;

import algorithms.graphTheory.WeightedEdge;
import util.sedgewick.UnionFind;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/**
 * 8/10/16 12:29 PM
 */
public class KruskalMST {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numNodes = sc.nextInt();
        UnionFind uf = new UnionFind(numNodes);
        List<WeightedEdge>[] adj = new List[numNodes];
        int numEdges = sc.nextInt();
        for (int i = 0; i < numNodes; i++) adj[i] = new ArrayList<>();
        Queue<WeightedEdge> q = new PriorityQueue<>();
        for (int i = 0; i < numEdges; i++) {
            int u = sc.nextInt() - 1;
            int v = sc.nextInt() - 1;
            int w = sc.nextInt();
            q.add(new WeightedEdge(u, v, w));
        }
        int startNode = sc.nextInt() - 1;
        int acc = 0;
        while (!q.isEmpty() && uf.count() > 1) {
            WeightedEdge next = q.poll();
            if (!uf.connected(next.u, next.v)) {
                uf.union(next.u, next.v);
                acc += next.weight;
            }
        }
        System.out.println(acc);
    }

}
