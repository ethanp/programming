package util;

/** this is just for the testing purpose */
public class Pair<T> {
    public final T first;
    public final T second;

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    @Override public String toString() {
        return "Pair{"+first+", "+second+'}';
    }

    public T getSecond() {
        return second;
    }
}
