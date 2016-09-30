package scrabble.model;

import javafx.geometry.Point2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 9/29/16 10:25 PM
 */
class SpecialSquareInputs {
    private Map<Point2D, Bonus> bonusMap = new HashMap<>();

    static SpecialSquareInputs readFromConfig() {
        SpecialSquareInputs ret = new SpecialSquareInputs();
        try (FileReader fileReader = new FileReader("src/scrabble/model/specialSquares.csv");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] csvLine = line.split(",");
                int row = Integer.parseInt(csvLine[0]) + 7;
                int col = Integer.parseInt(csvLine[1]);
                String bonusType = csvLine[2].toLowerCase();
                int factor = bonusType.charAt(0) == 'd' ? 2 : 3;
                boolean isWord = bonusType.charAt(1) == 'w';
                for (Point2D loc : flipsOf(row, col)) {
                    ret.bonusMap.put(loc, new Bonus(factor, isWord));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return ret;
    }

    private static Point2D[] flipsOf(int row, int col) {
        return new Point2D[]{
              // lower-left
              new Point2D(row, col),
              // lower-right
              new Point2D(row, 15 - col),
              // upper-left
              new Point2D(15 - row, col),
              // upper-right
              new Point2D(15 - row, 15 - col)
        };
    }

    Bonus get(int row, int col) {
        return bonusMap.get(new Point2D(row, col));
    }
}
