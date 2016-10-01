package scrabble.view;

import javafx.scene.layout.GridPane;
import scrabble.model.BoardModel;
import scrabble.model.LetterModel;
import scrabble.model.RowsAndColumns;
import scrabble.model.TileModel;

import static scrabble.model.BoardModel.NUM_COLS;
import static scrabble.model.BoardModel.NUM_ROWS;


/**
 * 9/30/16 2:33 AM
 */
class ScrabbleBoardView extends GridPane {
    private final BoardModel boardModel;
    private double rowHeight;
    private double colWidth;

    ScrabbleBoardView(BoardModel boardModel, double width, double height) {
        this.boardModel = boardModel;
        colWidth = width/NUM_COLS;
        rowHeight = height/NUM_ROWS;
        RowsAndColumns.each((row, col) -> {
            TileModel squareData = boardModel.getSquare(row, col);
            this.add(blankTile(squareData), row, col);
        });
    }

    private BoardTileView blankTile(TileModel tileModel) {
        return new BoardTileView(colWidth, rowHeight, tileModel);
    }

    private void setLetter(int row, int col, LetterModel letterModel) {
        ((BoardTileView) getChildren().get(NUM_COLS*row + col)).setLetterModel(letterModel);
    }
}
