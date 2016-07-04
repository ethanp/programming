package ch4;

/**
 * 7/4/16 11:59 AM
 */
@SuppressWarnings("WeakerAccess")
public class Search {

    private final boolean[] marks;

    Search(Graph g, int source) {
        marks = new boolean[g.V()];
        viaDFS(g, source);
    }

    public static void main(String[] args) {
        Graph g = new Graph(4);
        g.addEdge(1, 2);
        g.addEdge(0, 1);
        System.out.println(new Search(g, 1).count() == 3);
    }

    private void viaDFS(Graph g, int source) {
        marks[source] = true;
        for (int a : g.adj(source))
            if (!marks[a]) viaDFS(g, a);
    }

    /** is v connected to s? */
    boolean marked(int v) {
        return marks[v];
    }

    /**
     * how many vertices are "connected" to source?
     *
     * note that java can be a bit ugly
     */
    int count() {
        int acc = 0;
        for (boolean mark : marks)
            if (mark) acc++;
        return acc;
    }
}
