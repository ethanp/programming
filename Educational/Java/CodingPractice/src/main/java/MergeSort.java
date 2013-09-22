/** Ethan Petuchowski 9/21/13 */
public class MergeSort
{
    public static int[] mergeSort(int[] a) {

        // if length > 1, must be sorted
        // not pre-checking if it's already done
        if (a.length > 1) {

            // break into two pieces
            int lSize = a.length / 2 + a.length % 2;  // allow for odd-lengths
            int rSize = a.length / 2;
            int l[] = new int[lSize];
            int r[] = new int[rSize];
            for (int i = 0; i < lSize; i++)
                l[i] = a[i];
            for (int i = 0; i < rSize; i++)
                r[i] = a[lSize + i];

            // sort each piece
            int[] newL = mergeSort(l);
            int[] newR = mergeSort(r);

            // merge pieces back together and return
            return merge(newL, newR);
        }

        // base case, just return
        return a;
    }

    private static int[] merge(int[] a, int[] b) {
        int ap = 0, bp = 0, retp = 0;
        int[] ret = new int[a.length + b.length];

        // merge arrays
        while (ap < a.length && bp < b.length) {
            if (a[ap] < b[bp])
                ret[retp++] = a[ap++];
            else
                ret[retp++] = b[bp++];
        }

        // after one array finishes, simply finish-off the other
        while (ap < a.length)
            ret[retp++] = a[ap++];
        while (bp < b.length)
            ret[retp++] = b[bp++];

        return ret;
    }
}
