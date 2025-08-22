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
            List<String> results = runner.runTasks();

            results.forEach(System.out::println);
        } finally {
            executor.shutdown();
        }
    }
}
