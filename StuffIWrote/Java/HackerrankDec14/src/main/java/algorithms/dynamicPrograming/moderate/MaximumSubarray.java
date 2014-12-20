package algorithms.dynamicPrograming.moderate;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/19/14
 *
 * Given an array of N elements, find the maximum possible sum of a
 *
 *  1. contiguous subarray
 *  2. non-contiguous (not necessarily contiguous) subarray.
 *
 *  Empty subarrays should not be considered.
 */
public class MaximumSubarray {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int cases = in.nextInt();
        for (int caseNum = 0; caseNum < cases; caseNum++) {
            int arrSize = in.nextInt();
            int[] arr = new int[arrSize];
            for (int i = 0; i < arrSize; i++)
                arr[i] = in.nextInt();

            if (allNeg(arr)) {
                int maxElem = arrMax(arr);
                System.out.println(maxElem+" "+maxElem);
            } else {
                System.out.println(contiguousSum(arr)+" "+noncontiguousSum(arr));
            }
        }
    }

    static int contiguousSum(int[] arr) {
        int max_sum  = 0;
        int curr_sum = 0;

        for (int i = 0; i < arr.length; i++) {
            curr_sum += arr[i];
            if (curr_sum < 0)
                curr_sum = 0;
            if (curr_sum > max_sum)
                max_sum = curr_sum;
        }
        return max_sum;
    }

    /** simply add all positive elements */
    static int noncontiguousSum(int[] arr) {
        int acc = 0;
        for (int elem : arr)
            if (elem > 0)
                acc += elem;
        return acc;
    }

    static boolean allNeg(int[] arr) {
        for (int elem : arr)
            if (elem >= 0)
                return false;
        return true;
    }

    static int arrMax(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int elem : arr)
            if (elem > max)
                max = elem;
        return max;
    }
}
