package algorithms.arraysAndSorting.easy;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/19/14
 */
public class CavityMap {

    static int[][]      grid;
    static boolean[][]  cavities;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int size = in.nextInt(); in.nextLine();
        /* int[rowNum][colNum] => "Row Major Order" */
        grid = new int[size][size];
        cavities = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            char[] row = in.nextLine().toCharArray();
            for (int j = 0; j < size; j++) {
                grid[i][j] = row[j]-'0';
            }
        }
        /* loop through NON-BORDER cells */
        for (int i = 1; i < size-1; i++) {
            for (int j = 1; j < size-1; j++) {
                cavities[i][j] = isCavity(i, j);
            }
        }
        printOutput();
    }

    static boolean isCavity(int row, int col) {
        int val = grid[row][col];
        return val > grid[row-1][col]
            && val > grid[row+1][col]
            && val > grid[row][col-1]
            && val > grid[row][col+1];
    }

    static void printOutput() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (cavities[i][j]) {
                    System.out.print('X');
                } else {
                    System.out.print(grid[i][j]);
                }
            }
            System.out.println();
        }
    }
}
