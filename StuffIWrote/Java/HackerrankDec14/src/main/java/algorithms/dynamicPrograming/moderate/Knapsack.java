package algorithms.dynamicPrograming.moderate;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Ethan Petuchowski 5/1/16
 */
public class Knapsack {
    static int[] A;
    static int k;
    static int[] memo;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        for (int t = 0; t < T; t++) {
            int n = sc.nextInt();
            k = sc.nextInt();
            A = new int[n];
            memo = new int[k];
            Arrays.fill(memo, -1);
            for (int i = 0; i < n; i++)
                A[i] = sc.nextInt();
            int max = knp(0);
            System.out.println(max);
        }
    }
    
    static int knp(int sumSoFar) {
        if (sumSoFar > k) return -1;
        if (sumSoFar == k) return k;
        // check if we already "got the memo"
        if (memo[sumSoFar] > -1)
            return memo[sumSoFar];

        int best = sumSoFar;
        // consider each element in turn
        for (int a : A) {
            int take = knp(sumSoFar+a);
            best = Math.max(best, take);
        }
        memo[sumSoFar] = best;
        return best;
    }
}
