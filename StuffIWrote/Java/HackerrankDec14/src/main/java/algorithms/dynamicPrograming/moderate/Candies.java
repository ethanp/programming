package algorithms.dynamicPrograming.moderate;

import java.util.Scanner;

/**
 * Ethan Petuchowski 7/18/15
 *
 * How about I walk along and naively assign values via comparison
 * with the previous person, then normalize?
 *
 * But then for 2 1 0 2 1 0 what would I get?
 *
 *   0 -1 -2 -1 -2 -3 => 4 3 2 3 2 1 => 15
 *
 * when I could have done
 *
 *   3 2 1 3 2 1 => 12
 *
 * So how about I default back to zero when I'm below zero and must go upwards?
 *
 *   0 -1 -2 0 -1 -2 => 12
 *
 * I works this time but is it general?
 *
 * Maybe overall this is bounded by the len of the longest monotonic trend
 *
 * So above we'd get
 *
 *   0 -1 -2 0 -1 -2 => 12
 *
 * I think that's going to work.
 *
 */
public class Candies {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        new Candies();
    }

    Candies() {
        int numChildren = sc.nextInt();
        int[] ratings = new int[numChildren];
        int[] monotonic = new int[numChildren];  // track length of current monotonic trend
        int minRun = 0;
        int maxRun = 0;
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = sc.nextInt();
            if (i == 0) continue;

            // TODO I need to fix the logic here, the structure ought to be right
            if (ratings[i] > ratings[i-1]) {
                if (monotonic[i-1] > 0) {
                    monotonic[i] = monotonic[i-1] + 1;
                } else {
                    monotonic[i] = 0;
                }
            }
            else if (ratings[i] > ratings[i-1]) {
                if (monotonic[i-1] > 0) {
                    monotonic[i] = monotonic[i-1] + 1;
                } else {
                    monotonic[i] = 0;
                }
            }
        }

        /**
         * we walk backwards through the array
         *
         * mono = 0 => 0
         * mono = +n => +n
         *
         * upon reaching a negative number, we start at 0
         * and go up until mono = 0
         */
        int curVal = 0;
        boolean minusRun = false;
        for (int i = numChildren-1; i >= 0; i--) {
            if (i == 0) {
                // TODO special case
            }
            if (!minusRun) {
                if (monotonic[i] == 0) {
                    // TODO etc. (saving it for a rainy day)
                }
            }
        }
    }
}
