package algorithms.arraysAndSorting.easy;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Ethan Petuchowski 12/22/14
 */
public class MakeAnagram {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        char[] A = in.nextLine().toCharArray();
        char[] B = in.nextLine().toCharArray();
        System.out.println(symmetricDifference(makeCounter(A), makeCounter(B)));
    }

    static int symmetricDifference(
            Map<Character, Integer> a,
            Map<Character, Integer> b)
    {
        int diff = 0;
        for (Map.Entry<Character, Integer> entry : a.entrySet()) {
            char k = entry.getKey();
            int v = entry.getValue();
            if (b.containsKey(k)) {
                diff += Math.abs(v-b.get(k));
            } else {
                diff += v;
            }
        }
        for (Map.Entry<Character, Integer> entry : b.entrySet()) {
            char k = entry.getKey();
            int v = entry.getValue();
            if (!a.containsKey(k)) {
                diff += v;
            }
        }
        return diff;
    }

    static Map<Character, Integer> makeCounter(char[] arr) {
        Map<Character, Integer> toRet = new HashMap<>();
        for (char c : arr) {
            if (!toRet.containsKey(c))
                toRet.put(c, 0);
            toRet.put(c, toRet.get(c)+1);
        }
        return toRet;
    }
}
