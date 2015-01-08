package algorithms.strings.easy;

import java.util.Scanner;

/**
 * Ethan Petuchowski 1/8/15
 * kinda redeemed myself on this one
 */
public class PalindromeIndex {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        in.nextLine();
        for (int t = 0; t < T; t++) {
            String str = in.nextLine();
            int start = 0;
            int end = str.length()-1;
            while (str.charAt(start) == str.charAt(end) && start < end) {
                start++;
                end--;
            }
            if (start >= end)
                System.out.println(-1);
            else if (isPal(str.substring(start+1, end+1)))
                System.out.println(start);
            else
                System.out.println(end);
        }
    }
    static boolean isPal(String str) {
        int start = 0;
        int end = str.length()-1;
        while (str.charAt(start) == str.charAt(end) && start < end) {
            start++;
            end--;
        }
        return start >= end;
    }
}
