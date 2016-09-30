package scrabble.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 9/29/16 10:27 PM
 */
class LetterRack {
    private final List<Letter> letters = new ArrayList<>();
    private final LetterBag letterBag;

    public LetterRack(LetterBag letterBag) {
        this.letterBag = letterBag;
        for (int i = 0; i < 7; i++) {
            letters.add(letterBag.drawLetter());
        }
    }

    public boolean contains(Letter l) {
        return letters.contains(l);
    }

    public void remove(Letter l) {
        letters.remove(l);
    }
}
