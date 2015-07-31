package algorithms.strings.moderate;

import java.util.Scanner;

/**
 * Ethan Petuchowski 7/18/15
 *
 * My answer in general is a (much cleaner) version of the one given in
 *
 *      http://stackoverflow.com/questions/29191565
 *
 * I'm going to check for anagrams the way they suggest at
 *
 *      http://www.geeksforgeeks.org/anagram-substring-search-search-permutations
 *
 * I'm going to take every substring length from 1 to n-1 and slide over the
 * rest of the string looking for anagrams, then slide over the base substring
 * and check again.
 *
 * The trick is in the way we "slide", which is accomplished using the Counts.next() method
 * and the way we check for anagrams, as shown in the geeksforgeeks tutorial above.
 */
public class SherlockAnagrams {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int T = Integer.parseInt(sc.nextLine());
        for (int t = 0; t < T; t++)
            new SherlockAnagrams();
    }
    int count = 0;
    String s;
    SherlockAnagrams() {
        s = sc.nextLine();
        for (int len = 1; len < s.length(); len++) {
            Counts aCounts = new Counts(0, len);
            for (int aFirst = 0; aFirst+len < s.length(); aFirst++, aCounts.next()) {
                Counts bCounts = new Counts(aFirst+1, len);
                for (int bFirst = aFirst+1; bFirst+len < s.length(); bFirst++, bCounts.next())
                    aCounts.countIfAnagramOf(bCounts);
                aCounts.countIfAnagramOf(bCounts);
            }
        }
        System.out.println(count);
    }

    class Counts {
        final short[] counts = new short[26];
        int start;
        final int len;
        Counts(int start, int len) {
            this.start = start;
            this.len = len;
            for (int i = start; i < start+len; i++) {
                counts[s.charAt(i)-'a']++;
            }
        }
        Counts next() {
            counts[s.charAt(start)-'a']--;
            counts[s.charAt(start+len)-'a']++;
            start++;
            return this;
        }
        void countIfAnagramOf(Counts o) {
            assert len == o.len : "incorrect lens";
            for (int i = 0; i < counts.length; i++)
                if (counts[i] != o.counts[i])
                    return;
            count++;
        }
    }
}
