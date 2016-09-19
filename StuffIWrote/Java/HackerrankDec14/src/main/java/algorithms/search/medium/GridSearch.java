package algorithms.search.medium;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/21/14
 *
 * seems to me this might be classified as a
 *      "single pattern string searching problem"
 * two suitable algorithms listed on Wikipedia for that problem are
 *    - Knuth-Morris-Pratt
 *    - Boyer-Moore -- "the std benchmk for practical string srch literature"
 *  => I'll go with Boyer-Moore then.
 *
 *  On second thought, why re-implement regex when you can just use it!
 */
public class GridSearch {
    static String[] grid;
    static String[] patterns;
    static Scanner in = new Scanner(System.in);

    static String run() {
        boolean found;
        for (int row = 0; row <= grid.length-patterns.length; row++) {
            found = true;
            for (int patNo = 0; patNo < patterns.length; patNo++) {
                if (!grid[row+patNo].contains(patterns[patNo])) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return "YES";
            }
        }
        return "NO";
    }

    public static void main(String[] args) {
        int T = in.nextInt();
        for (int t = 0; t < T; t++) {
            readInput();
            System.out.println(run());
        }
    }

    static void readInput() {
        int gRows = in.nextInt();
        grid = new String[gRows];
        in.nextLine();
        for (int r = 0; r < gRows; r++)
            grid[r] = in.nextLine();
        int pRows = in.nextInt();
        patterns = new String[pRows];
        in.nextLine();
        for (int r = 0; r < pRows; r++)
            patterns[r] = in.nextLine();
    }
}
