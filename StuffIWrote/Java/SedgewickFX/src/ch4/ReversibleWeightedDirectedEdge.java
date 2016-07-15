package ch4;

/**
 * 7/10/16 10:53 PM
 */
class ReversibleWeightedDirectedEdge extends DirectedEdge {
    final double weight;
    final boolean isReversed;

    private ReversibleWeightedDirectedEdge(int from, int to, double weight, boolean isReversed) {
        super(from, to);
        this.weight = weight;
        this.isReversed = isReversed;
    }

    public ReversibleWeightedDirectedEdge(int from, int to, double weight) {
        this(from, to, weight, false);
    }

    @Override public String toString() {
        String fromTo = isReversed ? (to + "->" + from) : (from + "->" + to);
        return String.format(fromTo + " %.2f", weight);
    }

    public ReversibleWeightedDirectedEdge reverse() {
        return new ReversibleWeightedDirectedEdge(to, from, weight, !isReversed);
    }
}
