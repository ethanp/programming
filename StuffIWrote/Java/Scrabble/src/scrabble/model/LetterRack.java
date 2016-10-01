package scrabble.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 9/29/16 10:27 PM
 */
public class LetterRack implements Iterable<LetterModel> {
    private final List<LetterModel> letterModels = new ArrayList<>();
    private final LetterBag letterBag;

    LetterRack(LetterBag letterBag) {
        this.letterBag = letterBag;
        for (int i = 0; i < 7; i++) {
            letterModels.add(letterBag.drawLetter());
        }
    }

    boolean contains(LetterModel l) {
        return letterModels.contains(l);
    }

    void remove(LetterModel l) {
        letterModels.remove(l);
    }

    @Override public Iterator<LetterModel> iterator() {
        return letterModels.iterator();
    }
}
