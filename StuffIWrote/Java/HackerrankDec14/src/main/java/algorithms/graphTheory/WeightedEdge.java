package algorithms.graphTheory;

/**
 * 8/10/16 11:09 AM
 */
public class WeightedEdge implements Comparable<WeightedEdge> {
    public final int u;
    public final int v;
    public final int weight;

    public WeightedEdge(int u, int v, int weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    @Override public int compareTo(WeightedEdge o) {
        return weight - o.weight;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeightedEdge edge = (WeightedEdge) o;
        return u == edge.u
            && v == edge.v
            && weight == edge.weight;
    }

    @Override public int hashCode() {
        int result = u;
        result = 31*result + v;
        result = 31*result + weight;
        return result;
    }
}
