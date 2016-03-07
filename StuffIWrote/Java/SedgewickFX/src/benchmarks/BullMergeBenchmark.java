package benchmarks;

import javafx.collections.ObservableList;

/**
 * Ethan Petuchowski 3/6/16
 */
public class BullMergeBenchmark implements Benchmark {
    @Override public void runTheBenchmarks(ObservableList<Double> results) {
        for (int len = 100; len < 1_000_000; len*=2) {
            long start = System.nanoTime();

            long end = System.nanoTime();
            double seconds = ((double) end-start)/Math.pow(10, 9);
            results.add(seconds);
        }
    }
}
