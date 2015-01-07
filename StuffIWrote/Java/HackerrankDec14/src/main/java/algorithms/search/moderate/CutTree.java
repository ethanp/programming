package algorithms.search.moderate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * Ethan Petuchowski 1/7/15
 *
 * This is based on the answer by Rohit Kondekar
 *
 * Algorithm:
 *      1. Make adjacency lists for each node
 *      2. Use depth-first search from any node to label each node
 *           with the sum of it and the nodes below it
 *      3. Find argmin[k]: |(total - label[k]) - label[k]|
 */
public class CutTree {

    static int[] vertices;
    static boolean[] visited;
    static List<Stack<Integer>> neighbors; // Stack<Integer>[] is not possible in Java
    static int[] subSum;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        vertices = new int[N];
        subSum = new int[N];
        neighbors = new ArrayList<>(N-1);
        visited = new boolean[N];
        for (int i = 0; i < N; i++) {
            vertices[i] = in.nextInt();
            neighbors.add(new Stack<Integer>());
        }
        for (int i = 0; i < (N-1); i++) {
            int u = in.nextInt()-1;
            int v = in.nextInt()-1;
            neighbors.get(u).push(v);
            neighbors.get(v).push(u);
        }
        int total = labelSubValFrom(N-1);
        int[] cuts = new int[N-1];
        for (int i = 0; i < (N-1); i++)
            cuts[i] = Math.abs( (total-subSum[i]) - subSum[i] );
        System.out.println(arrMin(cuts));
    }

    private static int arrMin(int... ints) {
        int min = ints[0];
        for (int i : ints)
            if (i < min)
                min = i;
        return min;
    }

    private static int labelSubValFrom(int idx) {
        if (visited[idx]) return 0;
        visited[idx] = true;
        int sum = vertices[idx];
        Stack<Integer> nextStack = neighbors.get(idx);
        while (!nextStack.isEmpty()) {
            int nextIdx = nextStack.pop();
            sum += labelSubValFrom(nextIdx);
        }
        return subSum[idx] = sum;
    }
}
