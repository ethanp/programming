package algorithms.strings.easy;

import java.util.Scanner;

/**
 * 8/10/16 8:05 AM
 *
 * Given k and an n-digit number, help Sandy determine the
 * largest possible number she can make by changing <= k
 * digits.
 */
public class RichieRich {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.nextInt();
        int changesAllowed = sc.nextInt();
        sc.nextLine();
        char[] chars = sc.nextLine().toCharArray();
        int dist = palDist(chars);
        if (dist > changesAllowed) {
            // too far, not possible
            System.out.println("-1");
            return;
        }
        for (int i = 0; i <= chars.length/2 && changesAllowed > 0; i++) {
            int j = chars.length - 1 - i;
            if (changesAllowed > 1 && changesAllowed > dist) {
                // if at least one of the pair is not a 9
                // make both a 9
                // return the number of changes made
                if (chars[i] != chars[j]) {
                    // if they were already the same the dist won't change
                    dist--;
                }
                if (chars[i] != '9') {
                    chars[i] = '9';
                    changesAllowed--;
                    if (chars[j] != '9') {
                        chars[j] = '9';
                        changesAllowed--;
                    }
                } else if (chars[j] != '9') {
                    chars[j] = '9';
                    changesAllowed--;
                }
            } else if (chars[i] != chars[j]) {
                char max = (char) Math.max(chars[i], chars[j]);
                chars[i] = chars[j] = max;
                changesAllowed--;
            } else if (chars.length % 2 != 0 && i == chars.length/2) {
                chars[i] = '9';
                changesAllowed--;
            }
        }
        System.out.println(new String(chars));
    }

    private static int palDist(char[] chars) {
        int ctr = 0;
        for (int i = 0; i < chars.length/2; i++)
            if (chars[i] != chars[chars.length - 1 - i])
                ctr++;
        return ctr;
    }
}
