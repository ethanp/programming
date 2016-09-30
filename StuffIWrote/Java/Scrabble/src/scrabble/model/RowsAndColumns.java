package scrabble.model;

import scrabble.view.ScrabbleBoard;

import java.util.function.BiConsumer;

/**
 * 9/30/16 2:35 AM
 */
public class RowsAndColumns {
    public static void each(BiConsumer<Integer, Integer> biFunction) {
        for (int row = 0; row < ScrabbleBoard.NUM_ROWS; row++) {
            for (int col = 0; col < ScrabbleBoard.NUM_COLS; col++) {
                biFunction.accept(row, col);
            }
        }
    }
}
