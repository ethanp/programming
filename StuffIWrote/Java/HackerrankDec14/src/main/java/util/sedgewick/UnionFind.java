package util.sedgewick;

/** The code for this data structure belongs to @author Sedgewick */
public class UnionFind {
    private int[] parent;   // parent[i] = parent of i
    private int[] height;   // height[i] = height of subtree rooted at i
    private int count;  // number of components

    public UnionFind(int n) {
        count = n;
        parent = new int[n];
        height = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            height[i] = 0;
        }
    }

    public int count() {
        return count;
    }

    public int find(int p) {
        while (p != parent[p]) p = parent[p];
        return p;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public void union(int p, int q) {
        int i = find(p);
        int j = find(q);
        if (i == j) return;

        // make shorter root point to taller one
        if (height[i] < height[j]) parent[i] = j;
        else if (height[i] > height[j]) parent[j] = i;
        else {
            parent[j] = i;
            height[i]++;
        }
        count--;
    }
}
