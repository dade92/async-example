package main;

import taskrunner.MultiTaskRunner;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CompletableFutureExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        List<Integer> orderIds = List.of(123, 456, 789);

        try {
            List<Supplier<String>> tasks =
                orderIds.stream()
                    .map(orderId -> (Supplier<String>) () -> runTask(orderId))
                    .collect(Collectors.toList());

            MultiTaskRunner runner = new MultiTaskRunner(
                executor,
                tasks
            );
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

    private static String runTask(int taskId) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Task " + taskId + " was interrupted");
        }
        String data = "data-from-task-" + taskId;
        return buildResult(taskId, data);
    }

    private static String buildResult(int taskId, String data) {
        return "Task " + taskId + " produced " + data +
            " on " + Thread.currentThread().getName();
    }
}
