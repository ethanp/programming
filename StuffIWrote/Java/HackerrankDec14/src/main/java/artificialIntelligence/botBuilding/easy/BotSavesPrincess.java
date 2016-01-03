package artificialIntelligence.botBuilding.easy;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Ethan Petuchowski 1/2/16
 */
public class BotSavesPrincess {
    static void displayPathToPrincess(int n, String[] grid) {
        String[] results = returnPathToPrincess(grid);
        for (String r : results) System.out.println(r);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        int m = in.nextInt();
        String grid[] = new String[m];
        for (int i = 0; i < m; i++) grid[i] = in.next();
        displayPathToPrincess(m, grid);
    }

    public static String[] returnPathToPrincess(String[] grid) {
        Pt princess = locOf('p', grid);
        Pt start = locOf('m', grid);
        List<String> verts = new ArrayList<>();
        List<String> horz = new ArrayList<>();
        if (start.row > princess.row) {
            for (int i = princess.row; i < start.row; i++) {
                verts.add("UP");
            }
        }
        else if (start.row < princess.row) {
            for (int i = start.row; i < princess.row; i++) {
                verts.add("DOWN");
            }
        }
        if (start.col > princess.col) {
            for (int i = princess.col; i < start.col; i++) {
                horz.add("LEFT");
            }
        }
        else if (start.col < princess.col) {
            for (int i = start.col; i < princess.col; i++) {
                horz.add("RIGHT");
            }
        }
        verts.addAll(horz);
        return verts.toArray(new String[verts.size()]);
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

    static class Pt {
        final int row, col;
        public Pt(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}
