package otherDataStructures;

import java.util.Iterator;
import java.util.Stack;

/**
 * 9/24/16 10:01 PM
 *
 * It's a Sorted Set of ints.
 *
 * This was my second try at implementing this thing.
 * It's not well-tested, but even if it's not perfect, it's darn close.
 */
public class AVLTree {

    private Node root;

    public static void main(String[] args) {
        AVLTree avlTree = new AVLTree();
        avlTree.add(1);
        avlTree.add(2);
        avlTree.add(3);
        avlTree.remove(3);
        avlTree.iterator().forEachRemaining(System.out::println);
        System.out.println(avlTree.contains(2) + " should be true");
        System.out.println(avlTree.contains(3) + " should be false");
    }

    public boolean contains(int i) {
        return !isEmpty() && root.get(i) != null;
    }

    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            Stack<Node> nodeStack = new Stack<>();

            {
                for (Node curr = root; curr != null; curr = curr.left) {
                    nodeStack.add(curr);
                }
            }

            @Override public boolean hasNext() {
                return !nodeStack.isEmpty();
            }

            // too tired to check if this makes sense, but I like it
            @Override public Integer next() {
                Node next = nodeStack.pop();
                if (!nodeStack.isEmpty()) {
                    for (Node curr = nodeStack.peek().right; curr != null; curr = curr.left) {
                        nodeStack.push(curr);
                    }
                }
                return next.value;
            }
        };
    }

    public int size() {
        return isEmpty() ? 0 : root.size();
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void remove(int i) {
        if (isEmpty()) {
            return;
        }
        root = root.remove(i);
    }

    public void add(Integer integer) {
        root = isEmpty()
              ? new Node(integer)
              : root.insert(integer);
    }

    private static class Node {
        int value, height;
        Node left, right;

        Node(int value) {
            this.value = value;
            this.height = 1;
        }

        public int size() {
            return 1
                  + (left == null ? 0 : left.size())
                  + (right == null ? 0 : right.size());
        }

        public Node insert(int i) {
            if (i == value) return this;
            else if (i < value) return insertLeft(i);
            else /* i > value*/ return insertRight(i);
        }

        private Node insertLeft(int i) {
            if (left == null) {
                left = new Node(i);
                height = Math.max(height, 2);
                return this;
            }
            left = left.insert(i);
            return rebalance();
        }

        private Node insertRight(int i) {
            if (right == null) {
                right = new Node(i);
                height = Math.max(height, 2);
                return this;
            }
            right = right.insert(i);
            return rebalance();
        }

        private Node rebalance() {
            recalculateHeight();
            if (leftHeight() - rightHeight() > 1) {
                assert left != null;
                if (left.rightHeight() > left.leftHeight()) {
                    left = left.rotateLeft();
                }
                return rotateRight();
            } else if (rightHeight() - leftHeight() > 1) {
                assert right != null;
                if (right.leftHeight() > right.rightHeight()) {
                    right = right.rotateRight();
                }
                return rotateLeft();
            }
            return this;
        }

        @SuppressWarnings("SuspiciousNameCombination")
        private Node rotateLeft() {
            assert right != null;

            Node movedToTop = right;
            Node tmp = right.left;

            right.left = this;
            right = tmp;

            recalculateHeight();
            movedToTop.recalculateHeight();

            return movedToTop;
        }

        @SuppressWarnings("SuspiciousNameCombination")
        private Node rotateRight() {
            assert left != null;

            Node movedToTop = left;
            Node tmp = left.right;

            left.right = this;
            left = tmp;

            recalculateHeight();
            movedToTop.recalculateHeight();

            return movedToTop;
        }

        private void recalculateHeight() {
            height = Math.max(leftHeight(), rightHeight()) + 1;
        }

        private int leftHeight() {
            return left == null ? 0 : left.height;
        }

        private int rightHeight() {
            return right == null ? 0 : right.height;
        }

        /** if the node doesn't exist, this method has no effect */
        public Node remove(int i) {
            if (i == value) {
                if (left == null && right == null) {
                    return null;
                } else if (left == null) {
                    return right;
                } else if (right == null) {
                    return left;
                } else /* 2 children case */ {
                    /* 1) find inorder successor */
                    // NB: we know the successor is a child because there are 2 children (whew!)
                    Node succ = right;
                    while (succ.left != null) {
                        succ = succ.left;
                    }
                    /* 2) swap in inorder successor */
                    value = succ.value;
                    /* 3) delete inorder successor */
                    right = right.remove(value);
                    return this;
                }
            } else if (i < value) {
                if (left == null) {
                    return this;  // nothing to do
                } else {
                    left = left.remove(i);
                    return rebalance();
                }
            } else /* i > value */ {
                if (right == null) {
                    return this;
                } else {
                    right = right.remove(i);
                    return rebalance();
                }
            }
        }

        /** returns null if the node doesn't exist */
        public Node get(int i) {
            if (i == value) return this;
            else if (i < value) return left == null ? null : left.get(i);
            else /* i > value*/ return right == null ? null : right.get(i);
        }
    }
}
