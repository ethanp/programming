package scrabble.model;

import java.util.function.BiConsumer;

/**
 * 9/29/16 10:24 PM
 */
class Board {
    Square[][] squares = new Square[10][10];
    RowsAndColumns rowsAndColumns = new RowsAndColumns();
    private ScrabbleGame game;

    Board(ScrabbleGame game) {
        this.game = game;
        SpecialSquareInputs specialSquareInputs = SpecialSquareInputs.readFromConfig();
        assert specialSquareInputs != null;
        rowsAndColumns.each((Integer row, Integer col) -> squares[row][col] =
              new Square(this, specialSquareInputs.get(row, col), row, col));
    }

    public void placeLetter(Letter letter, int row, int col) {
        squares[row][col].placeLetter(letter);
    }

    private class RowsAndColumns {
        public void each(BiConsumer<Integer, Integer> biFunction) {
            for (int row = 0; row < squares.length; row++) {
                for (int col = 0; col < squares[0].length; col++) {
                    biFunction.accept(row, col);
                }
            }
        }
    }
}
