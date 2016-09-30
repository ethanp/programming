package scrabble.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 9/29/16 10:27 PM
 */
class LetterRack {
    private final List<LetterModel> letterModels = new ArrayList<>();
    private final LetterBag letterBag;

    public LetterRack(LetterBag letterBag) {
        this.letterBag = letterBag;
        for (int i = 0; i < 7; i++) {
            letterModels.add(letterBag.drawLetter());
        }
    }

    public boolean contains(LetterModel l) {
        return letterModels.contains(l);
    }

    public void remove(LetterModel l) {
        letterModels.remove(l);
    }
}
