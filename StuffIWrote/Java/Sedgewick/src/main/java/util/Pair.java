package util;

/** this is just for the testing purpose */
public class Pair {
    public final int a;
    public final int b;
    public Pair(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override public String toString() {
        return "Pair{"+a+", "+b+'}';
    }
}
