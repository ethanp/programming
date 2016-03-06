package ch1;

/**
 * Ethan Petuchowski 2/21/16
 *
 * 1.4.37 Run experiments to determine the performance penalty on your machine for using autoboxing
 * and auto-unboxing. (result: it's not nearly as bad as I expected.)
 *
 * Results
 * -------
 *
 * 10:               0.009123 ms
 * 100:              0.017030 ms
 * 1,000:            0.431887 ms
 * 10,000:           0.688753 ms
 * 100,000:          0.013401 ms [this drop is repeatable, what's going on here? Something GC-related?]
 * 1,000,000:        8.242396 ms
 * 10,000,000:   1,999.605690 ms
 * 100,000,000: 27,866.729022 ms
 *
 * // then the UNBOXED version causes OOM
 * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
 * 	at ch1.BoxingPenalty.main(BoxingPenalty.java:30)
 */
public class BoxingPenalty {
    public static void main(String[] args) {
        for (int j = 1; j < 12; j++) {
            int SIZE = (int) Math.pow(10, j);
            long beforeBare = System.nanoTime();
            int[] bare = new int[SIZE];
            for (int i = 0; i < SIZE; i++) bare[i] = i;
            long acc = 0;
            for (int i = 0; i < SIZE; i++) acc += bare[i];
            long afterBare = System.nanoTime();
            long beforeBoxed = System.nanoTime();
            Integer[] boxed = new Integer[SIZE];
            for (int i = 0; i < SIZE; i++) boxed[i] = i;
            long acc2 = 0;
            for (int i = 0; i < SIZE; i++) acc2 += boxed[i];
            long afterBoxed = System.nanoTime();
            System.out.printf("%d: %.6f ms\n",
                (int)Math.pow(10,j), (double)((afterBoxed-beforeBoxed)-(afterBare-beforeBare))/Math.pow(10, 6));
        }
    }
}
