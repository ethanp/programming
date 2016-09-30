package scrabble.view;

import javafx.scene.layout.GridPane;
import scrabble.model.BoardModel;
import scrabble.model.Letter;
import scrabble.model.RowsAndColumns;


/**
 * 9/30/16 2:33 AM
 */
public class ScrabbleBoard extends GridPane {
    public static final int NUM_ROWS = 10;
    public static final int NUM_COLS = 10;
    private final BoardModel boardModel;
    private double rowHeight;
    private double colWidth;

    ScrabbleBoard(BoardModel boardModel, double width, double height) {
        this.boardModel = boardModel;
        colWidth = width/NUM_COLS;
        rowHeight = height/NUM_ROWS;
        RowsAndColumns.each((row, col) -> add(blankTile(), row, col));
        setLetter(2, 3, new Letter('C', 5));
    }

    private Tile blankTile() {
        return new Tile(colWidth, rowHeight);
    }

    private void setLetter(int row, int col, Letter letter) {
        ((Tile) getChildren().get(NUM_COLS*row + col)).setLetter(letter);
    }
}
