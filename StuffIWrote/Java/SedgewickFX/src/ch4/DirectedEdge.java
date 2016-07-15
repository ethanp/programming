package ch4;

/**
 * 7/15/16 10:26 AM
 */
class DirectedEdge {
    final int from;
    final int to;

    public DirectedEdge(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override public String toString() {
        return from + "->" + to;
    }


}
