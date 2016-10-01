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
    private static Random random = new Random();
    private final List<LetterModel> availableLetterModels = new ArrayList<>();

    LetterBag() {
        try (FileReader fileReader = new FileReader("src/scrabble/model/letterConfig.csv");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] csvLine = line.split(",");
                int count = Integer.parseInt(csvLine[1]);
                for (int i = 0; i < count; i++) {
                    char letter = csvLine[0].equalsIgnoreCase("blank") ? ' ' : csvLine[0].charAt(0);
                    int points = Integer.parseInt(csvLine[2]);
                    availableLetterModels.add(new LetterModel(letter, points));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    LetterRack drawInitialSet() {
        return new LetterRack(this);
    }

    LetterModel drawLetter() {
        int randomIndex = random.nextInt(availableLetterModels.size());
        return availableLetterModels.remove(randomIndex);
    }

    boolean isEmpty() {
        return availableLetterModels.isEmpty();
    }
}
