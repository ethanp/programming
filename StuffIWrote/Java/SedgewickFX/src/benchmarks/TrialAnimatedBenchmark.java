package benchmarks;

import javafx.collections.ObservableList;

/**
 * Ethan Petuchowski 3/6/16
 */
public class TrialAnimatedBenchmark implements Benchmark {
    @Override public void runTheBenchmarks(ObservableList<Double> results) {
        for (int i = 0; i < 30; i++) {
            try { Thread.sleep(2000); }
            catch (InterruptedException ignored) {}
            results.add((double) i);
        }
    }
}
