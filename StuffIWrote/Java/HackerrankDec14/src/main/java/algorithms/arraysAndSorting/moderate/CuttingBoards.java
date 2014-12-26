package algorithms.arraysAndSorting.moderate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

/**
 * Ethan Petuchowski 12/22/14
 */
public class CuttingBoards {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        for (int t = 0; t < T; t++) {
            int M = in.nextInt();
            int N = in.nextInt();
            Long[] Y = new Long[M-1];
            Long[] X = new Long[N-1];
            for (int i = 0; i < M-1; i++)
                Y[i] = in.nextLong();
            for (int i = 0; i < N-1; i++)
                X[i] = in.nextLong();
            Arrays.sort(Y, Collections.reverseOrder());
            Arrays.sort(X, Collections.reverseOrder());
            long yCuts = 0;
            long xCuts = 0;
            long acc = 0;
            while (yCuts < Y.length && xCuts < X.length) {
                if (Y[(int)yCuts] > X[(int)xCuts])
                    acc += Y[(int)yCuts++]*(xCuts+1);
                else if (Objects.equals(Y[(int)yCuts], X[(int)xCuts])) {
                    if (yCuts > xCuts)
                        acc += Y[(int)yCuts++]*(xCuts+1);
                    else
                        acc += X[(int)xCuts++]*(yCuts+1);
                }
                else
                    acc += X[(int)xCuts++]*(yCuts+1);
                acc %= 1E9+7;
            }
            while (yCuts < Y.length) {
                acc += Y[(int) yCuts++]*(xCuts+1);
                acc %= 1E9+7;
            }
            while (xCuts < X.length) {
                acc += X[(int) xCuts++]*(yCuts+1);
                acc %= 1E9+7;
            }

            System.out.println((long) (acc%(1E9+7)));
        }
    }
}
