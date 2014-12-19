package algorithms.arraysAndSorting.tutorial.insertionsort;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/16/14
 */
public class InsertionSort1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int s = sc.nextInt();
        int[] ar = new int[s];
        for (int i = 0; i < s; i++) {
            ar[i] = sc.nextInt();
        }
        int toPlace = ar[s-1];
        for (int i = s-2; i >= 0; i--) {
            if (ar[i] > toPlace) {
                ar[i+1] = ar[i];
                printArr(ar);
            } else {
                ar[i+1] = toPlace;
                printArr(ar);
                return;
            }
        }
        ar[0] = toPlace;
        printArr(ar);
    }

    public static void printArr(int[] ar) {
        for (int i = 0; i < ar.length; i++) {
            if (i < ar.length-1) {
                System.out.print(i+" ");
            } else {
                System.out.println(i);
            }
        }
    }
}
