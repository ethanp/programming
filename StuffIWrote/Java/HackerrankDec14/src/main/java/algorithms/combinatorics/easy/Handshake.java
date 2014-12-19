package algorithms.combinatorics.easy;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Ethan Petuchowski 12/18/14
 */
public class Handshake {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int cases = in.nextInt();
        for (int i = 0; i < cases; i++) {
            int N = in.nextInt();
            System.out.println(choose2(N));
        }
    }

    private static int choose2(int n) {
        int result = 0;
        for (int i = 1; i < n; i++)
            result += i;
        return result;
    }
}
