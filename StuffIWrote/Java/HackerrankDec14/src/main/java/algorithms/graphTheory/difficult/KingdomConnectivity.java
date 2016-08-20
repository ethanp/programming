package algorithms.graphTheory.difficult;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 8/12/16 3:21 PM
 *
 * How many paths exist from node 1 to node N in DIRECTED graph G?
 *
 * 1) if we can't get to N from 1 => 0
 * 2) I think that if there's a cycle that brings us to any node on a path from 1->N => INFINITY
 * 3) Assuming neither of the above cases (which are non-trivial to verify), I think we can do
 * a DFS and add up for each node, the number of paths from each of its adj nodes that are
 * on paths to N.
 *
 * Apparently this is not a good way to go about it. Better to find the right way online. It was
 * worth a shot, but I'm not there yet I guess.
 */
public class KingdomConnectivity {
    final List<Integer>[] adj;
    final List<Integer>[] revAdj;
    boolean[] reachableFrom1 = new boolean[V()];
    boolean[] reachesN = new boolean[V()];
    boolean[] nodesWeCareAbout = new boolean[V()];
    boolean[] cycleDetection = new boolean[V()];

    public KingdomConnectivity(List<Integer>[] adj) {
        this.adj = adj;
        this.revAdj = buildRevAdj();
        printConnectivity();
    }

    private List<Integer>[] buildRevAdj() {
        List<Integer>[] ret = new List[V()];
        for (int v = 0; v < V(); v++) ret[v] = new ArrayList<>();
        for (int v = 0; v < V(); v++) for (int n : adj[v]) ret[n].add(v);
        return ret;
    }

    public static void main(String[] args) {
        KingdomConnectivity kc = new KingdomConnectivity(readGraph());
    }

    static List<Integer>[] readGraph() {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();
        List<Integer>[] adj = new List[N];
        for (int n = 0; n < N; n++) adj[n] = new ArrayList<>();
        for (int m = 0; m < M; m++) adj[sc.nextInt() - 1].add(sc.nextInt() - 1);
        return adj;
    }

    void printConnectivity() {
        // see observation 1 above
        findReachableFrom1();
        if (!reachableFrom1[V() - 1]) {
            System.out.println(0);
            return;
        }
        findReachesN();
        noteNodesWeCareAbout();
        boolean relevantCycle = lookForCycleInNodesWeCareAbout();
        if (relevantCycle) {
            System.out.println("INFINITE PATHS");
            return;
        }
    }

    /** IIRC a cycle means we're doing a DFS and we come across a node we've seen before? */
    private boolean lookForCycleInNodesWeCareAbout() {
        throw new NotImplementedException();
    }

    /** we only care about nodes that are BOTH reachable from 1 and reach N */
    private void noteNodesWeCareAbout() {
        for (int v = 0; v < V(); v++)
            if (reachableFrom1[v] && reachesN[v])
                nodesWeCareAbout[v] = true;
    }

    private void findReachesN() {
        dfsFromN(V() - 1);
    }

    private void dfsFromN(int u) {
        reachesN[u] = true;
        for (int v : revAdj[u])
            if (!reachesN[v])
                dfsFromN(v);
    }

    private int V() {
        return adj.length;
    }

    /** by 1 I mean 0. */
    void findReachableFrom1() {
        dfsFrom1(0);
    }

    void dfsFrom1(int u) {
        reachableFrom1[u] = true;
        for (int v : adj[u])
            if (!reachableFrom1[v])
                dfsFrom1(v);
    }
}
