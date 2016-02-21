package algorithms.implementation.easy;

import java.util.Scanner;

/**
 * Ethan Petuchowski 2/20/16
 */
public class FindDigits {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        for (int t = 0; t < T; t++) {
            int N = in.nextInt();
            int orig = N;
            int acc = 0;
            while (N > 0) {
                int digit = N % 10;
                if (digit != 0 && orig % digit == 0) {
                    acc++;
                }
                N /= 10;
            }
            System.out.println(acc);
        }
    }
}
