package algorithms.arraysAndSorting.easy;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/22/14
 */
public class SherlockWatson {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int K = in.nextInt();
        int Q = in.nextInt();
        int[] arr = new int[N];
        for (int n = 0; n < N; n++) {
            arr[(n+K)%N] = in.nextInt();
        }
        for (int q = 0; q < Q; q++) {
            System.out.println(arr[in.nextInt()]);
        }
    }
}
