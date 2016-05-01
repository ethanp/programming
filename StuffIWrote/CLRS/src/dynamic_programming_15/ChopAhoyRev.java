package dynamic_programming_15;

import java.util.Arrays;

/**
 * Ethan Petuchowski 4/30/16
 *
 * I'm doing this because it is part of the tutorial at
 *
 * https://tp-iiita.quora.com/Dynamic-Programming-Part-1
 *
 * It is from a SPOJ problem at
 *
 * http://www.spoj.com/problems/ANARC05H/en/
 *
 * I am writing this code after reading the verbal version of top-down solution in the tutorial, and
 * before reading his corresponding code.
 *
 * The PROBLEM STATEMENT is as follows
 *
 * Given a string composed of digits only, we may group these digits into substrings, if for every
 * substring but the last one, the sum of the digits in a substring is less than or equal to the sum
 * of the digits in the substring immediately on its right. => Calculate the total number of
 * possibilities of such groupings for a given string of digits.
 *
 * That was pretty straightforward, though I forgot to create the cache.
 */
public class ChopAhoyRev {

    static int[] input;
    static int[][] resultsCache;

    static int topDown(int grpStartIdx, int prevSum) {
        assert grpStartIdx <= input.length;

        // if we're at the end, we've already found one
        if (grpStartIdx == input.length) return 1;

        // maybe we've already seen these arguments
        if (resultsCache[grpStartIdx][prevSum] > -1)
            return resultsCache[grpStartIdx][prevSum];

        // otw let's try to find as many as possible
        int resultCount = 0;

        // the current group starts out empty
        int grpSum = 0;

        // we successively see what happens after we add each next digit to the current group
        for (int grpEndIdx = grpStartIdx; grpEndIdx < input.length; grpEndIdx++) {

            // add next digit to current group
            grpSum += input[grpEndIdx];

            // if it is now a valid group
            if (grpSum >= prevSum) {

                // count how many results we get using this as a group
                resultCount += topDown(grpEndIdx+1, grpSum);
            }
        }

        // store result in cache
        resultsCache[grpStartIdx][prevSum] = resultCount;

        // tell caller how many results were obtained with these arguments
        return resultCount;
    }

    static int calculate(String in) {

        // store input as global state
        input = new int[in.length()];
        for (int idx = 0; idx < in.length(); idx++)
            input[idx] = in.charAt(idx)-'0';

        // create results cache
        int maxSum = 0; for (int i : input) maxSum += i;
        resultsCache = new int[in.length()][maxSum];
        for (int[] arr : resultsCache) Arrays.fill(arr, -1);

        // run the algorithm
        return topDown(0, 0);
    }

    public static void main(String[] args) {
        givenExample1();
        givenExample2();
    }

    static void givenExample1() {
        shouldBe("635", 2);
    }

    static void givenExample2() {
        shouldBe("1117", 7);
    }

    static void shouldBe(String input, int desiredResult) {
        int result = calculate(input);
        System.out.println(result);
        assert result == desiredResult;
    }
}
