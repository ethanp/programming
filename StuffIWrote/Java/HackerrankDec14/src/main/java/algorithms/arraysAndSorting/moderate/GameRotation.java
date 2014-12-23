package algorithms.arraysAndSorting.moderate;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/22/14
 */
public class GameRotation {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long N = in.nextInt();
        in.nextLine();
        long[] a = new long[(int)N];
        String line = in.nextLine();
        String[] spl = line.split(" ");
        long sum = 0;

        // get elems & sum -- O(N)
        for (int i = 0; i < N; i++) {
            a[i] = Long.parseLong(spl[i]);
            sum += a[i];
        }

        long pMean = 0;

        // get initial pMean -- O(N)
        for (int i = 0; i < N; i++)
            pMean += (i+1)*a[i];

        long max = pMean;
        // calculate each rotation based on previous & save max -- O(N)
        for (int i = 0; i < N; i++) {

            /**
             * this is the mechanism of e.g. the first rotation
             *   - the previous coefficients are (1, 2, 3, 4)
             *   - subtract the "sum", leaving (0, 1, 2, 3)
             *   - add a[0] * N, leaving (4, 1, 2, 3)
             *   - this is indeed clever, though I can't take much credit for it
             */
            pMean = pMean - sum + a[i]*N;
            if (pMean > max)
                max = pMean;
        }
        System.out.println(max);
    }
}
