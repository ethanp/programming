package uvaoj.theTrip_10137;

import java.util.Scanner;

/**
 * Ethan Petuchowski 4/29/15
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = Integer.parseInt(sc.nextLine());
        while (N != 0) {
            int[] arr = new int[N];
            for (int i = 0; i < N; i++) {
                arr[i] = Integer.parseInt(sc.nextLine().replaceAll("\\.",""));
            }
            new Main(arr);
            N = Integer.parseInt(sc.nextLine());
        }
    }

    int[] given;
    public Main(int[] arr) {
        given = arr;
        int avg = getAvg();
        int ctrU = 0;
        int ctrD = 0;
        for (int e : given) {
            if (e > avg) {
                ctrU += e - avg;
            }

            //bug -- I had neglected this piece, then googled it to realize.
            else if (e < avg) {
                ctrD += avg - e;
            }
            //\bug
        }
        int ctr = Math.min(ctrU, ctrD);
        System.out.printf("$%d.%02d\n", ctr/100, ctr%100);
    }

    private int getAvg() {
        int ctr = 0;
        for (int e : given) ctr += e;
        return (int)Math.round((double)ctr/given.length);
    }
}
