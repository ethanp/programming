package algorithms.strings.easy;

import java.util.Scanner;

/**
 * Ethan Petuchowski 7/18/15
 */
public class Funny {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int T = sc.nextInt(); sc.nextLine(/*ugh*/);
        for (int i = 0; i < T; i++) {
            String s = sc.nextLine();
            new Funny(s);
        }
    }
    String s;
    Funny(String st) {
        s = st;
        for (int b = 0, e = s.length()-2; b < e; b++, e--) {
            if (diffAt(b) != diffAt(e)) {
                System.out.println("Not Funny");
                return;
            }
        }
        System.out.println("Funny");
    }

    int diffAt(int idx) {
        return Math.abs(s.charAt(idx)-s.charAt(idx+1));
    }
}
