package algorithms.search.medium;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/**
 * Ethan Petuchowski 12/20/14
 * this took about 2 hours
 */
public class CoinOnTheTable {

    static int nRows, nCols;
    static int maxTime;
    static char[][] board;
    static int FAIL = Integer.MAX_VALUE;
    static int[][] minPrevVisit; /* minimum cost we've paid to get to each loc */
    static int[][] timeMin; /* the time at which we deposited that min cost */
    static int starLocRow, starLocCol = FAIL;

    public static void main(String[] args) {
        readBoard();
        System.out.println(bfs());
    }

    static class SearchObj implements Comparable {

        int row, col;
        int numChanges, timeSpent;

        SearchObj(int r, int c, int ch, int t) {
            row = r;
            col = c;
            numChanges = ch;
            timeSpent = t;
        }

        /**
         * @return a negative integer, zero, or a positive integer as this object
         * is less than, equal to, or greater than the specified object.
         */
        @Override
        public int compareTo(Object o) {
            SearchObj other = (SearchObj) o;
            int diff = numChanges - other.numChanges;
            if (diff != 0) return diff;

            /* preferentially search towards the endpoint (didn't really help) */
            return starDist() - other.starDist();
        }

        /** manhattan distance to the star */
        public int starDist() {
            return Math.abs(row - starLocRow) + Math.abs(col -starLocCol);
        }

        public boolean offBoard() {
            return row < 0 || row >= nRows || col < 0 || col >= nCols;
        }

        public boolean requiresMoreChanges() {
            if (numChanges >= minPrevVisit[row][col] && timeSpent >= timeMin[row][col])
                return true;

            // nasty little optimization (not really useful either)
            if ((numChanges <  minPrevVisit[row][col] && timeSpent <= timeMin[row][col])
             || (numChanges <= minPrevVisit[row][col] && timeSpent <  timeMin[row][col])) {
                minPrevVisit[row][col] = numChanges;
                timeMin[row][col] = timeSpent;
            }
            return false;
        }

        public char ch() {
            return board[row][col];
        }

        public int cost(char u) {
            return board[row][col] == u ? numChanges : numChanges+1;
        }

        /* this was the optimization that polished it off */
        public boolean tooFar() {
            return starDist() > maxTime - timeSpent;
        }
    }

    static int bfs() {
        Queue<SearchObj> queue = new PriorityQueue<>(); /* Always removes the LOWEST element */
        queue.add(new SearchObj(0,0,0,0));
        while (!queue.isEmpty()) {
            SearchObj elem = queue.poll();
            if (elem.offBoard() || elem.requiresMoreChanges() || elem.tooFar())
                continue;
            char ch = elem.ch();
            if (ch == '*')
                return elem.numChanges;
            int t = elem.timeSpent+1;
            queue.add(new SearchObj(elem.row-1, elem.col, elem.cost('U'), t));
            queue.add(new SearchObj(elem.row+1, elem.col, elem.cost('D'), t));
            queue.add(new SearchObj(elem.row, elem.col-1, elem.cost('L'), t));
            queue.add(new SearchObj(elem.row, elem.col+1, elem.cost('R'), t));
        }
        return -1;
    }

    static void readBoard() {
        Scanner in = new Scanner(System.in);
        nRows = in.nextInt();
        nCols = in.nextInt();
        board = new char[nRows][];
        minPrevVisit = new int[nRows][nCols];
        timeMin = new int[nRows][nCols];
        maxTime = in.nextInt();
        in.nextLine(); // read \newline
        for (int r = 0; r < nRows; r++) {
            board[r] = in.nextLine().toCharArray();
            if (starLocCol == FAIL) {
                for (int c = 0; c < nCols; c++) {
                    if (board[r][c] == '*') {
                        starLocRow = r;
                        starLocCol = c;
                    }
                }
            }
            Arrays.fill(minPrevVisit[r], FAIL);
            Arrays.fill(timeMin[r], FAIL);
        }
    }
}
