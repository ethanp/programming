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

    public static void main(String[] args) {
        QueueViaCircularLL q = new QueueViaCircularLL();
        q.offer(1);
        q.offer(2);
        System.out.println(q.size());
        q.offer(3);
        System.out.println(q.peek());
        q.offer(4);
        System.out.println(q.size());
        System.out.println("---------------");
        q.offer(5);
        for (int elem : q)
            System.out.println(elem);
        System.out.println("---------------");
        for (int i = 0; i < 5; i++)
            System.out.println(q.poll());
        System.out.println("---------------");
        System.out.println(q.size());
        q.offer(3);
        System.out.println(q.poll());
        q.offer(3);
        System.out.println(q.poll());
    }

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
        size++;
        if (last == null) {
            last = new Node(integer);
            last.next = last;
        }
        else {
            Node first = last.next;
            last.next = new Node(integer);
            last = last.next;
            last.next = first;
        }
        return true;
    }

    @Override public Integer poll() {
        size--;
        Integer removed = peek();
        if (size == 0) last = null;
        else last.next = last.next.next;
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
