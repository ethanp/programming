package ch1;

import util.Pair;

import java.util.ArrayList;
import java.util.List;

interface UnionFind {
    void union(int a, int b);

    boolean connected(int a, int b);

    int find(int a);

    int count();
}

/**
 * Ethan Petuchowski 2/27/16
 *
 * This is the fast Union-Find data structure described in 1.5, but I'm going to try to implement it
 * before looking at their code.
 */
public class WeightedQuickUnion implements UnionFind {
    public final int numNodes;
    private int count;
    final int id[];
    final int sizes[]; // I think this only needs to be accurate at the roots

    public WeightedQuickUnion(int numNodes) {
        this.numNodes = numNodes;
        this.count = numNodes;
        this.id = new int[numNodes];
        this.sizes = new int[numNodes];
        for (int i = 0; i < numNodes; i++) {
            id[i] = i;
            sizes[i] = 1;
        }
    }

    @Override public void union(int a, int b) {
        int aGrp = find(a);
        int bGrp = find(b);
        if (aGrp == bGrp) return;

        // figure out who's bigger
        // fasten the smaller to the larger
        if (sizes[aGrp] < sizes[bGrp]) {
            id[aGrp] = bGrp;
            sizes[bGrp] += sizes[aGrp];
        } else {
            id[bGrp] = aGrp;
            sizes[aGrp] += sizes[bGrp];
        }
        count--;
    }

    @Override public boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    @Override public int find(int a) {
        int b = a, c;
        while (id[a] != a) a = id[a];
        // path compression
        // (doesn't help speed in practical scenarios)
        while (id[b] != a) {
            c = id[b];
            id[b] = a;
            b = c;
        }
        return a;
    }

    @Override public int count() {
        return count;
    }

    public static void main(String[] args) {
        UnionFind uf = new WeightedQuickUnion(10);
        List<Pair<Integer>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(4, 3));
        pairs.add(new Pair<>(3, 8));
        pairs.add(new Pair<>(6, 5));
        pairs.add(new Pair<>(9, 4));
        pairs.add(new Pair<>(2, 1));
        pairs.add(new Pair<>(8, 9));
        pairs.add(new Pair<>(5, 0));
        pairs.add(new Pair<>(7, 2));
        pairs.add(new Pair<>(6, 1));
        pairs.add(new Pair<>(1, 0));
        pairs.add(new Pair<>(6, 7));
        for (Pair<Integer> pair : pairs) {
            if (!uf.connected(pair.a, pair.b)) {
                System.out.println(pair);
                uf.union(pair.a, pair.b);
            }
        }
        System.out.println(uf.count() == 2 ? "PASSED" : uf.count() + " should be 2");
    }
}

