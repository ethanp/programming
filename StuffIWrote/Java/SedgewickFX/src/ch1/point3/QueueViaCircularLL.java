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
public class QueueViaCircularLL<E> extends AbstractQueue<E> {

    private Node<E> last;
    private int size = 0;

    public static void shouldBe(int item, int shouldBe) {
        System.out.println(item == shouldBe ? "pass" : item+" should be "+shouldBe);
    }

    public static void main(String[] args) {
        QueueViaCircularLL<Integer> q = new QueueViaCircularLL<>();
        q.offer(1);
        q.offer(2);
        shouldBe(q.size(), 2);
        q.offer(3);
        shouldBe(q.peek(), 1);
        q.offer(4);
        shouldBe(q.size(), 4);
        System.out.println("---------------");
        q.offer(5);
        q.forEach(System.out::println);
        System.out.println("---------------");
        for (int i = 0; i < 5; i++)
            shouldBe(q.poll(), i+1);
        System.out.println("---------------");
        shouldBe(q.size(), 0);
        q.offer(3);
        shouldBe(q.poll(), 3);
        q.offer(3);
        shouldBe(q.poll(), 3);
    }

    @Override public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> cur = null;

            @Override public boolean hasNext() {
                return cur != last;
            }

            @Override public E next() {
                cur = cur == null ? last.next : cur.next;
                return cur.value;
            }
        };
    }

    @Override public int size() {
        return size;
    }

    @Override public boolean offer(E integer) {
        size++;
        if (last == null) {
            last = new Node<>(integer);
            last.next = last;
        }
        else {
            Node first = last.next;
            last.next = new Node<>(integer);
            last = last.next;
            last.next = first;
        }
        return true;
    }

    @Override public E poll() {
        if (size == 0) return null;
        size--;
        E removed = peek();
        if (size == 0) last = null;
        else last.next = last.next.next;
        return removed;
    }

    @Override public E peek() {
        return last.next.value;
    }

    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }
}
