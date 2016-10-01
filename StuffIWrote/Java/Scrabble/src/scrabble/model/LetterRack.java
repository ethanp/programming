package scrabble.model;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Iterator;

/**
 * 9/29/16 10:27 PM
 */
public class LetterRack implements Iterable<LetterModel> {
    private final ObservableList<LetterModel> letterModels = FXCollections.observableArrayList();
    private final LetterBag letterBag;

    LetterRack(LetterBag letterBag) {
        this.letterBag = letterBag;
        for (int i = 0; i < 7; i++) {
            letterModels.add(letterBag.drawLetter());
        }
    }

    public void registerChangeListener(ListChangeListener<LetterModel> listener) {
        letterModels.addListener(listener);
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

    public void addLetter(LetterModel letterModel) {
        letterModels.add(letterModel);
    }

    public void refillFromBag() {
        while (!letterBag.isEmpty() && letterModels.size() < 7) {
            letterModels.add(letterBag.drawLetter());
        }
    }
}
