package algorithms.graphTheory.medium;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 * Ethan Petuchowski 8/1/15
 */
public class BFSShortestReach {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int T = sc.nextInt();
        for (int t = 0; t < T; t++) {
            int N = sc.nextInt();
            int M = sc.nextInt();
            int[] dists = new int[N];
            Arrays.fill(dists, -1);
            @SuppressWarnings("unchecked")
            List<Integer>[] adj = new List[N];
            for (int n = 0; n < N; n++) {
                adj[n] = new ArrayList<>();
            }
            for (int m = 0; m < M; m++) {
                int u = sc.nextInt()-1;
                int v = sc.nextInt()-1;
                adj[u].add(v);
                adj[v].add(u);
            }
            int S = sc.nextInt()-1;
            Queue<Integer> queue = new ArrayDeque<>();
            queue.add(S);
            // this is a neat little trick I learned somewhere
            queue.add(-1);
            int curDist = 0;
            while (!queue.isEmpty()) {
                int next = queue.poll();
                // this is part II of the neat little trick
                if (next == -1) {
                    curDist += 6;
                    if (!queue.isEmpty()) {
                        queue.add(-1);
                    }
                }
                else if (dists[next] == -1) {
                    dists[next] = curDist;
                    for (int conn : adj[next]) {
                        if (dists[conn] == -1) {
                            queue.add(conn);
                        }
                    }
                }
            }
            for (int n : dists) {
                if (n != 0) {
                    System.out.print(n+" ");
                }
            }
            System.out.println();
        }
    }
}
