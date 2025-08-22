package taskrunner;

import java.util.List;
import java.util.function.Supplier;

public class OrdersFetcher {

    private final MultiTaskRunner<Order> multiTaskRunner;
    private final SingleOrderFetcher singleOrderFetcher;

    public OrdersFetcher(
        MultiTaskRunner<Order> multiTaskRunner,
        SingleOrderFetcher singleOrderFetcher
    ) {
        this.multiTaskRunner = multiTaskRunner;
        this.singleOrderFetcher = singleOrderFetcher;
    }

    public List<Order> fetchAll(List<String> orderIds) {
        List<Supplier<Order>> tasks =
            orderIds
                .stream()
                .map(orderId -> (Supplier<Order>) () -> singleOrderFetcher.fetch(orderId))
                .toList();

        return multiTaskRunner.runTasks(tasks);
    }

}
