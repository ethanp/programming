package dataStructures.trie;

import java.util.Scanner;

/**
 * Ethan Petuchowski 8/29/15
 *
 * There are two cases (if we want to solve this in O(NM))
 *
 *      1) current string is a prefix of a previous string
 *          - which would happen if we terminate at an existing non-terminal
 *            in the trie
 *
 *      2) previous string is a prefix of current string
 *          - which would happen if we reach a terminal node while adding the
 *            current string to the trie
 */
public class NoPrefixSet {

    final static Scanner sc = new Scanner(System.in);

    static class Trie {
        boolean terminal;
        Trie[] next = new Trie['j'-'a'+1];

        /** @return true iff now we know it's a BAD SET */
        public boolean add(String s) {
            if (s.length() == 0) {
                for (Trie t : next) {
                    if (t != null) {
                        return true;
                    }
                }
                terminal = true;
            } else if (terminal) {
                return true;
            }
            return add(s, 0);
        }

        private boolean add(String s, int idx) {
            final Trie nextLevel = next[s.charAt(idx)-'a'];

            // case 1
            if (idx == s.length()-1 && nextLevel != null && !nextLevel.terminal) {
                return true;
            }

            // case 2
            if (nextLevel != null && nextLevel.terminal) {
                return true;
            }

            if (nextLevel == null) {
                next[s.charAt(idx)-'a'] = new Trie();
            }

            if (idx == s.length()-1) {
                next[s.charAt(idx)-'a'].terminal = true;
                return false;
            }

            else {
                return next[s.charAt(idx)-'a'].add(s, idx+1);
            }
        }
    }

    Trie base = new Trie();

    public NoPrefixSet(int count) {
        for (int i = 0; i < count; i++) {
            final String line = sc.nextLine();
            if (base.add(line)) {
                System.out.println("BAD SET\n"+line);
                return;
            }
        }
        System.out.println("GOOD SET");
    }

    public static void main(String[] args) {
        int count = Integer.parseInt(sc.nextLine());
        new NoPrefixSet(count);
    }
}
