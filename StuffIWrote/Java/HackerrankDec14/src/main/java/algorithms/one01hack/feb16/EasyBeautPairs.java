package algorithms.one01hack.feb16;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/**
 * Ethan Petuchowski 2/21/16
 *
 * Started at 9:20, passed tests at 10:10.
 * Algo could probably be far-simpler?
 */
public class EasyBeautPairs {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int N = sc.nextInt();
        int[] A = new int[N];
        int[] B = new int[N];
        for (int i = 0; i < N; i++) A[i] = sc.nextInt();
        for (int i = 0; i < N; i++) B[i] = sc.nextInt();

        // counts of numbers in each
        Map<Integer, Integer> aDig = new HashMap<>();
        Map<Integer, Integer> bDig = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int aLem = A[i];
            int bLem = B[i];
            if (!aDig.containsKey(aLem)) aDig.put(aLem, 0);
            if (!bDig.containsKey(bLem)) bDig.put(bLem, 0);
            aDig.put(aLem, aDig.get(aLem)+1);
            bDig.put(bLem, bDig.get(bLem)+1);
        }

        // alteration of B: flip element for which num in A > num in B
        int aElem = -1;
        for (Map.Entry<Integer, Integer> e : aDig.entrySet()) {
            if (!bDig.containsKey(e.getKey())) {
                bDig.put(e.getKey(), 0);
                aElem = e.getKey();
                break;
            }
            else if (bDig.get(e.getKey()) < e.getValue()) {
                aElem = e.getKey();
                break;
            }
        }

        // we didn't find one to flip
        // just blindly choose first one one at random?
        if (aElem == -1) {
            Iterator<Map.Entry<Integer, Integer>> iterator = aDig.entrySet().iterator();
            Map.Entry<Integer, Integer> e = iterator.next();
            bDig.put(e.getKey(), bDig.get(e.getKey())-1);
            if (N != 1) {
                Map.Entry<Integer, Integer> f = iterator.next();
                bDig.put(f.getKey(), bDig.get(f.getKey())+1);
            }
            else if (e.getKey() != 1) bDig.put(1, 1);
            else bDig.put(2, 1);
        }

        // we did find one to flip
        // so flip it
        // aside: one could move this code inside the for-loop above
        else {
            for (Map.Entry<Integer, Integer> e : bDig.entrySet()) {
                if (!aDig.containsKey(e.getKey()) || aDig.get(e.getKey()) < e.getValue()) {
                    bDig.put(e.getKey(), e.getValue()-1);
                    bDig.put(aElem, bDig.get(aElem)+1);
                    break;
                }
            }
        }

        // count number of evil-beautiful pairs or whatever
        int acc = 0;
        for (Map.Entry<Integer, Integer> e : aDig.entrySet())
            if (bDig.containsKey(e.getKey()))
                acc += Math.min(e.getValue(), bDig.get(e.getKey()));

        System.out.println(acc);
    }
}
