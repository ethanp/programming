package algorithms.dynamicPrograming.clrs;

import java.util.Arrays;

/**
 * Ethan Petuchowski 4/23/16
 *
 * This is the first example of The Rod Cutting Problem in CLRS.
 */
public class RodCutting {

    private static final int[] prices = {0, 1, 5, 8, 9, 10, 17, 17, 20, 24, 30};
    private static final int[] trueRev = {0, 1, 5, 8, 10, 13, 17, 18, 22, 25, 30};
    private static final int[] calcdRev = new int[11];

    public static void main(String[] args) {
        for (int i = 0; i < trueRev.length; i++) {
            int rev = prices[i];
            for (int j = 0; j < i; j++)
                rev = Math.max(rev, prices[j]+calcdRev[i-j]);
            calcdRev[i] = rev;
            System.out.printf("%d=%d\n", i, rev);
        }
        assert Arrays.equals(trueRev, calcdRev);
    }
}
