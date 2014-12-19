package algorithms.arraysAndSorting.easy;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Ethan Petuchowski 12/18/14
 */
public class MarkAndToys {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int K = in.nextInt();
        int[] prices = new int[N];
        for (int i = 0; i < N; i++)
            prices[i] = in.nextInt();
        Arrays.sort(prices);
        int i = 0;
        while (true) {
            K -= prices[i];
            if (K > 0)
                i++;
            else
                break;
        }
        System.out.println(i);
    }
}
