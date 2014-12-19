package algorithms.combinatorics.easy;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/18/14
 */
public class ConnectingTowns {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int cases = in.nextInt();
        for (int c = 0; c < cases; c++) {
            long accumulator = 1;
            int towns = in.nextInt();
            for (int town = 0; town < towns-1; town++) {
                accumulator = (accumulator%1234567)*in.nextInt();
            }
            System.out.println(accumulator%1234567);
        }
    }
}
