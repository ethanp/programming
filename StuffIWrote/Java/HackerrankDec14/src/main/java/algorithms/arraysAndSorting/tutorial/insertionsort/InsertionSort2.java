package algorithms.arraysAndSorting.tutorial.insertionsort;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/16/14
 */
public class InsertionSort2 {
    public static void insertionSortPart2(int[] ar) {
        // Fill up the code for the required logic here
        // Manipulate the array as required
        for (int last = 1; last < ar.length; last++) {
            int toPlace = ar[last];
            boolean placed = false;
            for (int i = last-1; i >= 0 && !placed; i--) {
                if (ar[i] > toPlace) {
                    ar[i+1] = ar[i];
                } else {
                    ar[i+1] = toPlace;
                    printArray(ar);
                    placed = true;
                }
            }
            if (!placed) {
                ar[0] = toPlace;
                printArray(ar);
            }
        }
    }



    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int s = in.nextInt();
        int[] ar = new int[s];
        for(int i = 0; i < s; i++) {
            ar[i] = in.nextInt();
        }
        insertionSortPart2(ar);

    }
    private static void printArray(int[] ar) {
        for(int n: ar) {
            System.out.print(n+" ");
        }
        System.out.println("");
    }
}
