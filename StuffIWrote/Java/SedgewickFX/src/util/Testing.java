package util;

/**
 * Ethan Petuchowski 3/6/16
 */
public class Testing {
    public static void shouldEqual(Object a, Object b) {
        if (!a.equals(b)) {
            String msg = String.format("%s should equal %s", a, b);
            throw new RuntimeException(msg);
        }
    }
}
