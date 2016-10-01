package scrabble.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 * 9/29/16 10:24 PM
 */
public class TileModel {
    public final int row;
    public final int col;
    private final Bonus bonus;
    private final BoardModel boardModel;
    private ObjectProperty<LetterModel> occupantLetterModel = new SimpleObjectProperty<>(null);

    public TileModel(BoardModel boardModel, Bonus bonus, int row, int col) {
        this.boardModel = boardModel;
        this.bonus = bonus;
        this.row = row;
        this.col = col;
    }

    public BoardModel getBoardModel() {
        return boardModel;
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
        return occupantLetterModel.get();
    }

    public void placeLetter(LetterModel letterModel) {
        if (occupantLetterModel.get() != null) {
            System.err.println("there's already a letter there!");
            return;
        }
        occupantLetterModel.set(letterModel);
    }

    public void removeLetter() {
        if (occupantLetterModel.get() == null) {
            System.err.printf("there's no letter here row=%d col=%d%n", row, col);
            return;
        }
        occupantLetterModel.set(null);
    }
}
