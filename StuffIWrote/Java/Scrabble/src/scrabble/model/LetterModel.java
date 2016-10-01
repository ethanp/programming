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

    public static LetterModel deserializeFromString(String serializedLetter) {
        String[] components = serializedLetter.split(",");
        char charLetter = components[0].charAt(0);
        int points = Integer.parseInt(components[1]);
        return new LetterModel(charLetter, points);
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

    /** just for general purpose printing, use serializeToString() for serialization */
    @Override public String toString() {
        return "LetterModel{" + "charLetter=" + charLetter + ", points=" + points + '}';
    }

    // NOTE: used for serialization
    public String serializeToString() {
        return String.format("%s,%d", charLetter, points);
    }
}
