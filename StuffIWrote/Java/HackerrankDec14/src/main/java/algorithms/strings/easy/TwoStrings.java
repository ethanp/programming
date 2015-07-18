package algorithms.strings.easy;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Ethan Petuchowski 7/18/15
 */
public class TwoStrings {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int T = Integer.parseInt(sc.nextLine());
        for (int test = 0; test < T; test++) {
            new TwoStrings();
        }
    }
    TwoStrings() {
        String s1 = sc.nextLine();
        String s2 = sc.nextLine();
        Set<Character> set = new HashSet<>();
        for (char c : s1.toCharArray()) set.add(c);
        for (char c : s2.toCharArray()) {
            if (set.contains(c)) {
                System.out.println("YES");
                return;
            }
        }
        System.out.println("NO");
    }
}
