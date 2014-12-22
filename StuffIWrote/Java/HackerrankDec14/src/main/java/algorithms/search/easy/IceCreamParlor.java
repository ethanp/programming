package algorithms.search.easy;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Ethan Petuchowski 12/20/14
 */
public class IceCreamParlor {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int numCases = in.nextInt();
        for (int caseNum = 0; caseNum < numCases; caseNum++) {
            int dollars = in.nextInt();
            int[] costIndices = new int[dollars];
            boolean done = false;
            Arrays.fill(costIndices, -1);
            int numFlavors = in.nextInt();
            for (int i = 0; i < numFlavors; i++) {
                int cost = in.nextInt();
                if (!done && cost < dollars) {
                    int lowerIdx = costIndices[dollars-cost];
                    if (lowerIdx >= 0) {
                        System.out.println((lowerIdx+1)+" "+(i+1));
                        done = true;
                    }
                    costIndices[cost] = i;
                }
            }
        }
    }
}
