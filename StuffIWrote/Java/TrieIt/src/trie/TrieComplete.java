package trie;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Ethan Petuchowski 5/23/15
 */
public class TrieComplete {
    String aliceLoc = "/Users/Ethan/Dropbox/CSyStuff/PrivateCode/Python"+
                      "/PreDraft_6_28_14/data/booktext/aliceinwonderland.txt";
    Trie trie = new Trie();
    class TrieElem {
        /* There could be some sort of "value" as a field here, but now there is no such thing. */
        boolean isTerminal;
        Map<Character, TrieElem> children = new HashMap<>();
        public boolean hasChild(char c) { return children.containsKey(c); }
        public TrieElem getChild(char c) {
            if (!hasChild(c)) children.put(c, new TrieElem());
            return children.get(c);
        }
        public Set<Map.Entry<Character, TrieElem>> nextLevel() {
            return children.entrySet();
        }
    }
    class Trie {
        TrieElem root = new TrieElem();
        public void add(String word) {
            if (word.isEmpty()) return;
            char[] letters = word.toLowerCase().toCharArray();
            TrieElem curr = root.getChild(letters[0]);
            for (int i = 1; i < letters.length; i++)
                curr = curr.getChild(letters[i]);
            curr.isTerminal = true;
        }
        public void printWordsWithPrefix(String prefix) {
            printWords("", prefix, root);
        }

        /** depth first printing */
        private void printWords(String soFar, String toGo, TrieElem curr) {
            if (toGo.isEmpty()) {
                if (curr.isTerminal) System.out.println(soFar);
                for (Map.Entry<Character, TrieElem> elem : curr.nextLevel())
                    printWords(soFar+elem.getKey(), toGo, elem.getValue());
            }
            else {
                char head = toGo.charAt(0);
                if (curr.hasChild(head))
                    printWords(soFar+head, toGo.substring(1), curr.getChild(head));
            }
        }
    }
    TrieComplete() throws FileNotFoundException {
        Iterable<String> words = parseAlice();
        for (String word : words) trie.add(word);
        console();
    }

    private void console() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("enter prefix and press enter: ");
            while (sc.hasNextLine()) {
                String inString = sc.nextLine();
                System.out.println("words with prefix "+inString+":");
                trie.printWordsWithPrefix(inString);
                System.out.print("enter prefix and press enter: ");
            }
            System.out.println("thank you and goodnight");
        }
    }

    private Iterable<String> parseAlice() throws FileNotFoundException {
        Set<String> words = new HashSet<>();
        Scanner sc = new Scanner(new File(aliceLoc));
        while (sc.hasNextLine())
            for (String word : sc.nextLine().split(" "))
                words.add(word.replaceAll("\\W", ""));
        return words;
    }

    public static void main(String[] args) throws FileNotFoundException { new TrieComplete(); }
}
