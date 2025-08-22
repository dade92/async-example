package taskrunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class TaskRunner {
    private final ExecutorService executor;

    public TaskRunner(ExecutorService executor) {
        this.executor = executor;
    }

    public List<String> runTasks() {
        List<CompletableFuture<String>> futures = new ArrayList<>();

        // Launch 3 tasks
        for (int i = 1; i <= 3; i++) {
            int taskId = i;
            CompletableFuture<String> future =
                CompletableFuture.supplyAsync(() -> runTask(taskId), executor);
            futures.add(future);
        }

        // Combine all futures into one
        CompletableFuture<Void> allDone = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );

        // Wait until all tasks are done, then collect results
        allDone.join(); // block until completion
        return futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());
    }

    // Full task logic (work + return value)
    private String runTask(int taskId) {
        String data = doWork(taskId); // get some data from doWork
        return buildResult(taskId, data);
    }

    // Simulated work that now returns data
    private String doWork(int taskId) {
        try {
            Thread.sleep(1000L * taskId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Task " + taskId + " was interrupted");
        }
        return "data-from-task-" + taskId;
    }

    // Build the return value
    private String buildResult(int taskId, String data) {
        return "Task " + taskId + " produced " + data +
            " on " + Thread.currentThread().getName();
    }
}