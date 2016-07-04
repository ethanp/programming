package ch4;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 7/4/16 11:40 AM
 *
 * this class is mainly "by the book"
 */
@SuppressWarnings("WeakerAccess")
public class Graph {

    private List<Integer>[] adjList;

    /** create a V-vertex graph with no edges */
    public Graph(int V) {
        adjList = (List<Integer>[]) new List[V];
        for (int i = 0; i < V; i++) {
            adjList[i] = new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        Graph g = new Graph(4);
        g.addEdge(1, 2);
        g.addEdge(0, 1);
        g.addEdge(1, 3);
        System.out.println(g);
    }

    /** number of vertices */
    public int V() {
        return adjList.length;
    }

    /** number of edges */
    public int E() {
        return Arrays
            .stream(adjList)
            .mapToInt(List::size)
            .sum()/2;
    }

    /**
     * add edge v-w to this graph
     *
     * this implementation makes it so that self-loops are added twice :/
     */
    public void addEdge(int v, int w) {
        adjList[v].add(w);
        adjList[w].add(v);
    }

    /** vertices adjacent to v */
    public Iterable<Integer> adj(int v) {
        return new ArrayList<>(adjList[v]);
    }

    /** string representation */
    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("V = " + V() + ", E = " + E()).append('\n');
        for (int i = 0; i < V(); i++) {
            sb.append(i+":");
            for (int nbr : adj(i)) {
                sb.append(" " + nbr);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
