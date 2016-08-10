package algorithms.strings.easy;

import java.util.Scanner;

/**
 * 8/10/16 7:57 AM
 */
public class SuperReduced {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        int d;
        while ((d = findDupe(s)) > -1)
            s = removeDupe(s, d);
        System.out.println(s.isEmpty() ? "Empty String" : s);
    }

    private static String removeDupe(String s, int d) {
        return s.substring(0, d)
            + s.substring(d + 2, s.length());
    }

    private static int findDupe(String s) {
        if (s.isEmpty()) return -1;
        char c = s.charAt(0);
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == c) return i-1;
            c = s.charAt(i);
        }
        return -1;
    }
}
