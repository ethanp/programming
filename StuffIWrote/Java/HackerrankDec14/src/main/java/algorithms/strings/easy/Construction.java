package algorithms.strings.easy;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 8/10/16 10:39 AM
 */
public class Construction {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        for(int i = 0; i < n; i++){
            String s = in.next();
            Set<Character> set = new HashSet<>();
            for (char c : s.toCharArray()) set.add(c);
            System.out.println(set.size());
        }
    }
}
