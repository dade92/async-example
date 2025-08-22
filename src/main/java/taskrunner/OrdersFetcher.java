package taskrunner;

import java.util.List;
import java.util.function.Supplier;

public class OrdersFetcher {

    private final MultiTaskRunner multiTaskRunner;
    private final OrderFetcher orderFetcher;

    public OrdersFetcher(
        MultiTaskRunner multiTaskRunner,
        OrderFetcher orderFetcher
    ) {
        this.multiTaskRunner = multiTaskRunner;
        this.orderFetcher = orderFetcher;
    }

    public void fetchAll(List<String> orderIds) {
        List<Supplier<String>> tasks =
            orderIds.stream()
                .map(orderId -> (Supplier<String>) () -> orderFetcher.fetch(orderId))
                .toList();

        long startTime = System.nanoTime();
        List<String> results = multiTaskRunner.runTasks(tasks);

        results.forEach(System.out::println);

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        System.out.println("Computation took " + durationMs + " ms");
    }

}
