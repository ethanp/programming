package algorithms.dynamicPrograming.moderate;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * Ethan Petuchowski 1/7/15
 */
public class FibonacciModified {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        BigInteger A = BigInteger.valueOf(in.nextInt());
        BigInteger B = BigInteger.valueOf(in.nextInt());
        BigInteger C = BigInteger.ONE;
        int N = in.nextInt();
        for (int i = 0; i < N-2; i++) {
            C = B.pow(2).add(A);
            A = B;
            B = C;
        }
        System.out.println(C);
    }
}
