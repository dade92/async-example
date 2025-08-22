package taskrunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MultiTaskRunner<E> {
    private final ExecutorService executor;

    public MultiTaskRunner(ExecutorService executor) {
        this.executor = executor;
    }

    public List<E> runTasks(List<Supplier<E>> tasks) {
        List<CompletableFuture<E>> futures = new ArrayList<>();

        for (Supplier<E> task : tasks) {
            futures.add(CompletableFuture.supplyAsync(task, executor));
        }

        CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        ).join();

        return futures
            .stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());
    }

}