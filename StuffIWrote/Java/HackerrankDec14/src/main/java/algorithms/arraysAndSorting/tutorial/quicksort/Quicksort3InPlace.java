package algorithms.arraysAndSorting.tutorial.quicksort;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/18/14
 * Seems I lost my work for part 2...
 */
public class Quicksort3InPlace {
    static void quickSort(int[] ar) {
        partition(ar, 0, ar.length);
    }

    /**
     * @param end is EXCLUSIVE (start is inclusive)
     * @return pivot index
     * */
    static int partition(int[] ar, int start, int end) {
        if (start+1 >= end) return -1;
        int pivotVal = ar[end-1];
        int lesserLen = 0; // number of elements found so far less than the pivot
        for (int i = start; i < end-1; i++) {
            if (ar[i] < pivotVal) {
                swap(ar, i, start+lesserLen++);
            }
        }
        swap(ar, start+lesserLen, end-1);
        printArray(ar);
        partition(ar, start, start+lesserLen);
        partition(ar, start+lesserLen+1, end);
        return start+lesserLen;
    }

    static void swap(int[] ar, int a, int b) {
        int tmp = ar[a];
        ar[a] = ar[b];
        ar[b] = tmp;
    }

    static void printArray(int[] ar) {
        printArray(ar, 0, ar.length);
    }

    static void printArray(int[] ar, int start, int end) {
        for (int i = start; i < end; i++) {
            System.out.print(ar[i]+" ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] ar = new int[n];
        for (int i = 0; i < n; i++) {
            ar[i] = in.nextInt();
        }
        quickSort(ar);
    }
}
