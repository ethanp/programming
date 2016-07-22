package ch5;

/**
 * 7/22/16 10:42 AM
 *
 * Sedgie pg. 746. Improvement over basic Trie for String-key symbol table when word length ("w")
 * is long or alphabet size ("R") is large.
 */
public class TernarySearchTree {
    TernarySearchTree left;
    TernarySearchTree center;
    TernarySearchTree right;
    char c;
    int value;

    public static void main(String[] args) {
        TernarySearchTree tst = new TernarySearchTree();
        tst.insert("she", 1);
        tst.insert("sells", 2);
        tst.insert("sea", 3);
        tst.insert("shells", 4);
        tst.insert("by", 5);
        tst.insert("the", 6);
        tst.insert("sea", 7);
        tst.insert("shore", 8);
        int sea = tst.get("sea");
        System.out.println(sea);
        System.out.println(sea == 7);
        System.out.println(tst.get("she") == 1);
        System.out.println(tst.get("by") == 5);
        System.out.println(tst.get("the") == 6);
        String sells = tst.longestPrefixOf("sells_p");
        System.out.println(sells);
        System.out.println(sells.equals("sells"));
    }

    private String longestPrefixOf(String string) {
        if (c == 0 || string.isEmpty()) return "";
        else if (c == string.charAt(0)) {
            if (string.length() == 1 || center == null) return ""+c;
            return c + center.longestPrefixOf(string.substring(1));
        } else if (c > string.charAt(0)) {
            if (left == null) return "";
            else return left.longestPrefixOf(string);
        } else if (right == null) return "";
        else return right.longestPrefixOf(string);
    }

    private int get(String string) {
        if (c == 0) return -1;
        else if (string.charAt(0) == c)
            if (string.length() == 1) return value;
            else if (center == null) return -1;
            else return center.get(string.substring(1));
        else if (string.charAt(0) < c)
            if (left == null) return -1;
            else return left.get(string);
        else if (right == null) return -1;
        else return right.get(string);
    }

    private void insert(String string, int val) {
        if (c == 0 || c == string.charAt(0)) {
            c = string.charAt(0);
            if (string.length() == 1) value = val;
            else {
                if (center == null) center = new TernarySearchTree();
                center.insert(string.substring(1), val);
            }
        } else if (c > string.charAt(0)) {
            if (left == null) left = new TernarySearchTree();
            left.insert(string, val);
        }
        else {
            if (right == null) right = new TernarySearchTree();
            right.insert(string, val);
        }
    }
}
