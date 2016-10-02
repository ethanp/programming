package scrabble.view;

import javafx.scene.layout.HBox;
import scrabble.model.LetterModel;
import scrabble.model.ScrabbleGame;

import java.util.Iterator;

/**
 * 9/30/16 7:51 PM
 */
class LetterRackView extends HBox {

    private final ScrabbleGame scrabbleGame;

    LetterRackView(ScrabbleGame scrabbleGame) {
        this.scrabbleGame = scrabbleGame;
        setSpacing(3);
        renderLetters();

        // register for initial player
        scrabbleGame.getCurrentPlayer()
              .getLetterRack()
              .registerChangeListener(c -> renderLetters());

        // stay bound to (only) whomever the current player is
        scrabbleGame.addPlayerChangeListener((observable, oldValue, newValue) -> {
            oldValue.getLetterRack().removeChangeListener();
            renderLetters();
            newValue.getLetterRack().registerChangeListener(c -> renderLetters());
        });
    }

    private void renderLetters() {
        getChildren().clear();
        Iterator<LetterModel> it = scrabbleGame.getCurrentPlayer().getLetterRack().iterator();
        for (int i = 0; i < 7; i++) {
            LetterModel nextLetter = it.hasNext() ? it.next() : null;
            RackTileView tileView = new RackTileView(90, 90, nextLetter);
            getChildren().add(tileView);
        }
    }
}
