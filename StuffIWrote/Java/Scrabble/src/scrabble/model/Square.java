package scrabble.model;

/**
 * 9/29/16 10:24 PM
 */
class Square {
    final int row;
    final int col;
    private final Bonus bonus;
    private final BoardModel boardModel;
    private Letter occupantLetter;

    public Square(BoardModel boardModel, Bonus bonus, int row, int col) {
        this.boardModel = boardModel;
        this.bonus = bonus;
        this.row = row;
        this.col = col;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public Letter getOccupantLetter() {
        return occupantLetter;
    }

    public void placeLetter(Letter letter) {
        if (occupantLetter != null) {
            System.err.println("there's already a letter there!");
        }
        occupantLetter = letter;
    }
}
