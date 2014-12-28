package algorithms.arraysAndSorting.moderate;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Ethan Petuchowski 12/25/14
 */
public class AlmostSorted {
    static int[] arr;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        arr = new int[N];

        for (int i = 0; i < N; i++) {
            arr[i] = in.nextInt();
        }

        if (isSorted(arr)) {
            System.out.println("yes");
            System.exit(0);
        }

        lookForSwap();      // exits if it works
        lookForReverse();   // exits if it works
        System.out.println("no");
    }

    static void lookForSwap() {


        int N = arr.length;
        /* if one or two elements are out of place, we can swap */
        ArrayList<Integer> idcsOutOfPlace = new ArrayList<>();
        for (int i = 1; i < N; i++) {
            if (arr[i] < arr[i-1]) {
                idcsOutOfPlace.add(i);
            }
        }

        // swap won't be enough to fix more than 2 out-of-place
        if (idcsOutOfPlace.size() > 2) {
            return;
        }

        if (idcsOutOfPlace.size() == 1) {
            // Only possible when N == 2 ? NO e.g. 3 5<>4.

            int valIdx = idcsOutOfPlace.get(0);
            /* look for place to swap into */
            for (int idx = 0; idx < N; idx++) {
                if (swapWorks(idx, valIdx)) {
                    int smaller = Math.min(idx, valIdx)+1;
                    int bigger = Math.max(idx, valIdx)+1;
                    System.out.printf("yes\nswap %d %d\n", smaller, bigger);
                    System.exit(0);
                }
            }
            return;
        }

        /* so #out-of-place == 2 then. (e.g. 1 9 8 4 => swap 2 4) */
        int firstIdx = idcsOutOfPlace.get(0)-1;
        int scndIdx = idcsOutOfPlace.get(1);
        if (swapWorks(firstIdx, scndIdx)) {
            System.out.printf("yes\nswap %d %d\n", firstIdx+1, scndIdx+1);
            System.exit(0);
        }
    }

    static boolean swapWorks(int aIdx, int bIdx) {
        swap(aIdx, bIdx);

        if (isSorted(arr))
            return true;

        swap(aIdx, bIdx);
        return false;
    }

    static void swap(int aIdx, int bIdx) {
        int tmp = arr[aIdx];
        arr[aIdx] = arr[bIdx];
        arr[bIdx] = tmp;
    }

    static void lookForReverse() {
        int N = arr.length;
        int bkwdStrt = -1;
        int bkwdEnd = -1;
        for (int i = 1; i < N; i++) {
            if (arr[i] < arr[i-1]) {
                if (bkwdStrt == -1) {
                    bkwdStrt = i;
                }
                else if (bkwdEnd > -1) {
                    System.out.println("no");
                    System.exit(0);
                }
            }
            else {
                if (bkwdStrt > -1 && bkwdEnd == -1) {
                    bkwdEnd = i;
                }
            }
        }
        if (bkwdEnd-bkwdStrt > 1) {
            System.out.printf("yes\nreverse %d %d\n", bkwdStrt, bkwdEnd);
            System.exit(0);
        }
    }

    static boolean isSorted(int[] a) {
        int N = a.length;
        for (int i = 1; i < N; i++)
            if (a[i] < a[i-1])
                return false;
        return true;
    }
}
