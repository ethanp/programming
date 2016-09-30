package scrabble.model;

/**
 * 9/29/16 10:27 PM
 */
public class LetterModel {
    public final char charLetter;
    public final int points;

    public LetterModel(char charLetter, int points) {
        this.charLetter = charLetter;
        this.points = points;
    }

    @Override public int hashCode() {
        int result = (int) charLetter;
        result = 31*result + points;
        return result;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LetterModel letterModel = (LetterModel) o;
        return charLetter == letterModel.charLetter;

    }
}
