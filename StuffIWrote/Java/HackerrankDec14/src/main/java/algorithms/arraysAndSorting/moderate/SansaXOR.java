package algorithms.arraysAndSorting.moderate;

import java.util.Scanner;

/**
 * Ethan Petuchowski 12/22/14
 */
public class SansaXOR {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        for (int t = 0; t < T; t++) {
            int N = in.nextInt();
            int acc = 0;
            int elem;
            for (int i = 0; i < N; i++) {
                elem = in.nextInt();
                if (((N-i)%2==1)&&((i+1)%2==1)) {
                    acc ^= elem;
                }
            }
            System.out.println(acc);
        }
    }
}
