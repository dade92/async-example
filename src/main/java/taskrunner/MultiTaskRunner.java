package taskrunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MultiTaskRunner {
    private final ExecutorService executor;

    public MultiTaskRunner(ExecutorService executor) {
        this.executor = executor;
    }

    public List<String> runTasks(List<Supplier<String>> tasks) {
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (Supplier<String> task : tasks) {
            futures.add(CompletableFuture.supplyAsync(task, executor));
        }

        CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        ).join();

        return futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());
    }

}