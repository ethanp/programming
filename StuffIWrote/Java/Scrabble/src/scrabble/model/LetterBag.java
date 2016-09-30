package scrabble.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 9/29/16 10:28 PM
 */
class LetterBag {
    static Random random = new Random();
    private final List<Letter> availableLetters = new ArrayList<>();

    LetterBag() {
        try (FileReader fileReader = new FileReader("letterConfig.csv");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] csvLine = line.split(",");
                int count = Integer.parseInt(csvLine[1]);
                for (int i = 0; i < count; i++) {
                    char letter = csvLine[0].charAt(0);
                    int points = Integer.parseInt(csvLine[2]);
                    availableLetters.add(new Letter(letter, points));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public LetterRack drawInitialSet() {
        return new LetterRack(this);
    }

    public Letter drawLetter() {
        int randomIndex = random.nextInt(availableLetters.size());
        return availableLetters.remove(randomIndex);
    }
}
