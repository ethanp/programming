package algorithms.arraysAndSorting.moderate;

import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Ethan Petuchowski 12/27/14
 */
public class PolarAngles {
    static class coord implements Comparable<coord> {
        int x, y; double theta, r;
        coord(int a,int b) { x = a; y = b; theta = t(); r = dist(); }
        double t() {
            double angle = Math.atan2(y, x);
            return angle >= 0 ? angle : 2*Math.PI + angle;
        }
        double dist() { return Math.sqrt(Math.pow(x,2)+Math.pow(y,2)); }

        @Override
        public int compareTo(coord c) {
            if (theta < c.theta) return -1;
            if (theta > c.theta) return 1;
            if (r < c.r) return -1;
            if (r > c.r) return 1;
            return 0;
        }
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        PriorityQueue<coord> ordered = new PriorityQueue<>();
        for (int i = 0; i < N; i++)
            ordered.add(new coord(in.nextInt(), in.nextInt()));
        while(!ordered.isEmpty()) {
            coord c = ordered.poll();
            System.out.println(c.x+" "+c.y);
        }
    }
}
