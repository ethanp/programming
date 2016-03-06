package util;

/** this is just for the testing purpose */
public class Pair<T> {
    public final T a;
    public final T b;
    public Pair(T a, T b) {
        this.a = a;
        this.b = b;
    }

    @Override public String toString() {
        return "Pair{"+a+", "+b+'}';
    }

    public T getB() {
        return b;
    }
}
