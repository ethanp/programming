package artificialIntelligence.botBuilding.easy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Ethan Petuchowski 1/2/16
 */
public class BotClean {
    static void nextMove(int n, int r, int c, String [] grid){
        List<Pt> dirties = new ArrayList<>();
        Pt myLoc = new Pt(r, c);

        /* find all the dirty squares */
        for (int row = 0; row < grid.length; row++) {
            int col = -1;
            do {
                col = grid[row].indexOf('d', col+1);
                if (col > -1) dirties.add(new Pt(row, col));
            } while (col > -1);
        }

        /* find the closest dirty square */
        int minDist = Integer.MAX_VALUE;
        Pt bestPt = null;
        for (Pt d : dirties) {
            int distance = myLoc.manhattanDistance(d);
            if (distance < minDist) {
                minDist = distance;
                bestPt = d;
            }
        }

        /* move towards it or clean it up */
        if (minDist == 0) System.out.println("CLEAN");
        else if (r < bestPt.row) System.out.println("DOWN");
        else if (r > bestPt.row) System.out.println("UP");
        else if (c < bestPt.col) System.out.println("RIGHT");
        else if (c > bestPt.col) System.out.println("LEFT");
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int r = in.nextInt();
        int c = in.nextInt();
        in.useDelimiter("\n");
        String f = in.next();
        int n = f.length();
        String grid[] = new String[n];
        for(int i = 0; i < n; i++) {
            if (i == 0) grid[i] = f;
            else grid[i] = in.next();
        }
        nextMove(grid.length,r,c,grid);
    }
    static class Pt {
        final int row, col;
        public Pt(int row, int col) {
            this.row = row;
            this.col = col;
        }
        public int manhattanDistance(Pt other) {
            return Math.abs(this.row-other.row)
                +  Math.abs(this.col-other.col);
        }
    }
}
