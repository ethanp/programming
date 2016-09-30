package scrabble.view;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import scrabble.model.Board;
import scrabble.model.RowsAndColumns;

/**
 * 9/30/16 2:33 AM
 */
public class ScrabbleBoard extends GridPane {
    public static final int NUM_ROWS = 10;
    public static final int NUM_COLS = 10;
    private final Board board;
    private double rowHeight;
    private double colWidth;

    ScrabbleBoard(Board board, double width, double height) {
        this.board = board;
        colWidth = width/NUM_COLS;
        rowHeight = height/NUM_ROWS;
        RowsAndColumns.each((row, col) -> add(blankTile(), row, col));
    }

    private Rectangle blankTile() {
        Rectangle ret = new Rectangle(colWidth, rowHeight, Color.BURLYWOOD);
        ret.setStrokeWidth(4);
        ret.setStroke(Color.BLACK);
        return ret;
    }
}
