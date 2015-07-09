package algorithms.dynamicPrograming.moderate;

import java.util.Scanner;

/**
 * Ethan Petuchowski 7/4/15
 *
 * I think I can just walk the array backwards with the highest value I've seen so far
 * and sum up the diffs
 */
public class StockMax {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int numT = sc.nextInt();
        for (int t = 0; t < numT; t++) {
            new StockMax();
        }
    }

    StockMax() {
        int numD = sc.nextInt();
        int[] arr = new int[numD];
        for (int i = 0; i < arr.length; i++)
            arr[i] = sc.nextInt();
        int m = arr[arr.length-1];
        long acc = 0;
        for (int i = arr.length-2; i >= 0; i--) {
            m = Math.max(m, arr[i]);
            if (arr[i] < m) acc += m-arr[i];
        }
        System.out.println(acc);
    }
}
