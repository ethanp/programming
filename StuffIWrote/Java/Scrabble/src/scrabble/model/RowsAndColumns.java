package scrabble.model;

import java.util.function.BiConsumer;

/**
 * 9/30/16 2:35 AM
 */
public class RowsAndColumns {
    public static void each(BiConsumer<Integer, Integer> biConsumer) {
        for (int row = 0; row < BoardModel.NUM_ROWS; row++) {
            for (int col = 0; col < BoardModel.NUM_COLS; col++) {
                biConsumer.accept(row, col);
            }
        }
    }
}
