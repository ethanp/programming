package algorithms.strings.moderate;

import java.util.Scanner;

/**
 * Ethan Petuchowski 7/18/15
 */
public class SherlockAnagrams {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int T = Integer.parseInt(sc.nextLine());
        for (int t = 0; t < T; t++) new SherlockAnagrams();
    }
    SherlockAnagrams() {
        String s = sc.nextLine();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            for (int j = i+1; j < s.length(); j++) {
                char d = s.charAt(j);
                // TODO I've got an idea yet for how to do this
                // I require a hint from the hinternet
            }
        }
    }
}
