package algorithms.arraysAndSorting.easy;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/**
 * Ethan Petuchowski 12/22/14
 */
public class CutTheSticks {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int count = in.nextInt();
        NavigableMap<Integer, Integer> alive = new TreeMap<>();
        for (int c = 0; c < count; c++) {
            int next = in.nextInt();
            if (!alive.containsKey(next))
                alive.put(next, 0);
            alive.put(next, alive.get(next)+1);
        }
        System.out.println(count);
        while (alive.size() > 1) {
            int smallest = alive.firstKey();
            alive.higherKey(smallest);
            alive.remove(smallest);
            int sum = 0;
            Set<Map.Entry<Integer, Integer>> entries = alive.entrySet();
            NavigableMap<Integer, Integer> cpd = new TreeMap<>();
            for (Map.Entry<Integer, Integer> entry : entries) {
                int value = entry.getValue();
                sum += value;
                cpd.put(entry.getKey()-smallest, value);
            }
            alive = cpd;
            System.out.println(sum);
        }
    }
}
