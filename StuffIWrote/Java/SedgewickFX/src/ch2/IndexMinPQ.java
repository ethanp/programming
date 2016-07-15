package ch2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Ethan Petuchowski 6/25/16
 *
 * This thing is a binary heap priority queue, but it also maintains a user-specified "index"
 * for each item. This means we can always lookup the item in O(1). This is implemented by
 * maintaining a map from given-index to real-index in the heap.
 */
public class IndexMinPQ<Item extends Comparable<Item>> {

    private final Item[] items;
    private final int[] indexOfItem;
    private final int[] reverseIndex;
    private int size = 0;

    /** create a priority queue of capacity maxN with possible indices between 0 and maxN-1 */
    IndexMinPQ(int maxN) {
        this.items = (Item[]) new Comparable[maxN];
        this.indexOfItem = new int[maxN];
        this.reverseIndex = new int[maxN];
        Arrays.fill(reverseIndex, -1);
    }

    public static void main(String[] args) {
        List<String> list1 = Arrays.asList("zeroth", "first", "second", "third", "fourth", "fifth", "sixth");
        List<String> sorted = new ArrayList<>(list1);
        Collections.sort(sorted);
        IndexMinPQ<String> heap = new IndexMinPQ<>(list1.size());
        for (int i = 0; i < list1.size(); i++) heap.insert(i, list1.get(i));
        List<Integer> resList = new ArrayList<>();
        while (!heap.isEmpty()) resList.add(heap.delMin());
        System.out.println(Arrays.toString(sorted.toArray()));
        System.out.println(Arrays.toString(resList.toArray()));

        Integer[] list2 = {3, 2, 1, 5, 22};
        IndexMinPQ<Integer> heap2 = new IndexMinPQ<>(list2.length);
        List<Integer> resList2 = new ArrayList<>();
        for (int i = 0; i < list2.length; i++) heap2.insert(i, list2[i]);
        while (!heap2.isEmpty()) resList2.add(heap2.delMin());
        System.out.println(Arrays.toString(resList2.toArray()));
    }

    /** insert item; associate it with k */
    void insert(int k, Item item) {
        indexOfItem[size] = k;
        items[size] = item;
        reverseIndex[k] = size;
        swim(size++);
    }

    private void swim(int index) {
        while (isLessThanParent(index)) {
            swapWithParent(index);
            index = parentIdx(index);
        }
    }

    private int parentIdx(int idx) {
        return (idx - 1)/2;
    }

    private void swapWithParent(int idx) {
        swap(idx, parentIdx(idx));
    }

    private void swap(int idx1, int idx2) {
        // NB: this must be ordered before swapIoI() to be correct
        swapRevIdx(idx1, idx2);
        swapItemIdx(idx1, idx2);
        swapIoI(idx1, idx2);
    }

    private void swapIoI(int idx1, int idx2) {
        int idxTmp = indexOfItem[idx2];
        indexOfItem[idx2] = indexOfItem[idx1];
        indexOfItem[idx1] = idxTmp;
    }

    private void swapItemIdx(int idx1, int idx2) {
        Item itemTmp = items[idx2];
        items[idx2] = items[idx1];
        items[idx1] = itemTmp;
    }

    private void swapRevIdx(int idx1, int idx2) {
        int revTmp = reverseIndex[indexOfItem[idx1]];
        reverseIndex[indexOfItem[idx1]] = idx2;
        reverseIndex[indexOfItem[idx2]] = revTmp;
    }

    // lucky for us, this will be false at the root
    private boolean isLessThanParent(int idx) {
        return isLessThan(idx, parentIdx(idx));
    }

    private boolean isLessThan(int idx1, int idx2) {
        return items[idx1].compareTo(items[idx2]) < 0;
    }

    /** change the item associated with k to item */
    void change(int k, Item item) {
        items[reverseIndex[k]] = item;
        /* TODO sink and swim this item */
    }

    /** is k associated with some item? */
    boolean contains(int k) {
        return reverseIndex[k] >= 0;
    }

    /** remove k and its associated item */
    void delete(int k) {
        int curIdx = reverseIndex[k];
        items[curIdx] = null;
        swap(curIdx, --size);
        reverseIndex[k] = -1;
        sink(curIdx);
    }

    private void sink(int idx) {
        while (hasSmallerChild(idx)) {
            int swapIdx = smallerChildIdx(idx);
            swap(idx, swapIdx);
            idx = swapIdx;
        }
    }

    // 0 => (1, 2); 1 => (3, 4); 2 => (5, 6)
    private int leftChildIdx(int idx) {
        return idx*2 + 1;
    }

    private int rightChildIdx(int idx) {
        return (idx + 1)*2;
    }

    private boolean hasSmallerChild(int idx) {
        if (hasTwoChildren(idx)) {
            return isLessThan(leftChildIdx(idx), idx)
                || isLessThan(rightChildIdx(idx), idx);
        }
        else if (hasAtLeastOneChild(idx)) {
            return isLessThan(leftChildIdx(idx), idx);
        }
        else return false;
    }

    private boolean hasTwoChildren(int idx) {
        return rightChildIdx(idx) < size;
    }

    private boolean hasAtLeastOneChild(int idx) {
        return leftChildIdx(idx) < size;
    }

    private int smallerChildIdx(int idx) {
        int left = leftChildIdx(idx);
        int right = rightChildIdx(idx);
        return hasTwoChildren(idx)
            ? isLessThan(left, right) ? left : right
            : left;
    }

    /** return a minimal item */
    Item min() {
        return items[0];
    }

    /** return a minimal item’s index */
    int minIndex() {
        return indexOfItem[0];
    }

    /** remove a minimal item and return its index */
    int delMin() {
        int retIdx = minIndex();
        delete(retIdx);
        return retIdx;
    }

    /** is the priority queue empty? */
    boolean isEmpty() {
        return size() == 0;
    }

    /** number of items in the priority queue */
    int size() {
        return size;
    }
}
