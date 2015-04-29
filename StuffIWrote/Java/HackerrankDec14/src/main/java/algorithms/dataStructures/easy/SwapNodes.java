package algorithms.dataStructures.easy;

import java.util.Scanner;

/**
 * Ethan Petuchowski 4/28/15
 */
public class SwapNodes {
    class Tree {
        Tree(int l, int r) {
            left = l;
            right = r;
        }

        int left;
        int right;

        public void swap(int d, int curr) {
            if (curr % d == 0) {
                int t = left;
                left = right;
                right = t;
            }
            if (left != -2) {
                tree[left].swap(d, curr+1);
            }
            if (right != -2) {
                tree[right].swap(d, curr+1);
            }
        }
        public void printInOrder(int myIdx) {
            if (left != -2) {
                tree[left].printInOrder(left);
            }
            System.out.print((myIdx+1)+" ");
            if (right != -2) {
                tree[right].printInOrder(right);
            }
        }
    }

    public static void main(String[] args) {
        new SwapNodes();
    }

    Tree[] tree;

    SwapNodes() {
        Scanner sc = new Scanner(System.in);
        int numNodes = sc.nextInt();
        tree = new Tree[numNodes];
        for (int i = 0; i < numNodes; i++) {
            int a = sc.nextInt()-1;
            int b = sc.nextInt()-1;
            tree[i] = new Tree(a, b);
        }
        Tree root = tree[0];
        int numSwaps = sc.nextInt();
        for (int i = 0; i < numSwaps; i++) {
            int level = sc.nextInt();
            root.swap(level, 1);
            root.printInOrder(0);
            System.out.println();
        }
    }

    /* NOT USED */
    public static int getHeight(int n) {
        int height = 31 - Integer.numberOfLeadingZeros(n);
        int highestBit = 1 << height;
        int remainder = n % highestBit;
        boolean nonRound = remainder != 0;
        if (nonRound) height++;
        return height;
    }
}
