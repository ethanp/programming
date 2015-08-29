package dataStructures.stacks;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

/**
 * Ethan Petuchowski 8/29/15
 *
 * I came up with an algorithm on my whiteboard.
 * I'm not especially in the mood to describe it in words up here at the moment.
 *
 * Clearly this is not great code, but I'm digging the algorithm....
 */
public class LargestRectangle {

    final static Scanner sc = new Scanner(System.in);

    public LargestRectangle(int N) {
        Stack<Item> stack = new Stack<>();
        Queue<Item> saved = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            int h = sc.nextInt();
            if (stack.isEmpty()) {
                stack.add(new Item(h));
            }
            else {
                Item popped = null;
                while (!stack.isEmpty() && stack.peek().height > h) {
                    if (popped != null && !stack.isEmpty()) {
                        stack.peek().count += popped.count;
                    }
                    popped = stack.pop();
                    saved.add(popped);
                }
                if (stack.isEmpty()) {
                    stack.add(new Item(h));
                    if (popped != null) {
                        stack.peek().count += popped.count;
                    }
                }
                else if (stack.peek().height == h) {
                    stack.peek().count++;
                    if (popped != null) {
                        stack.peek().count += popped.count;
                    }
                }
                else if (stack.peek().height < h) {
                    stack.add(new Item(h));
                    if (popped != null) {
                        stack.peek().count += popped.count;
                    }
                }
            }
        }
        Item popped = null;
        while (!stack.isEmpty()) {
            if (popped != null && !stack.isEmpty()) {
                stack.peek().count += popped.count;
            }
            popped = stack.pop();
            saved.add(popped);
        }
//        System.out.println(saved.peek().height + " " + saved.peek().count);
        System.out.println(saved.poll().value());
    }

    static class Item implements Comparable<Item> {
        final int height;
        int count = 1;

        Item(int height) {
            this.height = height;
        }

        public int value() {
            return count*height;
        }

        @Override public int compareTo(Item o) {
            return o.value()-value();
        }
    }

    public static void main(String[] args) {
        int N = sc.nextInt();
        new LargestRectangle(N);
    }
}
