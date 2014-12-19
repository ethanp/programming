package algorithms.arraysAndSorting.tutorial.countingsort;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/18/14
 */
public class CountingSort2Sort {
    public static void main(String[] args) {
        int[] counts = new int[100];
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        for (int i = 0; i < n; i++)
            counts[in.nextInt()]++;
        for (int i = 0; i < 100; i++) {
            for (int count = 0; count < counts[i]; count++) {
                System.out.print(i+" ");
            }
        }
        System.out.println();
    }
}
