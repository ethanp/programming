package scrabble.view;

import javafx.scene.layout.GridPane;
import scrabble.model.BoardModel;
import scrabble.model.LetterModel;
import scrabble.model.RowsAndColumns;
import scrabble.model.TileModel;


/**
 * 9/30/16 2:33 AM
 */
public class ScrabbleBoardView extends GridPane {
    public static final int NUM_ROWS = 15;
    public static final int NUM_COLS = 15;
    private final BoardModel boardModel;
    private double rowHeight;
    private double colWidth;

    ScrabbleBoardView(BoardModel boardModel, double width, double height) {
        this.boardModel = boardModel;
        colWidth = width/NUM_COLS;
        rowHeight = height/NUM_ROWS;
        RowsAndColumns.each((row, col) -> add(blankTile(boardModel.getSquare(row, col)), row, col));
    }

    private TileView blankTile(TileModel tileModel) {
        return new TileView(colWidth, rowHeight, tileModel);
    }

    private void setLetter(int row, int col, LetterModel letterModel) {
        ((TileView) getChildren().get(NUM_COLS*row + col)).setLetterModel(letterModel);
    }
}
