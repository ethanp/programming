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
    static int[] seq1;
    static int[] seq2;
    static int[][] table;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int aSize = in.nextInt();
        int bSize = in.nextInt();
        seq1 = new int[aSize];
        seq2 = new int[bSize];
        for (int i = 0; i < aSize; i++) seq1[i] = in.nextInt();
        for (int i = 0; i < bSize; i++) seq2[i] = in.nextInt();
        table = new int[aSize][bSize];
        for (int[] row : table) Arrays.fill(row, -1);

        /* fill the table */
        LCS(aSize-1, bSize-1);

        recoverAndPrint();
    }

    /* define c[i,j] := |LCS(x[1..i],y[1..j])|
     *   then c[len(x),len(y)] == |LCS(x,y)|
     *
     * now use the recurrence formula
     *
     * c[i,j] =
     *        | c[i-1,j-1] + 1         if x[i] = y[j]
     *        | max{c[i,j-1],c[i-1,j]} otw
     */
    static int LCS(int i, int j) {

        if (i < 0 || j < 0) return 0;
        // cell value < 0 iff it is uninitialized
        if (table[i][j] > -1) return table[i][j];
        table[i][j] = seq1[i] == seq2[j]
            ? LCS(i-1,j-1) + 1
            : Math.max(LCS(i, j-1), LCS(i-1, j));
        return table[i][j];
    }

    /**
     * In this implementation, instead of filling a secondary table while finding the length of
     * the LCS showing which direction to move to recover the LCS, we use the subproblem-table
     * itself to recover the LCS. The idea of using a secondary table for this purpose comes from
     * (CLRS, pg. 395).
     */
    static void recoverAndPrint() {
        int subseqLength = table[seq1.length-1][seq2.length-1];
        int[] subseq = new int[subseqLength];
        int remainingLength = subseqLength-1;
        int idx1 = seq1.length-1;
        int idx2 = seq2.length-1;
        while (remainingLength >= 0) {
            if (seq1[idx1] == seq2[idx2]) {
                subseq[remainingLength--] = seq1[idx1];
                idx1--;
                idx2--;
            } else if (LCS(idx1-1,idx2) == remainingLength+1) {
                idx1--;
            } else {
                idx2--;
            }
        }
        for (int aSeq : subseq)
            System.out.print(aSeq+" ");
        System.out.println();
    }
}
