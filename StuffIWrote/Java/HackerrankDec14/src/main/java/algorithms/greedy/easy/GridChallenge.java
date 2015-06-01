package algorithms.greedy.easy;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Ethan Petuchowski 5/31/15
 */
public class GridChallenge {
    public static void main(String[] args) { new GridChallenge(); }

    char[][] grid;
    GridChallenge() {
        Scanner sc = new Scanner(System.in);
        int T = Integer.parseInt(sc.nextLine());
        for (int testCase = 0; testCase < T; testCase++) {
            int N = Integer.parseInt(sc.nextLine());
            grid = new char[N][];
            for (int rowNum = 0; rowNum < N; rowNum++) {
                grid[rowNum] = sc.nextLine().toCharArray();
                Arrays.sort(grid[rowNum]);
            }
            checkSoln();
        }
    }

    private void checkSoln() {
        for (int colNum = 0; colNum < grid[0].length; colNum++) {
            for (int rowNum = 1; rowNum < grid.length; rowNum++) {
                if (grid[rowNum-1][colNum] > grid[rowNum][colNum]) {
                    System.out.println("NO");
                    return;
                }
            }
        }
        System.out.println("YES");
    }
}
