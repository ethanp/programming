package algorithms.dynamicPrograming.moderate;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Ethan Petuchowski 12/18/14
 *
 * note that the subsequence does NOT have to be CONTIGUOUS
 *    e.g. LCS of [1,2,3,4] & [1,3,4] => [1,3,4]
 */
public class LongestCommonSubsequence {
    static int[] aAr;
    static int[] bAr;
    static int[][] table;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int aSize = in.nextInt();
        int bSize = in.nextInt();
        aAr = new int[aSize];
        bAr = new int[bSize];
        for (int i = 0; i < aSize; i++)
            aAr[i] = in.nextInt();
        for (int i = 0; i < bSize; i++)
            bAr[i] = in.nextInt();
        table = new int[aSize][bSize];
        for (int[] row : table)
            Arrays.fill(row, -1);

        /* fill the table */
        LCS(aSize-1, bSize-1);

        recoverAndPrint();
    }

    /* define c[i,j] := |LCS(x[1..i],y[1..j])|
     *   then c[len(x),len(y)] == |LCS(x,y)|
     *
     * but c[i,j] =
     *            | c[i-1,j-1] + 1         if x[i] = y[j]
     *            | max{c[i,j-1],c[i-1,j]} otw
     *
     */
    static int LCS(int i, int j) {

        if (i < 0 || j < 0)
            return 0;

        if (table[i][j] > -1) {
            return table[i][j];
        }
        int result;
        if (aAr[i] == bAr[j]) {
            result = LCS(i-1,j-1) + 1;
        } else {
            result = Math.max(LCS(i, j-1), LCS(i-1, j));
        }
        table[i][j] = result;
        return result;
    }

    static void recoverAndPrint() {
        int lenLCS = table[aAr.length-1][bAr.length-1];
        int[] seq = new int[lenLCS];
        int i = lenLCS-1;
        int x = aAr.length-1;
        int y = bAr.length-1;
        while (i >= 0) {
            if (aAr[x] == bAr[y]) {
                seq[i--] = aAr[x];
                x--;
                y--;
            } else if (LCS(x-1,y) == i+1) {
                x--;
            } else {
                y--;
            }
        }
        for (int j = 0; j < seq.length; j++) {
            System.out.print(seq[j]+" ");
        }
        System.out.println();
    }
}
