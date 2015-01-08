package algorithms.dynamicPrograming.easy;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Ethan Petuchowski 1/7/15
 *
 * Find the sum of that multiset composed of the given values
 *      closest to the given value "k".
 * Note that one value may be added to the multiset multiple times.
 */
public class MultiKnapsack {

    static class Table {
        int[][] diffs;
        Table(int k, int n) {
            diffs = new int[k+1][n+1];
            Arrays.fill(diffs[0], 0);
            for (int i = 0; i < K+1; i++)
                diffs[i][0] = k;
        }
        int get(int k, int nIdx) {
            return k < 0 || nIdx < -1
                   ? -1
                   : diffs[k][nIdx+1];
        }
        void set(int k, int nIdx, int diff) {
            if (k >= 0 && nIdx >= 0)
                diffs[k][nIdx+1] = diff;
        }
    }
    static Table table;
    static int N;
    static int K;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        for (int t = 0; t < T; t++) {
            N = in.nextInt();
            K = in.nextInt();
            table = new Table(K, N);
            for (int n = 0; n < N; n++) {
                int val = in.nextInt();
                for (int k = 1; k < K+1; k++) {

                    int leftDiff = table.get(k-1, n)+1;
                    if (leftDiff >= val)
                        leftDiff -= val;

                    int upGet = table.get(k-val, n-1);
                    int upLeft = upGet == -1 ? k%val : upGet+val;
                    if (upLeft >= val) {
                        upLeft -= val;
                    }
                    int upUp = table.get(k, n-1);
                    int upDiff = Math.min(upLeft, upUp);
                    int diff = Math.min(leftDiff, upDiff);
                    table.set(k, n, diff);
                }
            }
            System.out.println(K-table.get(K, N-1));
        }
    }
}
