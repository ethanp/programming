package algorithms.arraysAndSorting.moderate;

import java.util.BitSet;
import java.util.Scanner;

/**
 * Ethan Petuchowski 12/25/14
 *
 * The big surprise here:
 *      The "faster" version was (by stroke of luck) **WAY**
 *      faster than the "original" version. It uses BitSet
 *      instead of int[]. My guess is that Java wastes plenty
 *      of time in *initializing* the new int[N] for large N,
 *      but the time to initialize BitSet is *way* smaller
 *      because (looking at the implementation) it's just a
 *      long[] which gets access via << bit-shifts.
 */
public class Cipher {

    static void faster(int N, int K, String chars) {
        BitSet decoded = new BitSet(N);
        BitSet pastK = new BitSet(K);
        BitSet encoded = enc(N, K, chars);
        boolean acc = encoded.get(N-1);
        decoded.set(N-1, acc);
        pastK.set((N-1)%(K-1), acc);
        for (int i = N-2; i >= 0; i--) {
            boolean nxtVal = encoded.get(i) ^ acc;
            decoded.set(i, nxtVal);
            acc ^= pastK.get(i%(K-1)) ^ nxtVal;
            pastK.set(i%(K-1), nxtVal);
        }
        print(decoded, N);
    }

    static BitSet enc(int N, int K, String chars) {
        BitSet toRet = new BitSet(N);
        for (int i = N+K-2; i >= K-1; i--)
            toRet.set(i-(K-1), chars.charAt(i) == '1');
        return toRet;
    }

    static void original(int N, int K, char[] chars) {
        int[] decoded = new int[N];
        decoded[N-1] = c2i(chars[N+K-2]);
        int acc = decoded[N-1];
        int[] pastK = new int[K-1];
        pastK[(N-1)%(K-1)] = acc;
        for (int i = N-2; i >= 0; i--) {
            int nxtEnc = c2i(chars[i+K-1]);
            decoded[i] = nxtEnc ^ acc;

            /* don't let accumulator hold more than K elements */
            acc ^= pastK[i%(K-1)]; // remove old
            pastK[i%(K-1)] = decoded[i];
            acc ^= pastK[i%(K-1)]; // add new

        }

        print(decoded);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int K = in.nextInt();
        in.nextLine();
        String inStr = in.nextLine();
//        char[] chars = inStr.toCharArray();
//        original(N, K, chars);
        faster(N, K, inStr);

    }

    static int c2i(char c) {
        return (c == '1' ? 1 : 0);
    }

    static boolean c2b(char c) {
        return c == '1';
    }

    static void print(BitSet bitSet, int N) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < N; i++) {
            sb.append(bitSet.get(i) ? '1' : '0');
        }
        System.out.println(sb.toString());
    }

    static void print(int[] decoded) {
        for (int i = 0; i < decoded.length; i++)
            System.out.print(decoded[i]);
        System.out.println();
    }
}
