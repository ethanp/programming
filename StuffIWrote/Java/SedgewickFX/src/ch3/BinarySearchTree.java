package ch3;


import org.jetbrains.annotations.NotNull;

/**
 * started 6/30/16
 *
 * The point here is to do this before reading the Sedgewick chapter, to understand which parts are
 * "trivial" (subjectively), and which are not. I.e. when I read the chapter I should be able to
 * know what I'm actually learning vs. what is actually just review material.
 */
public class BinarySearchTree<Key extends Comparable<Key>, Value> implements SymbolTable<Key, Value> {

    private Node root;
    private int size = 0;

    public static void main(String[] args) {
        BinarySearchTree<Integer, Integer> bst = basicTree();
        System.out.println(bst.prettyString());
        System.out.println("--------");
        bst = basicTree();
        bst.delete(4);
        System.out.println(bst.prettyString());
    }

    @NotNull private static BinarySearchTree<Integer, Integer> basicTree() {
        BinarySearchTree<Integer, Integer> bst = new BinarySearchTree<>();
        bst.put(1, 1);
        bst.put(2, 2);
        bst.put(0, 0);
        bst.put(4, 4);
        bst.put(3, 3);
        return bst;
    }

    /**
     * put key-value pair into the table (remove key from table if value is null)
     */
    @Override public void put(Key key, Value val) {
        Node newNode = new Node(key, val);
        Node found = findByKeyOrElseParent(key);
        if (found == null) {
            root = newNode;
        } else if (newNode.equals(found)) {
            //noinspection UnnecessaryReturnStatement
            return;
        } else if (newNode.compareTo(found) < 0) {
            found.left = newNode;
            newNode.parent = found;
        } else {
            found.right = newNode;
            newNode.parent = found;
        }
        size++;
    }

    private String prettyString() {
        return root == null ? "(empty tree)" : root.prettyString();
    }

    /**
     * value paired with key (null if key is absent)
     */
    @Override public Value get(Key key) {
        Node found = findByKeyOrElseParent(key);
        if (found == null || !found.key.equals(key))
            return null;
        else
            return found.value;
    }

    private Node findByKeyOrElseParent(Key key) {
        if (isEmpty()) return null;
        Node curr = root;
        Node newNode = new Node(key, null);
        while (true) {
            if (newNode.equals(curr)) {
                return curr;
            } else if (newNode.compareTo(curr) < 0) {
                if (curr.left != null) {
                    curr = curr.left;
                } else {
                    return curr;
                }
            } else {
                if (curr.right != null) {
                    curr = curr.right;
                } else {
                    return curr;
                }
            }
        }
    }

    /**
     * remove key (and its value) from table
     */
    @Override public void delete(Key key) {
        Node found = findByKeyOrElseParent(key);
        // key doesn't exist, nothing to do
        if (found == null || !found.key.equals(key))
            return;
        size--;
        delete(found);
    }

    /** NB: this method does NOT change the symbol-table's `size` property */
    private void delete(Node found) {
        /* case 0 or 1 children => REPLACE with child node */
        if (found.left == null || found.right == null) {
            // `child` == null if `found` has no children
            Node child = found.left != null ? found.left : found.right;
            if (found == root) {
                root = child;
            } else if (found.isLeftChild()) {
                found.parent.left = child;
            } else if (found.isRightChild()) {
                found.parent.right = child;
            }
            return;
        }

        /* case 2 children => REPLACE with successor (or predecessor) */
        // we know `succ` is a DESCENDANT of `found`
        // and that it has at most one [right!] child
        // ... and that became way simpler than I expected
        Node succ = successor(found);
        found.replaceWith(succ);
        delete(succ);
    }

    private Node successor(Node node) {
        // first try min of right child
        if (node.right != null) {
            return node.right.minChild();
        }
        // otw find closest larger ancestor
        Node trav = node;  // reassignment is not strictly necessary
        while (trav.parent != null && trav.isLargerThanParent()) {
            trav = trav.parent;
        }
        return trav.parent;  // null if trav == root
    }

