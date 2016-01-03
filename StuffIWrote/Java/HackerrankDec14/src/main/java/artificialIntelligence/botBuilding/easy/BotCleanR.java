package artificialIntelligence.botBuilding.easy;

import java.util.Scanner;

/**
 * Ethan Petuchowski 1/2/16
 */
public class BotCleanR {
    static void nextMove(int posr, int posc, String[] board) {
        Pt dirt = locOf('d', board);
        Pt start = new Pt(posr, posc);
        if (start.manhattanDistance(dirt) == 0) System.out.println("CLEAN");
        else if (start.row > dirt.row) System.out.println("UP");
        else if (start.row < dirt.row) System.out.println("DOWN");
        else if (start.col > dirt.col) System.out.println("LEFT");
        else if (start.col < dirt.col) System.out.println("RIGHT");
    }

    private static Pt locOf(char c, String[] grid) {
        for (int row = 0; row < grid.length; row++) {
            int col = grid[row].indexOf(c);
            if (col > -1) {
                return new Pt(row, col);
            }
        }
        throw new RuntimeException("couldn't find a "+c);
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[] pos = new int[2];
        String board[] = new String[5];
        for (int i = 0; i < 2; i++) pos[i] = in.nextInt();
        for (int i = 0; i < 5; i++) board[i] = in.next();
        nextMove(pos[0], pos[1], board);
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
