package ch2;

import util.SingleLLNode;

import java.util.Arrays;

/**
 * Ethan Petuchowski 3/2/16
 *
 * Bottom-Up Linked-List Merge-Sort
 */

/* TODO this ended up being more difficult than I anticipated
 *      and I have not figured out the inner-loop yet.
 *      Aka I am downright confused at this point and need to stop.
 *      I don't think it's _that far_ from working.
 */
public class BULLMerge {
    public static void main(String[] args) {
        SingleLLNode head = null;
        SingleLLNode cur = null;
        char[] testChars = "bac".toLowerCase().toCharArray();
        for (char c : testChars) {
            if (cur == null) {
                head = new SingleLLNode(c);
                cur = head;
            }
            else {
                cur.fwd = new SingleLLNode(c);
                cur = cur.fwd;
            }
        }
        assert head != null;
        System.out.println(head.listString());
        head = sort(head, testChars.length);
        Arrays.sort(testChars);
        System.out.println(Arrays.toString(testChars));
        System.out.println(head.listString());
    }

    private static SingleLLNode sort(SingleLLNode head, int size) {
        for (int sz = 1; sz < size; sz *= 2) {
            SingleLLNode front = head;
            SingleLLNode back = head;
            for (int backIdx = 0; backIdx <= size-(2*sz); backIdx += sz) {
                for (int space = 0; space < sz; space++) front = front.fwd;
                back = merge(back, sz, front, sz);
                if (backIdx == 0) head = back;
            }
        }
        return head;
    }

    /**
     * @param a    sorted list
     * @param lenA length of list a
     * @param b    sorted list
     * @param lenB length of list b
     * @return a merged with b in sorted order
     */
    private static SingleLLNode merge(SingleLLNode a, int lenA, SingleLLNode b, int lenB) {
        if (lenA == 0) return b;
        if (lenB == 0) return a;
        SingleLLNode ret = null;
        SingleLLNode cur = null;
        for (int i = 0, j = 0, k = 0; i < lenA+lenB; i++) {
            if (j >= lenA) {
                cur.fwd = new SingleLLNode(b.val);
                b = b.fwd;
                cur = cur.fwd;
                k++;
            }
            else if (k >= lenB) {
                cur.fwd = new SingleLLNode(a.val);
                a = a.fwd;
                cur = cur.fwd;
                j++;
            }
            else if (b.val < a.val) {
                //noinspection Duplicates
                if (ret == null) {
                    ret = new SingleLLNode(b.val);
                    cur = ret;
                    k++;
                }
                else {
                    cur.fwd = new SingleLLNode(b.val);
                    b = b.fwd;
                    cur = cur.fwd;
                    k++;
                }
            }
            else {
                //noinspection Duplicates
                if (ret == null) {
                    ret = new SingleLLNode(a.val);
                    cur = ret;
                    j++;
                }
                else {
                    cur.fwd = new SingleLLNode(a.val);
                    a = a.fwd;
                    cur = cur.fwd;
                    j++;
                }
            }
        }
        return ret;
    }
}
