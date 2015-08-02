package algorithms.graphTheory.difficult;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Ethan Petuchowski 8/1/15
 */
public class DijkstrasReach {

    static Scanner sc = new Scanner(System.in);

    static class Edge {
        final int to; final int dist;
        public Edge(int to, int dist) { this.to = to; this.dist = dist; }
        @Override public String toString() { return "Edge{"+"to="+to+", dist="+dist+'}'; }
    }
    static class Node {
        final int id; int dist; boolean visited;
        public Node(int id, int dist) { this.id = id; this.dist = dist; }
        public Node(int id) { this(id, Integer.MAX_VALUE); }
        @Override public String toString() { return "Node{"+"id="+id+", dist="+dist+'}'; }
    }

    public static void main(String[] args) {
        int T = sc.nextInt();
        for (int t = 0; t < T; t++) {
            int N = sc.nextInt();
            int M = sc.nextInt();
            Node[] nodes = new Node[N];
            @SuppressWarnings("unchecked")
            List<Edge>[] adjs = new List[N];
            for (int n = 0; n < N; n++) {
                adjs[n] = new ArrayList<>();
                nodes[n] = new Node(n);
            }
            for (int m = 0; m < M; m++) {
                int u = sc.nextInt()-1;
                int v = sc.nextInt()-1;
                int d = sc.nextInt();
                adjs[u].add(new Edge(v, d));
                adjs[v].add(new Edge(u, d));
            }
            int S = sc.nextInt()-1;
            nodes[S].dist = 0;
            Node cur = nodes[S];
            while (cur != null) {
                cur.visited = true;
                for(Edge edge : adjs[cur.id]) {
                    Node dest = nodes[edge.to];
                    dest.dist = Math.min(edge.dist+cur.dist, dest.dist);
                }
                // I originally thought that a priority-queue would take care of this, but
                // the problem is that the priority-queue doesn't rejigger itself when the
                // distances change. Now it's O(N^2) though. Seems like the easiest way to
                // get this back to O(NlogN) is to q.remove(node), q.add(node) whenever the
                // distance to a node is changed, so that the heap is forced to rejigger
                // itself.
                int min = Integer.MAX_VALUE;
                cur = null;
                for (Node n : nodes) {
                    if (!n.visited && n.dist < min) {
                        cur = n;
                        min = n.dist;
                    }
                }
            }
            for (Node n : nodes)
                if (n.dist != 0)
                    System.out.printf("%d ", n.dist == Integer.MAX_VALUE ? -1 : n.dist);
            System.out.println();
        }
    }
}
