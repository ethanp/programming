package algorithms.strings.difficult;

import java.util.Scanner;

/**
 * Ethan Petuchowski 2/12/16
 */
public class SherlocksValidString {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        String s = sc.nextLine();
        int[] ctr = new int[26];
        for (char c : s.toCharArray()) {
            ctr[c-'a'] += 1;
        }

        // try first non-zero value
        int v = 0;
        for (int i : ctr) {
            if (i > 0) {
                v = i;
                break;
            }
        }
        if (v == 0 || doCheck(ctr, v)) {
            System.out.println("YES");
            return;
        }

        // try second non-zero value
        boolean second = false;
        v = 0;
        for (int i : ctr) {
            if (i > 0) {
                if (!second) {
                    second = true;
                }
                else {
                    v = i;
                    break;
                }
            }
        }
        if (v == 0 || doCheck(ctr, v)) {
            System.out.println("YES");
        }

        // we can't all be winners
        else System.out.println("NO");
    }

    static boolean doCheck(int[] ctr, int v) {
        boolean mulliganed = false;
        for (int i : ctr) {
            if (i > 0 && i != v) {
                if (i == 1 || i-v == 1) {
                    if (!mulliganed) mulliganed = true;
                    else return false;
                }
                else return false;
            }
        }
        return true;
    }
}
