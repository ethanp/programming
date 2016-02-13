package algorithms.strings.difficult;

import java.util.Scanner;

/**
 * Ethan Petuchowski 2/12/16
 *
 * This is nothing but the classic dynamic programming problem.
 * We must build the classic DP table.
 * Then we must print the final element of said table.
 *
 * I forgot at first that we can EITHER "delete" an element of 'a' OR an element of 'b'.
 */
public class CommonChild {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {

        // read input
        String a = sc.nextLine();
        String b = sc.nextLine();

        // initialize table
        int[][] table = new int[a.length()][b.length()];

        // fill table
        for (int i = 0; i < a.length(); i++) {
            char x = a.charAt(i);
            for (int j = 0; j < b.length(); j++) {
                char y = b.charAt(j);
                int same = x == y ? 1 : 0;
                int val;
                if (i == 0 && j == 0) {
                    val = same;
                } else if (j == 0) {
                    val = Math.max(same, table[i-1][j]);
                } else if (i == 0) {
                    val = Math.max(same, table[i][j-1]);
                } else {
                    val = Math.max(table[i][j-1], Math.max(table[i-1][j], table[i-1][j-1] + same));
                }
                table[i][j] = val;
            }
        }

        // print final element of table
        System.out.println(table[a.length()-1][b.length()-1]);
    }
}
