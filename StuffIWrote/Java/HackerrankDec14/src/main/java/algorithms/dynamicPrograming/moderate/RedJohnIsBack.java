package algorithms.dynamicPrograming.moderate;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Ethan Petuchowski 1/7/15
 */
public class RedJohnIsBack {

    static boolean[] primeBools;
    static int[] primesLessEq;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        int[] res = new int[T];
        for (int t = 0; t < T; t++) {
            int N = in.nextInt();
            if (N < 4) {
                res[t] = 0;
                continue;
            }
            int fours = N/4;
            int rem = N%4;

            // TODO this part of the algorithm is not correct
            /* instead I must use the formula I discovered
             * \Pi{i=0}{n}{\frac{n+i}{i+1}} which can be cached as a table
             * but maybe (just maybe...) there's a non-iterative way to
             * compute that.  The only problem is I don't REALLY understand
             * where this formula comes from so I don't know whether there's
             * a "better" formula for it (or not).
             *
             * I really need to think it through again to see if I can "get it".
             * If not I should just implement the damn table and move on to
             * something else.
             */
            int sum = fours+1;
            for (int i = 0; i < fours; i++) {
                sum += rem + (4*i);
            }
            res[t] = sum;
        }
        sieve(biggest(res));
        for (int i : res) {
            System.out.println(primesLessEq[i]);
        }
    }

    static int biggest(int... arr) {
        int max = arr[0];
        for (int i : arr)
            if (i > max)
                max = i;
        return max;
    }

    /**
     * Sieve of Eratosthenes
     * if we wait until we have calculated all numbers to put in here,
     * we only need *run* the sieve one time, saving some effort in caching
     */
    static void sieve(int n) {
        primeBools = new boolean[n+1];
        Arrays.fill(primeBools, true);
        primeBools[0] = primeBools[1] = false;
        for (int i = 2; i <= Math.ceil(Math.sqrt(i)); i++) {
            if (!primeBools[i])
                continue;
            for (int j = i*i; j <= n; j += j)
                primeBools[j] = false;
        }
        fillPrimesLessEq(n);
    }

    /**
     * Here we fill the primesLessEq array.
     * By only calling it with the largest number we have,
     * we only need fill the array one time.
     */
    static void fillPrimesLessEq(int n) {
        primesLessEq = new int[n+1];
        int ctr = 0;
        for (int i = 2; i <= n; i++) {
            if (primeBools[i])
                ctr++;
            primesLessEq[i] = ctr;
        }
    }
}
