package algorithms.search.medium;

import java.util.Scanner;

/**
 * 9/18/16 8:19 PM
 *
 * I'm going to start with a brute-force-y way of doing it.
 * Some guy online hints that it can be done in O(N) time O(1) space.
 * I haven't been able to figure that one out. He says it involves some
 * preprocessing to build up some data structure for suffixes. I've
 * heard of similar things but I don't know them so I will look that up.
 */
public class ShortPalindrome {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.next();
        int count = bruteForce(input);
        int modded = count%((int) 1E9) + 7;
        System.out.println(modded);
    }

    static int bruteForce(String input) {
        int count = 0;
        for (int a = 0; a < input.length() - 3; a++)
            for (int b = a + 1; b < input.length() - 2; b++)
                for (int c = b + 1; c < input.length() - 1; c++)
                    for (int d = c + 1; d < input.length(); d++)
                        if (input.charAt(a) == input.charAt(d))
                            if (input.charAt(b) == input.charAt(c))
                                count++;
        return count;
    }
}
