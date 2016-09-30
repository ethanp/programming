package scrabble.model;

import javafx.scene.paint.Color;

/**
 * 9/29/16 10:24 PM
 */
public class TileModel {
    final int row;
    final int col;
    private final Bonus bonus;
    private final BoardModel boardModel;
    private LetterModel occupantLetterModel;

    public TileModel(BoardModel boardModel, Bonus bonus, int row, int col) {
        this.boardModel = boardModel;
        this.bonus = bonus;
        this.row = row;
        this.col = col;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public Color getColor() {
        if (occupantLetterModel != null) {
            return Color.LIGHTYELLOW;
        } else if (bonus != null) {
            return bonus.color;
        } else {
            return Color.BURLYWOOD;
        }
    }

    public LetterModel getOccupantLetterModel() {
        return occupantLetterModel;
    }

    public void setOccupantLetterModel(LetterModel occupantLetterModel) {
        this.occupantLetterModel = occupantLetterModel;
    }

    public void placeLetter(LetterModel letterModel) {
        if (occupantLetterModel != null) {
            System.err.println("there's already a letter there!");
        }
        occupantLetterModel = letterModel;
    }
}
