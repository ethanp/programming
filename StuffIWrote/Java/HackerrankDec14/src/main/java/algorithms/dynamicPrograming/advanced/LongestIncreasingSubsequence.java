package algorithms.dynamicPrograming.advanced;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * Ethan Petuchowski 5/4/16
 *
 * The NlogN algorithm below "formDag()" may be the toughest algo I have coded in about a year.
 * I do "get it" at this point but I "got a lot of help"...
 */
public class LongestIncreasingSubsequence {
    static int[] a;
    static Map<Two, Integer> memo;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        a = new int[N];
        memo = new HashMap<>();
        for (int i = 0; i < N; i++) a[i] = sc.nextInt();

        // this one is too slow: N^2
        // System.out.println(topDownDP(0, 0));

        // this is the NlogN one
        System.out.println(formDAG());
    }

    static int formDAG() {

        /**
         * Another Array of values of a[].
         *
         * As we traverse through a[], we build this array up in such a way that element j always
         * holds the lowest value in a[] seen holding the last index of any subsequence of length
         * j seen in the array so far.
         *
         * That's the crucial element of this algorithm.
         *
         * I can't think of a good name for it, so I'll just call it "sub".
         */
        int[] sub = new int[a.length];
        sub[0] = a[0];

        // note: the first element automatically counts as one
        int longestSeenSubsequenceLengthSoFar = 1;

        for (int i = 1; i < a.length; i++) {

            if (a[i] <= sub[0]) {
                sub[0] = a[i];
            }
            else if (a[i] > sub[longestSeenSubsequenceLengthSoFar-1]) {
                sub[longestSeenSubsequenceLengthSoFar++] = a[i];
            }
            else {
                /* This is a ceiling version of binary search
                 *
                 * as trials, try using
                 *
                 * 7 1 2 3 4 1 2 3  => 4
                 *
                 * 7 5 4 5 8 3 6 7 => 4
                 *
                 * 8 0 0 1 2 0 2 2 1 => 3
                 *
                 * 7 1 2 3 4 1 2 3
                 *
                 * The crucial thing ended up being the debug method of actually looking at the
                 * array sub[].
                 *
                 * For reference, see
                 * http://www.geeksforgeeks.org/longest-monotonically-increasing-subsequence-size-n-log-n
                 */
                int lo = 0;
                int hi = longestSeenSubsequenceLengthSoFar-1;
                int mid = 0;
                // when there are only two options left, we must have found the one too low
                // and the one too high, so we know the high one is the "ceiling"
                while (hi - lo > 1) {

                    mid = (lo+hi)/2;

                    // maybe e is the ceiling?
                    // so we don't move to hi = mid -1, we use hi = mid
                    if (a[i] < sub[mid])
                        hi = mid;

                    // again, we're intentionally leaving a gap
                    else if (a[i] > sub[mid])
                        lo = mid;
                    else {
                        hi = mid;
                        break;
                    }
                }

                sub[hi] = a[i];
            }
        }

        return longestSeenSubsequenceLengthSoFar;
    }

    /** This was my first answer. I didn't need help with it, but it didn't pass bc it is N^2. */
    static int topDownDP(int idx, int prev) {
        if (idx == a.length) return 0;

        Two local = new Two(idx, prev);
        if (memo.containsKey(local)) return memo.get(local);

        int result;
        if (a[idx] > prev)
            result = Math.max(
                topDownDP(idx+1, prev),
                1+topDownDP(idx+1, a[idx]));

        else result = topDownDP(idx+1, prev);
        memo.put(local, result);
        return result;
    }

    static class Two {
        final int a;
        final int b;

        Two(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Two two = (Two) o;
            return a == two.a
                && b == two.b;

        }

        @Override public int hashCode() {
            return 31*a+b;
        }
    }
}
