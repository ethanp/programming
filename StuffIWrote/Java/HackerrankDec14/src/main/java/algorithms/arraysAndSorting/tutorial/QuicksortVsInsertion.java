package algorithms.arraysAndSorting.tutorial;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Ethan Petuchowski 12/18/14
 */
public class QuicksortVsInsertion {

    static int quicksortSwaps = 0;
    static int insertionsortSwaps = 0;

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
                quicksortSwap(ar, i, start+lesserLen++);
            }
        }
        quicksortSwap(ar, start+lesserLen, end-1);
        partition(ar, start, start+lesserLen);
        partition(ar, start+lesserLen+1, end);
        return start+lesserLen;
    }

    static void quicksortSwap(int[] ar, int a, int b) {
        quicksortSwaps++;
        if (a == b) return;
        swap(ar, a, b);
    }

    static void swap(int[] ar, int a, int b) {
        int tmp = ar[a];
        ar[a] = ar[b];
        ar[b] = tmp;
    }

    static void insertionsortSwap(int [] ar, int a, int b) {
        swap(ar, a, b);
        ++insertionsortSwaps;
//        System.out.println("iSwap: "+ insertionSwaps);
    }

    static void quicksortPrint(int[] ar) {
        System.out.println("Quicksort: ");
        printArray(ar);
    }

    static void insertionPrint(int[] ar) {
        System.out.print("Insertion: ");
        printArray(ar);
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
        int[] ar2 = Arrays.copyOf(ar, ar.length);
        quickSort(ar);
        insertionSort(ar2);
        System.out.println(insertionsortSwaps - quicksortSwaps);
    }

    /** iterate Forwards, and for each item, swap it back until it's >= */
    static void insertionSort(int[] ar) {
        for (int loc = 1; loc < ar.length; loc++) {
            int tmpLoc = loc;
            while (tmpLoc > 0 && ar[tmpLoc] < ar[tmpLoc-1]) {
                insertionsortSwap(ar, tmpLoc, tmpLoc-1);
                tmpLoc--;
            }
        }
    }
}
