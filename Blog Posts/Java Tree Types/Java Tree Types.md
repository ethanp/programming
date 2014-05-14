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
        public boolean elemEquals(Object o) {
            if (!(o instanceof BinaryTreeNode)) return false;
            return ((BinaryTreeNode) o).getElem().equals(this.getElem());
        }
    }
    public class BST<T> extends BinaryTree<T> {}
    public class BSTNode<T> extends BinaryTreeNode<T> {}


But now I wanted to make sure the `BinarySearchTree` is able to keep itself sorted,
so I started doing something like:

	public class BST<T extends Comparable<T>> extends BinaryTree<T> {}
	public class BSTNode<T extends Comparable<T>> extends BinaryTreeNode<T> implements Comparable<T> {}
	
I could not get this to compile, plus I couldn't really understand what I had written.
So to make the type parameters more transparent about what they *actually* accomplish,
I made the `Tree` itself type-parameterized by its node type, while ignoring the type
*within* the node like so:

	public class BinaryTreeNode<NodeType extends BinaryTreeNode> {
		protected NodeType root;
		// etc.
	}
	public class BinaryTreeNode<T> {
		private T elem;
		private BinaryTreeNode<T> left;
		private BinaryTreeNode<T> right;
		// etc.
	}
	public class BinarySearchTree<NodeType extends Comparable<T>> extends BinaryTree<NodeType extends Comparable<T>>  {}
	public class BSTNode<T extends Comparable<T>> extends BinaryTreeNode<T> {}
	
This was close but not quite there. After taking a little break and coming back to
it, the following setup made more sense, and compiled (bonus!).

	// base tree types unchanged.
	public class BinarySearchTree<NodeType extends BSTNode> extends BinaryTree<NodeType> {}
	public class BSTNode<T extends Comparable<T>> extends BinaryTreeNode<T> implements Comparable<T> {}
	
#### Key lessons

1. Give the type parameter a meaningful name if it's type is restricted
2. Don't give a class a type parameter that it is *ONLY* passing on to one of its fields
3. Don't accidently implement `Comparator` instead of `Comparable` (heh.)
4. Intellij's Java code generators speed up boilerplate coding significantly

#### Funny side note

The next day, in Martin Odersky's amazing *Functional Programming with Scala*
Coursera course, he went over the 'functional' way of making binary trees, which
have all that functional-beauty I was promised at the outset of the course. Now
my Java version feels rather *dis*functional.

