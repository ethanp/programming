package scrabble.model;

import scrabble.view.ScrabbleBoardView;

/**
 * 9/29/16 10:24 PM
 */
public class BoardModel {
    TileModel[][] tileModels = new TileModel[ScrabbleBoardView.NUM_ROWS][ScrabbleBoardView.NUM_COLS];
    private ScrabbleGame game;

    BoardModel(ScrabbleGame game) {
        this.game = game;
        SpecialSquareInputs specialSquareInputs = SpecialSquareInputs.readFromConfig();
        assert specialSquareInputs != null;
        RowsAndColumns.each((row, col) -> tileModels[row][col] =
              new TileModel(this, specialSquareInputs.get(row, col), row, col));
    }

    public void placeLetter(LetterModel letterModel, int row, int col) {
        tileModels[row][col].placeLetter(letterModel);
    }

    public TileModel getSquare(int row, int col) {
        return tileModels[row][col];
    }
}
