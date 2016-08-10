package dataStructures.arrays.easy;

import java.util.Scanner;

/**
 * 8/9/16 11:16 PM
 */
public class TwoD {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int arr[][] = new int[6][6];
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++)
                arr[i][j] = in.nextInt();
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                max = Math.max(max, hourglassSum(arr, i, j));
        System.out.println(max);
    }

    static int hourglassSum(int arr[][], int i, int j) {
        int row1 = arr[i][j] + arr[i][j + 1] + arr[i][j + 2];
        int row2 = arr[i + 1][j + 1];
        int row3 = arr[i + 2][j] + arr[i + 2][j + 1] + arr[i + 2][j + 2];
        return row1 + row2 + row3;
    }
}
