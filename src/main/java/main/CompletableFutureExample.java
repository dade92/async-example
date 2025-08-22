package main;

import taskrunner.MultiTaskRunner;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        try {
            MultiTaskRunner runner = new MultiTaskRunner(executor);
            long startTime = System.nanoTime();
            List<String> results = runner.runTasks();

            results.forEach(System.out::println);

            long endTime = System.nanoTime();
            long durationMs = (endTime - startTime) / 1_000_000;

            System.out.println("Computation took " + durationMs + " ms");
        } finally {
            executor.shutdown();
        }
    }
}
