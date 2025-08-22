package taskrunner;

import java.util.List;
import java.util.function.Supplier;

public class OrdersFetcher {

    private final MultiTaskRunner multiTaskRunner;
    private final SingleOrderFetcher singleOrderFetcher;

    public OrdersFetcher(
        MultiTaskRunner multiTaskRunner,
        SingleOrderFetcher singleOrderFetcher
    ) {
        this.multiTaskRunner = multiTaskRunner;
        this.singleOrderFetcher = singleOrderFetcher;
    }

    public void fetchAll(List<String> orderIds) {
        List<Supplier<String>> tasks =
            orderIds.stream()
                .map(orderId -> (Supplier<String>) () -> singleOrderFetcher.fetch(orderId))
                .toList();

        long startTime = System.nanoTime();
        List<String> results = multiTaskRunner.runTasks(tasks);

        results.forEach(System.out::println);

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        System.out.println("Computation took " + durationMs + " ms");
    }

}
