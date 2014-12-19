package algorithms.combinatorics.easy;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/18/14
 */
public class MinimumDraws {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int testCases = in.nextInt();
        for (int i = 0; i < testCases; i++) {
            int numPairs = in.nextInt();
            System.out.println(2+(numPairs-1));
        }
    }
}
