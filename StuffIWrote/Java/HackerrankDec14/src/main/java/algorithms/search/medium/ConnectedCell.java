package algorithms.search.medium;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 * Ethan Petuchowski 4/28/15
 *
 * plan: bfs, when a node is expanded, its value becomes zero
 */
public class ConnectedCell {
    public static void main(String[] args) {
        new ConnectedCell();
    }
    int[][] grid;
    int nRows, nCols;
    ConnectedCell() {
        Scanner sc = new Scanner(System.in);
        nRows = sc.nextInt();
        nCols = sc.nextInt();
        grid = new int[nRows][nCols];
        for (int r = 0; r < nRows; r++) {
            for (int c = 0; c < nCols; c++) {
                grid[r][c] = sc.nextInt();
            }
        }
        int max = 0;
        for (int r = 0; r < nRows; r++) {
            for (int c = 0; c < nCols; c++) {
                int val = bfs(r,c);
                if (val > max) {
                    max = val;
                }
            }
        }
        System.out.println(max);
    }

    private boolean inBounds(int r, int c) {
        return r >= 0
            && r < nRows
            && c >= 0
            && c < nCols;
    }

    class Cell {
        Cell(int r, int c) { row = r; col = c; }
        int row, col;

        public Iterable<? extends Cell> getNeighbors() {
            List<Cell> list = new ArrayList<>();
            for (int i = row-1; i < row+2; i++) {
                for (int j = col-1; j < col+2; j++) {
                    if (!(i == 0 && j == 0) && inBounds(i, j)) {
                        list.add(new Cell(i, j));
                    }
                }
            }
            return list;
        }

        public boolean isOne() {
            return grid[row][col] == 1;
        }

        public void setToZero() {
            grid[row][col] = 0;
        }
    }

    private int bfs(int r, int c) {

        if (grid[r][c] == 0) return 0;
        else grid[r][c] = 0;

        Queue<Cell> pq = new ArrayDeque<>();
        pq.add(new Cell(r,c));
        int ctr = 1;
        while (!pq.isEmpty()) {
            Cell cell = pq.poll();
            for (Cell nbr : cell.getNeighbors()) {
                if (nbr.isOne()) {
                    ctr++;
                    nbr.setToZero();
                    pq.add(nbr);
                }
            }
        }
        return ctr;
    }
}
