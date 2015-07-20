package algorithms.dynamicPrograming.moderate;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Ethan Petuchowski 7/18/15
 *
 * got some help :(
 */
public class Candies {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        new Candies();
    }

    Candies() {
        int numChildren = sc.nextInt();
        int[] ratings = new int[numChildren];
        int[] candies = new int[numChildren];
        Arrays.fill(candies, 1);

        // first we make sure to do it right for those monotonically-increasing sequences
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = sc.nextInt();
            if (i == 0) continue;
            if (ratings[i] > ratings[i-1]) {
                candies[i] = candies[i-1]+1;
            }
        }

        // then we correct for those monotonically-decreasing sequences.
        // the trick is that we can say that the left one must be more than the right one
        // but if it's already HIGHER than that, just leave it how it is.
        for (int i = ratings.length-1; i > 0; i--) {
            if (ratings[i-1] > ratings[i]) {
                candies[i-1] = Math.max(candies[i-1], candies[i]+1);
            }
        }
        System.out.println(sum(candies));
    }

    private int sum(int[] a) {
        int ctr = 0;
        for (int i:a) ctr += i;
        return ctr;
    }
}
