package algorithms.search.moderate;

import java.util.Scanner;

/**
 * Ethan Petuchowski 7/30/15
 *
 * I'm just going to do the exact same thing as the "SherlockAnagrams"
 * problem. We'll see how that works out for me.
 *
 * Well that was correct but way too slow; I got 17.5 out of 40 points.
 * I think if I slide along in a sort of "accordion"-like fashion,
 * I could roughly cut my operations in half.
 *
 * After finding out that the root of my performance issues is in the
 * use of using the `modulus` operator (surprise!), I have enlisted the
 * help of Quora to explain The Better Way. The answer is written in
 *
 *      http://www.quora.com/What-is-the-logic-used-in-the-HackerRank-Maximise-Sum-problem
 *
 * But frankly I don't understand it.
 */
public class MaximiseSum {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        stillTooSlow();
    }

    public static void stillTooSlow() {
        int T = sc.nextInt();
        long a = System.nanoTime();
        for (int t = 0; t < T; t++) {
            int N = sc.nextInt();
            long M = sc.nextLong();
            long arr[] = new long[N];
            long max = 0;
            for (int n = 0; n < N; n++) {
                // this part _could_ be slightly optimized if need-be
                arr[n] = sc.nextLong();
            }
            long sum;
            for (int offset = 0; offset < N; offset++) {
                sum = arr[offset];

                // first we grow
                int len;
                for (len = 1; offset+len < N; len++) {

                    // the `modulo` operator is causing all my performance issues!!
                    // who'd'a thunk it?!
                    max = Math.max(max, sum%M);

                    sum += arr[offset+len];
                }
                max = Math.max(max, sum%M);

                // then we shrink
                sum -= arr[offset];
                offset++;
                for (len -= 2; len > 0; len--) {
                    max = Math.max(max, sum % M);
                    sum -= arr[offset+len];
                }
                max = Math.max(max, sum%M);
            }
            System.out.println(max);
        }
        long b = System.nanoTime();
        System.out.printf("%.2f", ((double) b-a)*1.25/1E9);
    }
    public static void tooSlow() {
        int T = sc.nextInt();
        long a = System.nanoTime();
        for (int t = 0; t < T; t++) {
            int N = sc.nextInt();
            long M = sc.nextLong();
            long arr[] = new long[N];
            long max = 0;
            for (int n = 0; n < N; n++) {
                // this part _could_ be slightly optimized if need-be
                arr[n] = sc.nextLong();
            }
            for (int len = 1; len <= N; len++) {
                long sum = 0;
                for (int partial = 0; partial < len; partial++) {
                    sum += arr[partial];
                }
                for (int offset = 0; offset+len < N; offset++) {
                    max = Math.max(max, sum % M);
                    sum -= arr[offset];
                    sum += arr[offset+len];
                }
                max = Math.max(max, sum % M);
            }
            System.out.println(max);
        }
        long b = System.nanoTime();
        System.out.printf("%.2f", ((double)b-a)*1.25/1E9);
    }
}
