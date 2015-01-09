package algorithms.strings.moderate;

import java.util.Scanner;

/**
 * Ethan Petuchowski 1/8/15
 *
 * TODO this one seems quite tough. I'm'a gonna skip it for now
 */
public class StringModification {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int reqdLen = in.nextInt();
        int minVal = in.nextInt();
        in.next();
        String S = in.nextLine();
        final int MOD_VAL = 1_000_003;

    }

    static int stringValue(String str) {
        int acc = 0;
        for (char c : str.toCharArray())
            acc += c;
        return acc;
    }
}
