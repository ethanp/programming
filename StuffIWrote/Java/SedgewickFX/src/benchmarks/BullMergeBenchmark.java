package benchmarks;

import ch2.BULLMerge;
import javafx.collections.ObservableList;
import util.SingleLLNode;
import util.Testing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Ethan Petuchowski 3/6/16
 */
public class BullMergeBenchmark implements Benchmark {
    @Override public void runTheBenchmarks(ObservableList<Double> results) {
        Random r = new Random(1);
        for (int len = 2; len < 1_000_000; len += len+r.nextInt(5)) {

            System.out.println(len);

            // initialize it
            BULLMerge<Integer> merger = new BULLMerge<>();
            List<Integer> correct = new ArrayList<>();
            int first = r.nextInt();
            correct.add(first);
            merger.setHead(new SingleLLNode<>(first));
            SingleLLNode<Integer> cur = merger.getHead();
            for (int i = 1; i < len; i++) {
                int next = r.nextInt();
                cur.nxt = new SingleLLNode<>(next);
                cur = cur.nxt;
                correct.add(next);
            }

            // sort, verify, and plot it
            long start = System.nanoTime();
            merger.sort(len);
            long end = System.nanoTime();
            double seconds = ((double) end-start)/Math.pow(10, 9);
            System.out.println(seconds);
            results.add(seconds);
            Collections.sort(correct);
            SingleLLNode<Integer> it = merger.getHead();
            for (int i = 0; i < len; i++) {
                Testing.shouldEqual(it.val, correct.get(i));
                it = it.nxt;
            }
        }
    }
}
