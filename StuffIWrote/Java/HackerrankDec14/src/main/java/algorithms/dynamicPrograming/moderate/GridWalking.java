package algorithms.dynamicPrograming.moderate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Ethan Petuchowski 5/1/16
 *
 * This brute-force solution is way too memory intensive, and does not pass.
 *
 * The solution requires a significant amount more thought and can be found here
 *
 * http://cs.stackexchange.com/questions/4941/dynamic-programming-with-large-number-of-subproblems
 */
public class GridWalking {
    static int[] X;
    static int[] D;
    static Map<GridLoc, Long> memo;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        for (int t = 0; t < T; t++) {
            memo = new HashMap<>();
            int N = sc.nextInt();
            int M = sc.nextInt();
            X = new int[N];
            D = new int[N];
            for (int n = 0; n < N; n++) X[n] = sc.nextInt();
            for (int n = 0; n < N; n++) D[n] = sc.nextInt();
            long res = count(M);
            System.out.println(res);
        }
    }
    static long count(int movesRemaining) {
        // verify implementation
        assert movesRemaining >= 0;

        // check if we're done searching
        if (movesRemaining == 0) return 1;

        // note: clone() is shallow, but that's fine for primitives
        GridLoc cur = new GridLoc(X.clone(), movesRemaining);

        // check if we've been here before
        if (memo.containsKey(cur)) return memo.get(cur);

        // go to all neighbors
        long c = 0;
        for (int i = 0; i < X.length; i++) {

            // try down
            X[i]--; if (X[i] > 0) c += count(movesRemaining-1);

            // try up
            X[i] += 2; if (X[i] <= D[i]) c += count(movesRemaining-1);

            // reset
            X[i]--;
        }
        if (c > 1000000007) c %= 1000000007;
        memo.put(cur, c);
        return c;
    }
    static class GridLoc {
        int[] loc;
        int movesLeft;

        GridLoc(int[] loc, int movesLeft) {
            this.loc = loc;
            this.movesLeft = movesLeft;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GridLoc loc1 = (GridLoc) o;
            return movesLeft == loc1.movesLeft
                && Arrays.equals(loc, loc1.loc);
        }

        @Override public int hashCode() {
            int result = Arrays.hashCode(loc);
            result = 31*result+movesLeft;
            return result;
        }
    }
}
