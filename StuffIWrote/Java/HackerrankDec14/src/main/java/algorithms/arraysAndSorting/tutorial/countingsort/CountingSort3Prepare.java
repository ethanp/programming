package algorithms.arraysAndSorting.tutorial.countingsort;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/18/14
 */
public class CountingSort3Prepare {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int[] ar = new int[100];
        for (int i = 0; i < N; i++) {
            int e = in.nextInt();
            ar[e]++;
            String str = in.next();
        }
        int seen = 0;
        for (int i = 0; i < 100; i++) {
            seen += ar[i];
            System.out.print(seen+" ");
        }
        System.out.println();
    }
}
