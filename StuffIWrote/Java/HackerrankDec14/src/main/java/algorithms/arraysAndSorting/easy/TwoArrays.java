package algorithms.arraysAndSorting.easy;

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * Ethan Petuchowski 12/22/14
 */
public class TwoArrays {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        for (int t = 0; t < T; t++) {
            int N = in.nextInt();
            int K = in.nextInt();
            int[] A = new int[N];
            Integer[] B = new Integer[N];
            for (int i = 0; i < N; i++)
                A[i] = in.nextInt();
            for (int i = 0; i < N; i++)
                B[i] = in.nextInt();
            Arrays.sort(A);
            Arrays.sort(B, Collections.reverseOrder());
            boolean passed = true;
            for (int i = 0; i < N; i++) {
                if (A[i]+B[i] < K) {
                    System.out.println("NO");
                    passed = false;
                    break;
                }
            }
            if (passed) {
                System.out.println("YES");
            }
        }
    }
}
