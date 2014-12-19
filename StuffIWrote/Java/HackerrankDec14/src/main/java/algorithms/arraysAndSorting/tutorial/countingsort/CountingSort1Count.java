package algorithms.arraysAndSorting.tutorial.countingsort;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/18/14
 */
public class CountingSort1Count {
    public static void main(String[] args) {
        int[] counts = new int[100];
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        for (int i = 0; i < n; i++)
            counts[in.nextInt()]++;
        for (int count : counts)
            System.out.print(count + " ");
        System.out.println();
    }
}
