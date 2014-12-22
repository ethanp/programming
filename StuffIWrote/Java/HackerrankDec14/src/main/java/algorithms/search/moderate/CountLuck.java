package algorithms.search.moderate;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/**
 * Ethan Petuchowski 12/22/14
 */
public class CountLuck {

    static int rows, cols;
    static char[][] forest;
    static boolean[][] trodden;
    static int maxWaves;
    static Loc start;
    static Loc goal;

    static String searchUponAStar() {
        Queue<Loc> locQueue = new PriorityQueue<>();
        locQueue.add(start);
        while (!locQueue.isEmpty()) {
            Loc loc = locQueue.poll();
            trodden[loc.x][loc.y] = true;
            /* if it's not a fork, we can just keep going */
            /* I suppose a fork means there are two unvisited neighbors */
            boolean isFork = loc.fork();
            for (int i = 0; i < loc.numNeighbors; i++) {
                Loc nbr = loc.neighbors[i];
                if (!nbr.trodden()) {
                    if (isFork)
                        nbr.waves++;
                    if (nbr.equals(goal))
                        return nbr.waves == maxWaves ? "Impressed" : "Oops!";
                    locQueue.add(nbr);
                }
            }
        }
        return "Couldn't find path from start to goal";
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int tests = in.nextInt();
        for (int t = 0; t < tests; t++) {
            readInput(in);
            System.out.println(searchUponAStar());
        }
    }

    static void readInput(Scanner in) {
        rows = in.nextInt();
        cols = in.nextInt();
        in.nextLine();
        forest = new char[rows][];
        trodden = new boolean[rows][cols];
        for (int r = 0; r < rows; r++) {
            String line = in.nextLine();
            forest[r] = line.toCharArray();
            int idx;
            if ((idx = line.indexOf('*')) >= 0)
                goal = new Loc(r, idx, 0);
            if ((idx = line.indexOf('M')) >= 0)
                start = new Loc(r, idx, 0);
        }
        maxWaves = in.nextInt();
    }

    static class Loc implements Comparable {
        int x, y, waves;
        Loc[] neighbors = new Loc[4]; // lazy
        int numNeighbors = -1;  // to make it "C-like" but really a waste of time

        public Loc(int iX, int iY, int t) { x = iX; y = iY; waves = t; }
        public boolean equals(Object o) {
            return x == ((Loc) o).x && y == ((Loc) o).y;
        }

        public int compareTo(Object o) {
            Loc ll = (Loc)o;
            int turnDiff = waves- ll.waves;
            if (turnDiff != 0) return turnDiff;
            return goalDist() - ll.goalDist();
        }

        int goalDist() {
            return Math.abs(goal.x-x) + Math.abs(goal.y-y);
        }

        boolean treadable() {
            return x >= 0 && x < rows
                && y >= 0 && y < cols
                && forest[x][y] != 'X';
        }

        boolean trodden() {
            return trodden[x][y];
        }

        void place(int x, int y) {
            Loc loc = new Loc(x, y, waves);
            if (loc.treadable())
                neighbors[numNeighbors++] = loc;
        }

        void findNeighbors() {
            numNeighbors = 0;
            place(x-1, y);
            place(x+1, y);
            place(x, y-1);
            place(x, y+1);
        }

        boolean fork() {
            if (numNeighbors == -1) {
                findNeighbors();
            }
            boolean foundOne = false;
            for (int i = 0; i < numNeighbors; i++) {
                if (!neighbors[i].trodden()) {
                    if (!foundOne) {
                        foundOne = true;
                    }
                    else {
                        return true;
                    }
                }
            }
            return false;
        }

    }
}
