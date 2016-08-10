package algorithms.graphTheory.medium;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/**
 * 8/10/16 10:47 AM
 */
public class PrimsMST {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numNodes = sc.nextInt();
        boolean[] seen = new boolean[numNodes];
        int numEdges = sc.nextInt();
        List<WeightedEdge>[] adj = new List[numNodes];
        for (int i = 0; i < adj.length; i++)
            adj[i] = new ArrayList<>();
        for (int i = 0; i < numEdges; i++) {
            int u = sc.nextInt()-1;
            int v = sc.nextInt()-1;
            int w = sc.nextInt();
            adj[u].add(new WeightedEdge(u, v, w));
            adj[v].add(new WeightedEdge(v, u, w));
        }
        int startNode = sc.nextInt()-1;
        seen[startNode] = true;
        Queue<WeightedEdge> q = new PriorityQueue<>();
        for (int i = 0; i < adj[startNode].size(); i++) {
            q.add(adj[startNode].get(i));
        }
        int acc = 0;
        while (!q.isEmpty()) {
            WeightedEdge next = q.poll();
            if (!seen[next.v]) {
                acc += next.weight;
                seen[next.v] = true;
                for (int i = 0; i < adj[next.v].size(); i++) {
                    q.add(adj[next.v].get(i));
                }
            }
        }
        System.out.println(acc);
    }

}
