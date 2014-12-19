package algorithms.arraysAndSorting.tutorial.quicksort;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/16/14
 */
public class Quicksort1Partition {
    static void partition(int[] ar) {

        int[] larger = new int[ar.length];
        int lgrSize = 0;
        int putLoc = 0;
        int p = ar[0];

        for (int i = 1; i < ar.length; i++) {
            if (ar[i] > p) {
                larger[lgrSize++] = ar[i];
            } else {
                ar[putLoc++] = ar[i];
            }
        }
        ar[putLoc++] = p;
        for (int i = 0; i < lgrSize; i++) {
            ar[putLoc++] = larger[i];
        }
        printArray(ar);
    }

    static void printArray(int[] ar) {
        for (int n : ar) {
            System.out.print(n+" ");
        }
        System.out.println("");
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] ar = new int[n];
        for (int i = 0; i < n; i++) {
            ar[i] = in.nextInt();
        }
        partition(ar);
    }
}
