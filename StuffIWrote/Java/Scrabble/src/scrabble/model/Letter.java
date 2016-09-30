package scrabble.model;

/**
 * 9/29/16 10:27 PM
 */
public class Letter {
    public final char charLetter;
    public final int points;

    public Letter(char charLetter, int points) {
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
        Letter letter = (Letter) o;
        return charLetter == letter.charLetter;

    }
}
