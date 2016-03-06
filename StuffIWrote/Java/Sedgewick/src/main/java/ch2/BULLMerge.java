package ch2;

import util.Pair;
import util.SingleLLNode;

import java.util.Arrays;

/**
 * Ethan Petuchowski 3/2/16
 *
 * Bottom-Up Linked-List Merge-Sort
 *
 * This was way more confusing than I expected. But I think it works properly. Since I don't know
 * the "real" algorithm I'm not sure if this is the best way to implement it. All I know is it works
 * on whatever inputs I've tried it on and it's a cool algorithm.
 */
@SuppressWarnings("Duplicates")
public class BULLMerge {
    public static void main(String[] args) {
        SingleLLNode head = null;
        SingleLLNode cur = null;
        char[] testChars = "MergeSortExample".toLowerCase().toCharArray();
        for (char c : testChars) {
            if (cur == null) {
                head = new SingleLLNode(c);
                cur = head;
            }
            else {
                cur.nxt = new SingleLLNode(c);
                cur = cur.nxt;
            }
        }
        assert head != null;
        System.out.println(head.listString());
        head = sort(head, testChars.length);
        Arrays.sort(testChars);
        String truth = new String(testChars);
        System.out.println(truth);
        String actuality = head.listString();
        System.out.println(actuality);
        assert truth.equals(actuality);
    }

    private static SingleLLNode sort(SingleLLNode head, int size) {
        for (int sz = 1; sz <= size; sz *= 2) {
            SingleLLNode front = head;
            SingleLLNode last = null;
            Pair<SingleLLNode> pair;
            for (int frontIdx = 0; frontIdx <= size-sz; frontIdx += Math.min(2*sz, size-frontIdx)) {
                pair = merge(front, sz, Math.min(sz, size-(frontIdx+sz)));
                if (last == null) head = pair.a;
                else last.nxt = pair.a;
                last = pair.b;
                front = last.nxt;
            }
        }
        return head;
    }

    /**
     * @param a    first element of first list
     * @param aLen length of first list
     * @param bLen length of second list
     * @return the beginning and the last node in the list. However the elements of the list come
     * back rearranged such that it and the "b" list after it (see below) are "merged", as known
     * from the classic mergesort algorithm.
     */
    private static Pair<SingleLLNode> merge(SingleLLNode a, int aLen, int bLen) {
        if (bLen == 0 || a == null) return new Pair<>(a, a);

        SingleLLNode ret = null;
        SingleLLNode cur = null;

        SingleLLNode b = a;
        for (int skip = 0; skip++ < aLen; )
            b = b.nxt;

        for (int i = 0, j = 0, k = 0; i < aLen+bLen; i++) {
            if (j >= aLen) {
                assert cur != null;
                cur.nxt = new SingleLLNode(b.val);
                b = b.nxt;
                cur = cur.nxt;
                k++;
            }
            else if (k >= bLen) {
                assert cur != null;
                cur.nxt = new SingleLLNode(a.val);
                a = a.nxt;
                cur = cur.nxt;
                j++;
            }
            else if (b.val < a.val) {
                if (ret == null) {
                    ret = new SingleLLNode(b.val);
                    b = b.nxt;
                    cur = ret;
                    k++;
                }
                else {
                    cur.nxt = new SingleLLNode(b.val);
                    b = b.nxt;
                    cur = cur.nxt;
                    k++;
                }
            }
            else {
                if (ret == null) {
                    ret = new SingleLLNode(a.val);
                    a = a.nxt;
                    cur = ret;
                    j++;
                }
                else {
                    cur.nxt = new SingleLLNode(a.val);
                    a = a.nxt;
                    cur = cur.nxt;
                    j++;
                }
            }
        }
        assert cur != null;
        cur.nxt = b;
        return new Pair<>(ret, cur);
    }
}
