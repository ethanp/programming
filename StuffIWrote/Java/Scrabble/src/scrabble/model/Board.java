package scrabble.model;

import scrabble.view.ScrabbleBoard;

/**
 * 9/29/16 10:24 PM
 */
public class Board {
    Square[][] squares = new Square[ScrabbleBoard.NUM_ROWS][ScrabbleBoard.NUM_COLS];
    private ScrabbleGame game;

    Board(ScrabbleGame game) {
        this.game = game;
        SpecialSquareInputs specialSquareInputs = SpecialSquareInputs.readFromConfig();
        assert specialSquareInputs != null;
        RowsAndColumns.each((row, col) -> squares[row][col] =
              new Square(this, specialSquareInputs.get(row, col), row, col));
    }

    public void placeLetter(Letter letter, int row, int col) {
        squares[row][col].placeLetter(letter);
    }
}