    /**
     * is there a value paired with key?
     */
    @Override public boolean contains(Key key) {
        return get(key) != null;
    }

    /** is the table empty? */
    @Override public boolean isEmpty() {
        return size == 0;
    }

    /** number of key-value pairs */
    @Override public int size() {
        return size;
    }

    /** smallest key */
    @Override public Key min() {
        Node minNode = minNode();
        return minNode == null ? null : minNode.key;
    }

    private Node minNode() {
        return isEmpty() ? null : root.minChild();
    }

    /** largest key */
    @Override public Key max() {
        Node maxNode = maxNode();
        return maxNode == null ? null : maxNode.key;
    }

    private Node maxNode() {
        return isEmpty() ? null : root.maxChild();
    }

    /**
     * largest key less than or equal to key
     */// TODO
    @Override public Key floor(Key key) {
        Node found = findByKeyOrElseParent(key);
        if (found == null || !found.key.equals(key))
            return null;
        return null;
    }

    /**
     * smallest key greater than or equal to key
     */// TODO
    @Override public Key ceiling(Key key) {
        Node found = findByKeyOrElseParent(key);
        if (found == null || !found.key.equals(key))
            return null;
        return null;
    }

    /**
     * number of keys less than key
     */// TODO
    @Override public int rank(Key key) {
        Node found = findByKeyOrElseParent(key);
        if (found == null || !found.key.equals(key))
            return -1;
        return -1;
    }

    /**
     * key of rank k
     */// TODO
    @Override public Key select(int k) {
        if (isEmpty()) return null;
        return null;
    }

    /** delete smallest key */
    @Override public void deleteMin() {
        delete(min());
    }

    /** delete largest key */
    @Override public void deleteMax() {
        delete(max());
    }

    /**
     * number of keys in [lo..hi]
     */// TODO
    @Override public int size(Key lo, Key hi) {
        if (isEmpty()) return 0;
        return 0;
    }

    /**
     * keys in [lo..hi], in sorted order
     */// TODO
    @Override public Iterable<Key> keys(Key lo, Key hi) {
        if (isEmpty()) return null;
        return null;
    }

    /** all keys in the table, in sorted order */// TODO
    @Override public Iterable<Key> keys() {
        if (isEmpty()) return null;
        return null;
    }

    private class Node implements Comparable<Node> {

        Key key;
        Value value;
        Node left;
        Node right;
        Node parent;

        Node(Key key, Value val) {
            this.key = key;
            this.value = val;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return key.equals(node.key);
        }

        @Override public int hashCode() {
            return key.hashCode();
        }

        @Override public int compareTo(@NotNull Node o) {
            return key.compareTo(o.key);
        }

        private StringBuilder stringAtLevel(int level) {

            StringBuilder leftSubtree = left == null
                ? new StringBuilder()
                : left.stringAtLevel(level + 1);

            StringBuilder rightSubtree = right == null
                ? new StringBuilder()
                : right.stringAtLevel(level + 1);

            return rightSubtree
                .append(indentation(level))
                .append(toString())
                .append("\n")
                .append(leftSubtree);
        }

        @Override public String toString() {
            return "{" + key + ", " + value + '}';
        }

        private StringBuilder indentation(int level) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < level; i++) sb.append("  ");
            return sb;
        }

        String prettyString() {
            return stringAtLevel(0).toString();
        }

        Node minChild() {
            return left == null ? this : left.minChild();
        }

        Node maxChild() {
            return right == null ? this : right.maxChild();
        }

        boolean isLeftChild() {
            return parent != null && parent.left == this;
        }

        boolean isRightChild() {
            return parent != null && parent.right == this;
        }

        boolean isSmallerThanParent() {
            return isLeftChild();
        }

        boolean isLargerThanParent() {
            return isRightChild();
        }

        void replaceWith(Node node) {
            key = node.key;
            value = node.value;
        }
    }
}
