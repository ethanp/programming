package ch2;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Ethan Petuchowski 6/12/16
 */
public class HeapPQ<Key extends Comparable<Key>> {

    private Key[] arr;
    private int size = 0;

    public HeapPQ(int initialCapacity) {
        this.arr = (Key[]) new Comparable[initialCapacity];
    }

    public HeapPQ(Key[] a) {
        this.arr = Arrays.copyOf(a, a.length);
        this.size = a.length;
        heapify();
    }

    public HeapPQ() {
        this(16);
    }

    public static void main(String[] args) {
        HeapPQ<Integer> heap = new HeapPQ<>();
        heap.insertAll(Arrays.asList(3, 1, 5, 57, 15, 4, 2));
        List<Integer> resList = new ArrayList<>();
        while (!heap.isEmpty()) resList.add(heap.delMax());
        System.out.println(Arrays.toString(resList.toArray()));

        HeapPQ<Integer> heap2 = new HeapPQ<>(new Integer[]{3, 2, 1, 5, 22});
        List<Integer> resList2 = new ArrayList<>();
        while (!heap2.isEmpty()) resList2.add(heap2.delMax());
        System.out.println(Arrays.toString(resList2.toArray()));
    }

    public void insertAll(Collection<Key> keys) {
        for (Key k : keys) insert(k);
    }

    @Contract(pure = true) private boolean arrFull() {
        return arr.length == size;
    }

    private void increaseCapacity() {
        Key[] longer = (Key[]) new Comparable[arr.length*2];
        System.arraycopy(arr, 0, longer, 0, size);
        arr = longer;
    }

    public void insert(Key v) {
        if (arrFull()) increaseCapacity();
        arr[size] = v;
        swim(size++);
    }

    private void swim(int idx) {
        while (largerThanParent(idx)) {
            swap(idx, parentIdx(idx));
            idx = parentIdx(idx);
        }
    }

    private boolean largerThanParent(int idx) {
        return arr[idx].compareTo(parent(idx)) > 0;
    }

    @Contract(pure = true) private Key parent(int idx) {
        return arr[parentIdx(idx)];
    }

    @Contract(pure = true) private int parentIdx(int idx) {
        return (idx - 1)/2;
    }

    public Key max() {
        return arr[0];
    }

    public Key delMax() {
        Key max = arr[0];
        // copy last to top
        arr[0] = arr[--size];
        arr[size] = null;
        // then sink it
        sink(0);
        return max;
    }

    @Contract(pure = true) private int leftChildIdx(int idx) {
        return idx*2 + 1;
    }

    private Key leftChild(int idx) {
        return getAtIdx(leftChildIdx(idx));
    }

    @Contract(pure = true) private int rightChildIdx(int idx) {
        return idx*2 + 2;
    }

    @Nullable private Key getAtIdx(int childIdx) {
        if (childIdx >= size()) return null;
        return arr[childIdx];
    }

    private Key rightChild(int idx) {
        return getAtIdx(rightChildIdx(idx));
    }

    private void sink(int idx) {
        while (smallerThanEitherChild(idx)) {
            if (leftChildIdx(idx) >= size()) return;
            int largerChildIdx = largerChildIdx(idx);
            swap(idx, largerChildIdx);
            idx = largerChildIdx;
        }
    }

    private void swap(int a, int b) {
        Key tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }

    private int largerChildIdx(int idx) {
        return rightChild(idx) == null || leftChild(idx).compareTo(rightChild(idx)) > 0
            ? leftChildIdx(idx) : rightChildIdx(idx);
    }

    private boolean smallerThanEitherChild(int idx) {
        return smallerThanChild(idx, leftChild(idx))
            || smallerThanChild(idx, rightChild(idx));
    }

    private boolean smallerThanChild(int idx, Key child) {
        return child != null && arr[idx].compareTo(child) < 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void heapify() {
        for (int i = size-1; i >= 0;) swim(i--);
    }
}
