package ch1;

/**
 * Ethan Petuchowski 2/21/16
 *
 * 1.4.10 Modify so that it always returns the element with the smallest index that matches the
 * search element (and still guarantees logarithmic running time).
 *
 * I think this'll work.
 */
public class LoIdxBinSrch {
    public static void main(String[] args) {
        System.out.println(3 == loIdx(new int[]{2, 3, 4, 5, 7}, 5));
        System.out.println(1 == loIdx(new int[]{2, 3, 3, 3, 3, 3, 3, 3, 3, 3}, 3));
        System.out.println(0 == loIdx(new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3}, 3));
        System.out.println(0 == loIdx(new int[]{3, 3, 3, 3, 3, 3, 4, 4, 4}, 3));
    }

    /* binary search for the number, then keep going, using lo as the base point na mean */
    static int loIdx(int[] arr, int val) {
        int lo = 0, hi = arr.length, mid;
        while (lo <= hi) {
            mid = (lo+hi)/2;
            if (arr[mid] > val) hi = mid-1;
            else if (arr[mid] < val) lo = mid+1;
            else if (mid > 0 && arr[mid-1] == arr[mid]) hi = mid-1; // this is where we win.
            else return mid;
        }
        return -1;
    }
}
