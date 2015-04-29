package algorithms.regex.easy;

import java.util.Scanner;

/**
 * Ethan Petuchowski 4/28/15
 */
public class Tweets {

    public static final String PATTERN = "(?i:.*this is case insensitive.*)";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = Integer.parseInt(sc.nextLine());
        int ctr = 0;
        for (int i = 0; i < N; i++) {
            String s = sc.nextLine();
            if (works(s)) {
                ctr++;
            }
        }
        System.out.println(ctr);
    }

    private static boolean works(String s) {
        return s.matches(PATTERN);
    }
}
