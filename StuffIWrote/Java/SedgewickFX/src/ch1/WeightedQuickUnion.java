package ch1;

import util.Pair;

import java.util.ArrayList;
import java.util.List;

interface UnionFind {
    void union(int a, int b);
    boolean areInSameSubset(int a, int b);
    int findIndexOfRootOfSubsetContaining(int a);
    int count();
}

/**
 * Ethan Petuchowski 2/27/16
 *
 * This is the fast Union-Find data structure described in 1.5, but I'm going to try to implement it
 * before looking at their code.
 */
public class WeightedQuickUnion implements UnionFind {
    private int currentSubsetCount;
    private final int parentID[];
    private final int subsetSize[]; // I think this only needs to be accurate at the roots

    public WeightedQuickUnion(int numNodes) {
        this.currentSubsetCount = numNodes;
        this.parentID = new int[numNodes];
        this.subsetSize = new int[numNodes];
        for (int i = 0; i < numNodes; i++) {
            parentID[i] = i;
            subsetSize[i] = 1;
        }
    }

    @Override public void union(int a, int b) {
        int aGrp = findIndexOfRootOfSubsetContaining(a);
        int bGrp = findIndexOfRootOfSubsetContaining(b);
        if (aGrp == bGrp) return;
        attachSmallerSubsetTolarger(aGrp, bGrp);
        currentSubsetCount--;
    }
    
    private void attachSmallerSubsetTolarger(int group1, int group2) {
        int smallerSubsetRoot = subsetSize[group1] < subsetSize[group2] ? group1 : group2;
        int largerSubsetRoot = group1 == smallerSubsetRoot ? group2 : group1;
        parentID[smallerSubsetRoot] = largerSubsetRoot;
        subsetSize[largerSubsetRoot] += subsetSize[smallerSubsetRoot];
    }

    @Override public boolean areInSameSubset(int idxA, int idxB) {
        return findIndexOfRootOfSubsetContaining(idxA)
            == findIndexOfRootOfSubsetContaining(idxB);
    }

    @Override public int findIndexOfRootOfSubsetContaining(int givenIdx) {
        int rootIdx = getRootFor(givenIdx);
        compressPathToRoot(givenIdx, rootIdx);
        return rootIdx;
    }

    /** path compression (note: doesn't actually help speed in practical scenarios [why?]) */
    private void compressPathToRoot(int startIdx, int rootIdx) {
        int curIdx = startIdx;
        int parentIdx;
        while (parentID[curIdx] != rootIdx) {
            parentIdx = parentID[curIdx];
            parentID[curIdx] = rootIdx;
            curIdx = parentIdx;
        }
    }

    private int getRootFor(int givenIdx) {
        while (hasParent(givenIdx))
            givenIdx = parentID[givenIdx];
        return givenIdx;
    }

    private boolean hasParent(int a) {
        return parentID[a] != a;
    }

    @Override public int count() {
        return currentSubsetCount;
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
            if (!uf.areInSameSubset(pair.a, pair.b)) {
                System.out.println(pair);
                uf.union(pair.a, pair.b);
            }
        }
        System.out.println(uf.count() == 2 ? "PASSED" : uf.count() + " should be 2");
    }
}

