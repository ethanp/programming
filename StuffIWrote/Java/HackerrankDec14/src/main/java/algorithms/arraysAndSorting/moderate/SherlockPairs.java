package algorithms.arraysAndSorting.moderate;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Ethan Petuchowski 12/22/14
 */
public class SherlockPairs {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        for (int t = 0; t < T; t++) {
            int N = in.nextInt();
            Map<Integer, Integer> numToCt = new HashMap<>();
            for (int i = 0; i < N; i++) {
                int elem = in.nextInt();
                if (!numToCt.containsKey(elem))
                    numToCt.put(elem, 0);
                numToCt.put(elem, numToCt.get(elem)+1);
            }
            long sum = 0;
            for (int v : numToCt.values())
                for (int i = 1; i < v; i++)
                    sum += i;
            System.out.println(sum*2);
        }
    }
}
