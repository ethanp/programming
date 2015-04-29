package algorithms.graphTheory.easy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

/**
 * Ethan Petuchowski 4/28/15
 *
 * brute-force bfs to the top
 */
public class SnakesAndLadders {
    public static void main(String[] args) {
        new SnakesAndLadders();
    }

    Map<Integer, Integer> snakes;
    Map<Integer, Integer> ladders;
    Scanner sc = new Scanner(System.in);

    class Obj implements Comparable<Obj> {
        public Obj(int l, int d) { loc = l; depth = d; }
        int loc;
        int depth;

        @Override public int compareTo(Obj o) {
            return depth - o.depth;
        }
    }

    int bfs() {
        // its a MIN-heap
        PriorityQueue<Obj> q = new PriorityQueue<>();
        Set<Integer> visited = new HashSet<>();
        q.add(new Obj(1, 0));
        while (!q.isEmpty()) {
            Obj obj = q.poll();
            int loc = obj.loc;
            int depth = obj.depth;
            for (int i = 1; i <= 6; i++) {
                int nxtLoc = loc+i;
                while (snakes.containsKey(nxtLoc) || ladders.containsKey(nxtLoc)) {
                    if (snakes.containsKey(nxtLoc)) {
                        nxtLoc = snakes.get(nxtLoc);
                    }
                    else {
                        nxtLoc = ladders.get(nxtLoc);
                    }
                }
                if (nxtLoc == 100) {
                    return depth+1;
                }
                if (nxtLoc > 100) {
                    continue;
                }
                if (!visited.contains(nxtLoc)) {
                    visited.add(nxtLoc);
                    q.add(new Obj(nxtLoc, depth+1));
                }
            }
        }
        return -1;
    }

    SnakesAndLadders() {
        int numTests = sc.nextInt();
        for (int testCase = 0; testCase < numTests; testCase++) {
            ladders = new HashMap<>();
            snakes = new HashMap<>();
            fill(ladders);
            fill(snakes);
            System.out.println(bfs());
        }
    }

    void fill(Map<Integer, Integer> m) {
        int count = sc.nextInt();
        for (int idx = 0; idx < count; idx++) {
            int startPt = sc.nextInt();
            int endPt = sc.nextInt();
            m.put(startPt, endPt);
        }
    }
}
