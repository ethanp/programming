package ch5;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 7/21/16 12:05 PM
 */
public class LsdRadixSort {
    static final int NUM_LETTERS = 26;
    static final int NUM_NUMERALS = 10;
    static final int NUM_ALPHANUM = NUM_LETTERS + NUM_NUMERALS;

    public static void main(String[] args) {
        String[] stages = {
            "4PGC938 2IYE230 3CIO720 2IYE230 2RLA629 1ICK750 3ATW723 1ICK750",
            "2IYE230 3CIO720 3CIO720 4JZY524 2RLA629 1ICK750 3CIO720 1ICK750",
            "3CIO720 1ICK750 3ATW723 2RLA629 4PGC938 4PGC938 3CIO720 1OHV845",
            "1ICK750 1ICK750 4JZY524 2RLA629 2IYE230 1OHV845 1ICK750 1OHV845",
            "1OHV845 3CIO720 2RLA629 3CIO720 1ICK750 1OHV845 1ICK750 1OHV845",
            "4JZY524 3ATW723 2RLA629 3CIO720 1ICK750 1OHV845 2IYE230 2IYE230",
            "1ICK750 4JZY524 2IYE230 3ATW723 3CIO720 3CIO720 4JZY524 2RLA629",
            "3CIO720 1OHV845 4PGC938 1ICK750 3CIO720 3CIO720 1OHV845 2RLA629",
            "1OHV845 1OHV845 1OHV845 1ICK750 1OHV845 2RLA629 1OHV845 3ATW723",
            "1OHV845 1OHV845 1OHV845 1OHV845 1OHV845 2RLA629 1OHV845 3CIO720",
            "2RLA629 4PGC938 1OHV845 1OHV845 1OHV845 3ATW723 4PGC938 3CIO720",
            "2RLA629 2RLA629 1ICK750 1OHV845 3ATW723 2IYE230 2RLA629 4JZY524",
            "3ATW723 2RLA629 1ICK750 4PGC938 4JZY524 4JZY524 2RLA629 4PGC938"
        };

        String[][] splitStages = Arrays.stream(stages)
            .map(s -> s.split(" "))
            .collect(Collectors.toList())
            .toArray(new String[stages.length][stages[0].split(" ").length]);

        String[] input = getStage(splitStages, 0);


        for (int stageNum = 0; stageNum < input[0].length(); stageNum++) {
            String[] res = runForStage(input, stageNum);
            String[] expectedRes = getStage(splitStages, stageNum + 1);
            boolean correct = Arrays.equals(res, expectedRes);
            String correctness = correct ? "correct" : "incorrect";
            System.out.println("stage " + stageNum + " is " + correctness);
        }
    }

    private static String[] runForStage(String[] input, int stageNum) {
        final int inputCount = input.length;

        // get counts (index 0 is always zero for convenience in the start-indices calculation)
        int[] counts = new int[NUM_ALPHANUM+1];
        for (int str = 0; str < inputCount; str++) {
            int stageIdx = input[str].length() - 1 - stageNum;
            char c = input[str].charAt(stageIdx);
            counts[charIdx(c)+1]++;
        }

        // get start indices
        for (int str = 0; str < counts.length-1; str++)
            counts[str + 1] += counts[str];

        // partition into aux
        String[] aux = new String[inputCount];
        for (int str = 0; str < inputCount; str++) {
            String thisString = input[str];
            int stageIdx = input[str].length() - 1 - stageNum;
            char c = thisString.charAt(stageIdx);
            int countsIdx = charIdx(c);
            int auxIdx = counts[countsIdx]++;
            aux[auxIdx] = thisString;
        }

        // copy back to input arr
        for (int str = 0; str < inputCount; str++)
            input[str] = aux[str];

        return input;
    }

    private static int charIdx(char c) {
        if (c >= '0' && c <= '9') return c - '0';
        else if (c >= 'A' && c <= 'Z') return (c - 'A') + 10;
        else throw new IllegalArgumentException(c + " is not a valid input char");
    }

    static String[] getStage(String[][] splitStages, int stageNum) {
        return Arrays.stream(splitStages)
            .map(s -> s[stageNum])
            .collect(Collectors.toList())
            .toArray(new String[splitStages.length]);
    }
}
