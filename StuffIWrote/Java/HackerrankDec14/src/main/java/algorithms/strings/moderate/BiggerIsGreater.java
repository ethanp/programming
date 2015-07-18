package algorithms.strings.moderate;

import java.util.Arrays;
import java.util.NavigableSet;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Ethan Petuchowski 7/18/15
 */
public class BiggerIsGreater {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int T = Integer.parseInt(sc.nextLine());
        for (int test = 0; test < T; test++) {
            new BiggerIsGreater();
        }
    }
    BiggerIsGreater() {
        String s = sc.nextLine();
        NavigableSet<Character> set = new TreeSet<>();
        char[] arr = s.toCharArray();
        for (char c : arr) set.add(c);
        if (set.size() <= 1) {
            System.out.println("no answer");
            return;
        }
        for (int i = s.length()-1; i >= 0; i--) {
            NavigableSet<Character> innerSet = new TreeSet<>();
            for (char c : s.substring(i+1).toCharArray()) innerSet.add(c);
            Character c = innerSet.higher(arr[i]);
            if (c == null) continue;
            for (int j = s.length()-1; j > i; j--) {
                if (arr[j] == c) {
                    char tmp = arr[j];
                    arr[j] = arr[i];
                    arr[i] = tmp;
                    Arrays.sort(arr, i+1, arr.length);
                    System.out.println(new String(arr));
                    return;
                }
            }
        }
        System.out.println("no answer");
    }
}
