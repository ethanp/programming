package algorithms.arraysAndSorting.tutorial;

import java.io.IOException;
import java.util.Scanner;

/**
 * Ethan Petuchowski 12/16/14
 */
public class SampleChallenge {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        int V = in.nextInt();
        int n = in.nextInt();
        for (int i = 0; i < n; i++) {
            int j = in.nextInt();
            if (j == V) {
                System.out.println(i);
                return;
            }
        }
    }
}
