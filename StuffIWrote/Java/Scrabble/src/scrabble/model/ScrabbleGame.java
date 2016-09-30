package scrabble.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 9/29/16 9:21 PM
 */
public class ScrabbleGame {
    private final List<Player> players = new ArrayList<>();
    private final LetterBag letterBag = new LetterBag();
    private final BoardModel boardModel = new BoardModel(this);
    private Player currentPlayer;

    public ScrabbleGame() {
        addPlayer("1st player");
    }

    void addPlayer(String name) {
        Player player = new Player(name, this);
        players.add(player);
        if (currentPlayer == null) {
            currentPlayer = player;
        }
    }

    void removePlayer(Player player) {
        players.remove(player);
    }

    public LetterBag getLetterBag() {
        return letterBag;
    }

    public void addLetterToBoard(LetterModel letterModel, int row, int col) {
        boardModel.placeLetter(letterModel, row, col);
    }

    public BoardModel getBoardModel() {
        return boardModel;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
