import java.util.Arrays;

/** Ethan Petuchowski 9/21/13 */
public class MergeSortTest
{
    public static void main(String[] args) {
        int[] a = {22};
        int[] aT = {22};
        int[] aM = sortAndPrint(a);
        int[] b = {26, 22};
        int[] bT = {22, 26};
        int[] bM = sortAndPrint(b);
        int[] c = {10, 5, 45, 2, 37};
        int[] cT = {2, 5, 10, 37, 45};
        int[] cM = sortAndPrint(c);

        test("A", aM, aT);
        test("B", bM, bT);
        test("C", cM, cT);
    }

    public static int[] sortAndPrint(int[] a) {
        int[] b = MergeSort.mergeSort(a);
        System.out.print(b[0]);
        for (int i = 1; i < b.length; i++)
            System.out.print(", " + b[i]);
        System.out.println();
        return b;
    }

    public static void test(String num, int[] result, int[] truth) {
        if (Arrays.equals(result, truth))
            System.out.println(num+": Passed")
        else System.out.println(num+": Failed")
    }
}
