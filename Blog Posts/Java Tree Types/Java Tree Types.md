#### Someday I'd like to get a job programming.

From what I've heard, one can only get a job programming after proving to the
employer that one can come up with and perfectly implement little
tree-manipulation algorithms on the spot in C++ or Java. I've never had to hire
a programmer so I don't really understand this practice, but there's nothing I
can do about it except start my own company, so I decided to familiarize myself
with some basic tree-manipulation techniques.

The first step seemed to me to build a generic binary tree. I'm not super familiar
with Java, so this is (something like) what I wrote:

    public class BinaryTree<T> {
        private BinaryTreeNode<T> root;
        // getter and setter and constructor

        public static void main(String[] args) {
            BinaryTree<Integer> bt = new BinaryTree<Integer>(new BinaryTreeNode<Integer>(32));
            bt.getRoot().setLeft(new BinaryTreeNode<Integer>(23));
            System.out.println(bt.getRoot().getElem() + " -> " + bt.getRoot().getLeft().getElem());
        }

    }

    public class BinaryTreeNode<T> {
        private T elem;
        private BinaryTreeNode<T> left;
        private BinaryTreeNode<T> right;
        // getters and setters and constructor
    }

    public class BST<T> extends BinaryTree<T> {
        // TODO
    }

    public class BSTNode<T> extends BinaryTreeNode<T> {
        // TODO
    }


