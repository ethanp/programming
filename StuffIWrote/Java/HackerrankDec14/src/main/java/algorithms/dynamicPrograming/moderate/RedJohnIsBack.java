package algorithms.dynamicPrograming.moderate;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Ethan Petuchowski 1/7/15
 */
public class RedJohnIsBack {

    static boolean[] isPrime;
    static int[] primesMost;

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

            /**
             * Basically I think my old solution solved the problem for a
             * different understanding of "arrangements" because it worked
             * perfectly up to 38 blocks which is 10794 primes! My solution
             * was to use (I think it was) \prod_{i=1}^{n}{\frac{n+i}{i+i}}. I
             * had derived that formula from the "enumerate a few and sit and
             * stare at it" method of problem solving.
             *
             * This solution in the editorial comes from thinking about it as
             * laying down one block at a time. We can either lay a single
             * block vertically, leaving us with F(i-1) options for how to lay
             * the rest down; or we can lay a set of 4 blocks horizontally,
             * leaving us with F(i-4) ways of laying the rest down.
             */
            int[] arr = new int[N];
            arr[0] = arr[1] = arr[2] = 1;
            arr[3] = 2;
            for (int i = 4; i < N; i++) {
                arr[i] = arr[i-1] + arr[i-4];
            }
            res[t] = arr[N-1];
        }
        sieve(biggest(res));
        for (int i : res) {
            System.out.println(primesMost[i]);
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
        isPrime = new boolean[n+1];
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;
        for (int i = 2; i <= Math.ceil(Math.sqrt(n)); i++) {
            if (!isPrime[i])
                continue;
            for (int j = i*i; j <= n; j += i)
                isPrime[j] = false;
        }
        primesMost = new int[n+1];
        int ctr = 0;
        for (int i = 2; i <= n; i++) {
            if (isPrime[i])
                ctr++;
            primesMost[i] = ctr;
        }
    }
}
