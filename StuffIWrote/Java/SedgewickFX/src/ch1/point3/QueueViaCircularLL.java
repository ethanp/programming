package ch1.point3;

import java.util.AbstractQueue;
import java.util.Iterator;

/**
 * Ethan Petuchowski 5/21/16
 *
 * Write a Queue implementation that uses a circular linked list, which is the same as a linked list
 * except that no links are null and the value of last.next is first whenever the list is not empty.
 * Keep only one Node instance variable (last).
 */
public class QueueViaCircularLL extends AbstractQueue<Integer> {

    private Node last;
    private int size = 0;

    @Override public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            Node cur = null;
            @Override public boolean hasNext() {
                return cur != last;
            }
            @Override public Integer next() {
                cur = cur == null ? last.next : cur.next;
                return cur.value;
            }
        };
    }

    @Override public int size() {
        return size;
    }

    @Override public boolean offer(Integer integer) {
        if (last == null) {
            last = new Node(integer);
            last.next = last;
        } else {
            Node first = last.next;
            last.next = new Node(integer);
            last = last.next;
            last.next = first;
        }
        size++;
        return true;
    }

    @Override public Integer poll() {
        Integer removed = peek();
        last.next = last.next.next;
        size--;
        return removed;
    }

    @Override public Integer peek() {
        return last.next.value;
    }

    private static class Node {
        Integer value;
        Node next;

        Node(Integer value) {
            this.value = value;
        }

        public Node(Integer value, Node next) {
            this.value = value;
            this.next = next;
        }
    }
}
