package ch1;

/**
 * Ethan Petuchowski 2/21/16
 *
 * 1.4.37 Run experiments to determine the performance penalty on your machine for using autoboxing
 * and auto-unboxing. Result: either it's not nearly as bad as I expected, or there are sneaky
 * optimizations being performed.
 *//*
 * @formatter:off
 *
 * Task: Sum up arrays of given sizes,
 *      Then take the difference in the time taken between using an array of primitive `int`s, and
 *      an array of "boxed" `Integer`s.
 * --------------------------------------------
 *
 * 10:               0.009123 ms
 * 100:              0.017030 ms
 * 1,000:            0.431887 ms
 * 10,000:           0.688753 ms
 * 100,000:          0.013401 ms [this drop is repeatable, what's going on here? Something GC-related? Caching? JIT?]
 * 1,000,000:        8.242396 ms
 * 10,000,000:   1,999.605690 ms
 * 100,000,000: 27,866.729022 ms
 *
 * // then the UNBOXED version causes OOM
 * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
 * 	at ch1.BoxingPenalty.main(BoxingPenalty.java:30)
 *
 * @formatter:on
 */
public class BoxingPenalty {
    public static void main(String[] args) {
        for (int j = 1; j < 12; j++) {
            int sizeThisRound = (int) Math.pow(10, j);
            long primitiveDuration = timePrimitiveArraySum(sizeThisRound);
            long boxedDuration = timeBoxedArraySum(sizeThisRound);
            double differenceInMilliseconds = calculateTimeDifference(primitiveDuration, boxedDuration);
            System.out.printf("%d: %.6f ms\n", sizeThisRound, differenceInMilliseconds);
        }
    }

    private static double calculateTimeDifference(long primitiveArraySumDuration, long boxedArraySumDuration) {
        long totalDurationDifference = boxedArraySumDuration - primitiveArraySumDuration;
        double msPerSec = Math.pow(10, 6);
        return (double) totalDurationDifference/msPerSec;
    }

    private static long timeBoxedArraySum(int sizeThisRound) {
        long boxedArrayStartTime = System.nanoTime();
        Integer[] boxedIntegerArray = new Integer[sizeThisRound];
        for (int i = 0; i < sizeThisRound; i++) boxedIntegerArray[i] = i;
        long accumulator2 = 0;
        for (int i = 0; i < sizeThisRound; i++) accumulator2 += boxedIntegerArray[i];
        long boxedArrayEndTime = System.nanoTime();
        return boxedArrayEndTime - boxedArrayStartTime;
    }

    private static long timePrimitiveArraySum(int sizeThisRound) {
        long primitiveArrayStartTime = System.nanoTime();
        int[] bareIntArray = new int[sizeThisRound];
        for (int i = 0; i < sizeThisRound; i++) bareIntArray[i] = i;
        long accumulator = 0;
        for (int i = 0; i < sizeThisRound; i++) accumulator += bareIntArray[i];
        long primitiveArrayEndTime = System.nanoTime();
        return primitiveArrayEndTime - primitiveArrayStartTime;
    }
}
