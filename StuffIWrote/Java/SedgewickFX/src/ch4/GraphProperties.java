package ch4;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 7/9/16 2:16 PM
 */
public class GraphProperties {
    private final Graph G;

    GraphProperties(Graph G) {
        this.G = G;
    }

    public static void main(String[] args) {
        Graph g = new Graph(4);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        GraphProperties gp = new GraphProperties(g);
        System.out.println("diameter: " + gp.diameter());
        System.out.println("radius:   " + gp.radius());
        System.out.println("center:   " + gp.center());
    }

    /** the length of the shortest path from that vertex to the furthest vertex from v */
    int eccentricity(int v) {
        int currDist = 0;
        boolean[] marked = new boolean[G.V()];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(v);
        queue.add(-1);
        while (true) {
            int curr = queue.poll();
            if (curr == -1) {
                if (queue.isEmpty())
                    return currDist;
                queue.add(-1);
                currDist++;
                continue;
            }
            for (int nbr : G.adj(curr)) {
                if (!marked[nbr]) {
                    marked[nbr] = true;
                    queue.add(nbr);
                }
            }
        }
    }

    /** maximum eccentricity of any vertex */
    int diameter() {
        int max = 0;
        for (int v = 0; v < G.V(); v++)
            max = Math.max(max, eccentricity(v));
        return max;
    }

    /** smallest eccentricity of any vertex */
    int radius() {
        int min = G.V();
        for (int v = 0; v < G.V(); v++)
            min = Math.min(min, eccentricity(v));
        return min;
    }

    /** id of a vertex whose eccentricity is the radius */
    int center() {
        int radius = radius();
        for (int v = 0; v < G.V(); v++)
            if (eccentricity(v) == radius)
                return v;
        throw new RuntimeException("hmm.");
    }
}
