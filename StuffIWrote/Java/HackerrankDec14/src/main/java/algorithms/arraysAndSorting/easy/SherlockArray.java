package algorithms.arraysAndSorting.easy;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/22/14
 */
public class SherlockArray {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        for (int t = 0; t < T; t++) {
            int N = in.nextInt();
            int[] arr = new int[N];
            for (int i = 0; i < N; i++) {
                arr[i] = in.nextInt();
            }
            if (N == 1) {
                System.out.println("YES");
                continue;
            }
            int[] leftSums = new int[N];
            int[] rightSums = new int[N];
            leftSums[0] = arr[0];
            rightSums[N-1] = arr[N-1];
            for (int i = 1; i < N; i++) {
                leftSums[i] = leftSums[i-1] + arr[i];
            }
            boolean found = false;
            for (int i = N-2; i >= 0; i--) {
                rightSums[i] = rightSums[i+1] + arr[i];
                if (rightSums[i] == leftSums[i]) {
                    System.out.println("YES");
                    found = true;
                    break;
                }
            }
            if (!found) {
                if (rightSums[0] == 0 || leftSums[N-1] == 0)
                    System.out.println("YES");
                else
                    System.out.println("NO");
            }
        }
    }
}
