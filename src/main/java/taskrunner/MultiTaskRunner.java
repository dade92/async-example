package taskrunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class MultiTaskRunner {
    private final ExecutorService executor;

    public MultiTaskRunner(ExecutorService executor) {
        this.executor = executor;
    }

    public List<String> runTasks() {
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            int taskId = i;
            futures.add(CompletableFuture.supplyAsync(() -> runTask(taskId), executor));
        }

        CompletableFuture<Void> allDone = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );

        allDone.join();
        return futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());
    }

    private String runTask(int taskId) {
        try {
            Thread.sleep(1000L * taskId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Task " + taskId + " was interrupted");
        }
        String data = "data-from-task-" + taskId;
        return buildResult(taskId, data);
    }

    private String buildResult(int taskId, String data) {
        return "Task " + taskId + " produced " + data +
            " on " + Thread.currentThread().getName();
    }
}