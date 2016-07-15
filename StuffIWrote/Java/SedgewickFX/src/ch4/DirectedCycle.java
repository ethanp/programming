package ch4;

import java.util.Stack;

/**
 * 7/15/16 11:49 AM
 *
 * TODO test this
 */
class DirectedCycle {
    final DirectGraph graph;
    final boolean[] seen;
    final int[] gotHereFrom;
    final boolean[] onStack;
    Stack<Integer> cycle;

    public DirectedCycle(DirectGraph graph) {
        this.graph = graph;
        this.seen = new boolean[graph.V()];
        this.onStack = new boolean[graph.V()];
        this.gotHereFrom = new int[graph.V()];
        for (int v = 0; v < graph.V(); v++) {
            if (!this.seen[v]) dfs(v);
        }
    }

    private void dfs(int v) {
        seen[v] = true;
        onStack[v] = true;
        for (DirectedEdge e : graph.adj(v)) {
            int w = e.to;
            if (hasCycle()) {
                return;  // we're already done, and don't need to keep looking
            } else if (!seen[w]) {
                gotHereFrom[w] = v;
                dfs(w);
            } else if (onStack[w]) {
                // uh-oh: found a cycle
                cycle = new Stack<>();
                cycle.push(w);  // the first and last node are w
                cycle.push(v);
                for (int x = gotHereFrom[v]; x != w; x = gotHereFrom[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                return;
            } else {
                // it has been seen, but it's not on the stack,
                // so we already know there are no cycles originating out of here
                // or we'd've already seen one
                continue;
            }
        }
        onStack[v] = false;
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public Stack<Integer> getCycle() {
        return cycle;
    }
}
