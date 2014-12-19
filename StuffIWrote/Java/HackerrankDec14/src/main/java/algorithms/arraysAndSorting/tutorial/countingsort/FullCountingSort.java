package algorithms.arraysAndSorting.tutorial.countingsort;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/18/14
 */
public class FullCountingSort {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        StringBuilder[] ar = new StringBuilder[100];
        for (int i = 0; i < 100; i++) ar[i] = new StringBuilder();
        for (int i = 0; i < N; i++) {
            int e = in.nextInt();
            String str = in.next();
            if (i < N/2)
                str = "-";
            ar[e].append(str);
            ar[e].append(" ");
        }
        for (StringBuilder s : ar) {
            System.out.print(s.toString());
        }
        System.out.println();
    }
}
