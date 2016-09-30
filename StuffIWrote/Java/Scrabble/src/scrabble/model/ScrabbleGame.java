package scrabble.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 9/29/16 9:21 PM
 */
public class ScrabbleGame {
    private final List<Player> players = new ArrayList<>();
    private final LetterBag letterBag = new LetterBag();
    private final Board board = new Board(this);

    void addPlayer(String name) {
        players.add(new Player(name, this));
    }

    void removePlayer(Player player) {
        players.remove(player);
    }

    public LetterBag getLetterBag() {
        return letterBag;
    }

    public void addLetterToBoard(Letter letter, int row, int col) {
        board.placeLetter(letter, row, col);
    }
}
