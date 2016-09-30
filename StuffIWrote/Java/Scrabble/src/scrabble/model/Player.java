package scrabble.model;

/**
 * 9/29/16 10:27 PM
 */
class Player {

    private final ScrabbleGame game;
    private final String name;
    private final LetterRack letterRack;

    public Player(String name, ScrabbleGame game) {
        this.name = name;
        this.game = game;
        letterRack = game.getLetterBag().drawInitialSet();
    }

    void playLetterAtSquare(Letter letter, int row, int col) {
        if (!letterRack.contains(letter)) {
            System.err.println("you don't have the letter " + letter + ". cancelling");
            return;
        }
        letterRack.remove(letter);
        game.addLetterToBoard(letter, row, col);
    }
}
