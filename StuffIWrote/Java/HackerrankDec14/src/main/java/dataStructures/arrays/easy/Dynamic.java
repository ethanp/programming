package dataStructures.arrays.easy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 8/10/16 7:50 AM
 */
public class Dynamic {
    public static void main(String[] args) {
        int lastAns = 0;
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int Q = sc.nextInt();
        List<Integer>[] lists = new List[N];
        for (int n = 0; n < N; n++)
            lists[n] = new ArrayList<>();
        for (int q = 0; q < Q; q++) {
            int type = sc.nextInt();
            int x = sc.nextInt();
            int y = sc.nextInt();
            List<Integer> seq = lists[(x ^ lastAns) % N];
            if (type == 1) seq.add(y);
            else {
                lastAns = seq.get(y%seq.size());
                System.out.println(lastAns);
            }
        }
    }
}
