package algorithms.bitManipulation.easy;

import java.util.Scanner;

/**
 * Ethan Petuchowski 2/20/16
 */
public class FlippingBits {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int T = sc.nextInt();
        for (int t = 0; t < T; t++) {
            long in = sc.nextLong();
            long ret = 0;
            for (int idx = 0; idx < 32; idx++) {
                // tricky: you MUST put the L's here (otw you're shifting an INT!)
                ret |= (((in >> idx) & 1) == 1 ? 0L : 1L) << idx;
            }
            System.out.println(ret);
        }
    }
}
