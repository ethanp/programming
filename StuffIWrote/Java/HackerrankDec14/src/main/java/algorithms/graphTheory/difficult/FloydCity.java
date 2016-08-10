package algorithms.graphTheory.difficult;

import java.util.Scanner;

/**
 * 8/10/16 1:02 PM
 *
 * I guess we're supposed to use "Floyd-Warshall" for this (judging by the name of the problem)
 *
 * I'm not familiar with Floyd-Warshall, so let's do some research:
 *
 * A single execution of the algorithm will find the lengths (summed weights) of the shortest paths
 * between all pairs of vertices, though it does not return details of the paths themselves.
 *
 * It uses dynamic programming (snazzy), and was published by Floyd in 1962, though various similar
 * algorithms existed e.g. Kleene's algo for turning DFA into RegEx (ooh).
 *
 * It basically amounts to 3 nested for loops (clean). It tests _every_ combination of edges. It
 * incrementally improves an "estimated" length until (deterministically) arriving at the optimal
 * [in O(|V|^3) time].
 *
 * Let shortestPath(i, j, k) return the length of the shortest path from i to j using only
 * intermediate nodes in 1:k. Then the recurrence relation is as follows
 *
 * shortestPath(i, j, k+1) = min(
 * shortestPath(i,j,k),
 * shortestPath(i,k+1,k) + shortestPath(k+1,j,k)
 * )
 *
 * and the base case is
 *
 * shortestPath(i,j,0) = w(i,j) // duh.
 *
 * Shortest-paths can be computed without too much difficulty if necessary.
 */
public class FloydCity {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();


        /* floyd warshall */
        // init
        int[][] dist = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                dist[i][j] = i == j ? 0 : Integer.MAX_VALUE/2;

        // note this is a _directed_ graph (by problem statement)
        for (int m = 0; m < M; m++) {
            int u = sc.nextInt() - 1;
            int v = sc.nextInt() - 1;
            int w = sc.nextInt();
            dist[u][v] = w;
        }

        // fill it in
        for (int k = 0; k < N; k++)
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    dist[i][j] = Math.min(
                        dist[i][j],
                        dist[i][k] + dist[k][j]);

        // answer queries
        int Q = sc.nextInt();
        for (int q = 0; q < Q; q++) {
            int x = sc.nextInt() - 1;
            int y = sc.nextInt() - 1;
            System.out.println(dist[x][y] == Integer.MAX_VALUE/2 ? -1 : dist[x][y]);
        }
    }
}
