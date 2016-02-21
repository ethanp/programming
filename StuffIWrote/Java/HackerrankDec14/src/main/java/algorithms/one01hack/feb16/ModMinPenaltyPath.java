package algorithms.one01hack.feb16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Ethan Petuchowski 2/21/16
 *
 * Started at 10:15; not done by 11:00. Ran out of time.
 *
 * It is 5x correct, 1x wrong, and 8x times out. To speed it up, we should not explore paths that
 * won't get us to the end-node.
 */
public class ModMinPenaltyPath {
    static final Map<Integer, List<Edge>> adj = new HashMap<>();
    private final static Scanner sc = new Scanner(System.in);
    static int endNode = 0;

    public static void main(String[] args) {
        // read-in and build undirected graph
        int numNodes = sc.nextInt();
        int numEdges = sc.nextInt();
        for (int edgeIdx = 0; edgeIdx < numEdges; edgeIdx++) {
            int from = sc.nextInt();
            int to = sc.nextInt();
            int cost = sc.nextInt();
            if (!adj.containsKey(from)) adj.put(from, new ArrayList<Edge>());
            if (!adj.containsKey(to)) adj.put(to, new ArrayList<Edge>());
            Edge e = new Edge(Math.min(from, to), Math.max(from, to), cost);
            adj.get(from).add(e);
            adj.get(to).add(e);
        }

        int startNode = sc.nextInt();
        endNode = sc.nextInt();

        // depth-first search?
        int result = dfs(startNode, 0, Integer.MAX_VALUE);
        if (result == Integer.MAX_VALUE) System.out.println(-1);
        else System.out.println(result);
    }

    /**
     * Notes: All costs are positive. Bitwise OR will monotonically increase, so I can't see it
     * being useful to go back down a path we've already tried. We want either MIN COST or NO PATH
     * result.
     */
    /* ----------------------------
     * To fix timeout, we might try
     *
     * 1. TODO not going down paths that can't possibly get us to the end-node
     *
     * 2. DONE not going down paths if `acc` is already greater than a previously-found result
     */
    private static int dfs(int startNode, int acc, int prevMin) {
        int min = prevMin;
        int res;
        for (Edge e : adj.get(startNode)) {
            if (!e.traversed) {
                int otherNode = e.otherNode(startNode);
                int newAcc = acc | e.cost;
                if (otherNode == endNode) res = newAcc;
                else if (newAcc < min) {
                    e.traversed = true;
                    res = dfs(otherNode, newAcc, min);
                    e.traversed = false;
                }
                else res = Integer.MAX_VALUE;
                if (res < min) {
                    min = res;
                }
            }
        }
        return min;
    }


    static class Edge {
        final int lower;
        final int higher;
        final int cost;
        boolean traversed;

        public Edge(int lower, int higher, int cost) {
            this.lower = lower;
            this.higher = higher;
            this.cost = cost;
        }

        int otherNode(int one) {
            return lower == one ? higher : lower;
        }
    }
}
