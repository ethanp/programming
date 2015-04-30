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

    public Main(int[] arr) {
        int avg = getAvg(arr), ctrU = 0, ctrD = 0;
        for (int e : arr) {
            if (e > avg) ctrU += e-avg;
            else ctrD += avg-e; //bug: I had neglected this line, then googled it to realize.
        }
        int res = Math.min(ctrU, ctrD);
        System.out.printf("$%d.%02d\n", res/100, res%100);
    }

    private int getAvg(int[] arr) {
        int sum = 0; for (int e : arr) sum += e;
        return (int)Math.round((double)sum/arr.length);
    }
}
