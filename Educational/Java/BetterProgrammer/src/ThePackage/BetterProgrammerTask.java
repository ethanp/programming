package ThePackage;

/** Ethan Petuchowski 10/22/13
 *
 * To solve each task you will need to:
 *   copy the task into a java file (use you favorite Java IDE)
 *   implement the missing parts
 *   copy your class back
 *
 * You can use all standard JDK 1.5-1.6 classes.
 *
 * How we evaluate results
 *   solve the tasks correctly,
 *   in time,
 *   paying attention to the corner cases.
 *
 * Do not optimize for speed unless it is mentioned in the task.
 */
import java.util.*;

public class BetterProgrammerTask
{
    public static Object[] reverseArray(Object[] a) {

//        Please implement this method to
//        return a new array where the order of elements has been reversed from the original
//        array.
        if (a == null) return null;

        Object[] b = new Object[a.length];
        for (int i = 0; i < a.length; i++) {
            b[b.length - 1 - i] = a[i];
        }
        return b;
    }

    public static int getCountOfOnes(int n) {
        //         Please implement this method to
        //         return the number of '1's in the binary representation of n
        //         for any integer n, where n > 0
        //
        //         Example: for n=6 the binary representation is '110' and the number of '1's in that
        //         representation is 2
        if (n <= 0)
            return -1;

        int count = 0;
        while (n != 0) {
            if (n % 2 == 1) {
                count++;
            }
            n /= 2;
        }
        return count;
    }

    // Please do not change this interface
    public static interface Node
    {
        int getValue();

        List<Node> getChildren();
    }

    public static int getLargestRootToLeafSum(Node root) {
        //          A root-to-leaf path in a tree is a path from a leaf node through all its ancestors
        //          to the root node inclusively.
        //          A "root-to-leaf sum" is a sum of the node values in a root-to-leaf path.
        //
        //          Please implement this method to
        //          return the largest root-to-leaf sum in the tree.

        // plan: return the max of sum from all the children
        if (root == null) return -1;
        int bestPathSum = 0;
        for (Node node : root.getChildren()) {
            int pathSum = getLargestRootToLeafSum(node);
            if (pathSum > bestPathSum)
                bestPathSum = pathSum;
        }

        return root.getValue() + bestPathSum;
    }

    public static int countWaysToJump(int N) {
        /*
          A set of stairs has N steps.
          You can jump either 1 or 2 steps at a time.
          For example, if the stairs is N=4 steps, you can reach the end in 5 possible ways:
          1-1-1-1, or 1-2-1 or 1-1-2 or 2-1-1 or 2-2
          Please implement this method to
          return the count of the different ways to reach the end of the stairs with N steps.
         */
        int oneStep = 0;
        int twoSteps = 0;
        if (N > 1) {
           twoSteps = countWaysToJump(N-2);
        }
        if (N > 0) {
            oneStep = countWaysToJump(N-1);
        }
        if (N == 0) {
            return 1;
        }
        return oneStep + twoSteps;
    }
}

