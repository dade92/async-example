package taskrunner;

import java.util.List;
import java.util.function.Supplier;

public class OrdersFetcher {

    private final MultiTaskRunner<String> multiTaskRunner;
    private final SingleOrderFetcher singleOrderFetcher;

    public OrdersFetcher(
        MultiTaskRunner<String> multiTaskRunner,
        SingleOrderFetcher singleOrderFetcher
    ) {
        this.multiTaskRunner = multiTaskRunner;
        this.singleOrderFetcher = singleOrderFetcher;
    }

    public List<String> fetchAll(List<String> orderIds) {
        List<Supplier<String>> tasks =
            orderIds
                .stream()
                .map(orderId -> (Supplier<String>) () -> singleOrderFetcher.fetch(orderId))
                .toList();

        long startTime = System.nanoTime();
        List<String> orders = multiTaskRunner.runTasks(tasks);

        orders.forEach(System.out::println);

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        System.out.println("Computation took " + durationMs + " ms");
        return orders;
    }

}
