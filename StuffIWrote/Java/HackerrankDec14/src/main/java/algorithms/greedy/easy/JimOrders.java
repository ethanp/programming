package algorithms.greedy.easy;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/**
 * Ethan Petuchowski 5/31/15
 *
 * I just need to stick them in a MinHeap and pop them out
 */
public class JimOrders {
    static class Order implements Comparable<Order> {
        int idx, endTime;
        public Order(int i, int e) { idx = i; endTime = e; }
        @Override public int compareTo(Order o) {
            if (endTime == o.endTime) return idx-o.idx;
            return endTime-o.endTime;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numOrders = sc.nextInt();
        Queue<Order> orders = new PriorityQueue<>();
        for (int orderNum = 0; orderNum < numOrders; orderNum++)
            orders.add(new Order(orderNum, sc.nextInt()+sc.nextInt()));
        while (!orders.isEmpty())
            System.out.print((orders.poll().idx+1)+" ");
        System.out.println();
    }
}
