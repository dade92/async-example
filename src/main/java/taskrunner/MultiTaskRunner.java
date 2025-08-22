package taskrunner;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MultiTaskRunner<T> {
    private final ExecutorService executor;

    public MultiTaskRunner(ExecutorService executor) {
        this.executor = executor;
    }

    public List<T> runTasks(List<Supplier<T>> tasks) {
        List<CompletableFuture<T>> futures =
            tasks.stream()
                .map(task -> CompletableFuture.supplyAsync(task, executor))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return futures
            .stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());
    }

}