package algorithms.implementation.easy;

import java.util.Scanner;

/**
 * Ethan Petuchowski 2/20/16
 */
public class ChocolateFeast {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        for (int t = 0; t < T; t++) {
            int purse = in.nextInt();
            int cost = in.nextInt();
            int freedom = in.nextInt();

            int bought = purse/cost;
            int wrappers = bought;
            while (wrappers >= freedom) {
                int more = wrappers/freedom;
                int leftover = wrappers%freedom;
                wrappers = more+leftover;
                bought += more;
            }
            System.out.println(bought);
        }
    }
}
