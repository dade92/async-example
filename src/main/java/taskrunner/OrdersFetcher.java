package taskrunner;

import order.Order;
import order.OrderRepository;

import java.util.List;
import java.util.function.Supplier;

public class OrdersFetcher {

    private final MultiTaskRunner<Order> multiTaskRunner;
    private final OrderRepository singleOrderFetcher;

    public OrdersFetcher(
        MultiTaskRunner<Order> multiTaskRunner,
        OrderRepository singleOrderFetcher
    ) {
        this.multiTaskRunner = multiTaskRunner;
        this.singleOrderFetcher = singleOrderFetcher;
    }

    public List<Order> fetchAll(List<String> orderIds) {
        List<Supplier<Order>> tasks =
            orderIds
                .stream()
                .map(this::getOrderSupplier)
                .toList();

        return multiTaskRunner.runTasks(tasks);
    }

    private Supplier<Order> getOrderSupplier(String orderId) {
        return () -> singleOrderFetcher.retrieveSingleOrder(orderId);
    }

}
