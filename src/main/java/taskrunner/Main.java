package taskrunner;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.List.of;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        SingleOrderFetcher singleOrderFetcher = new SingleOrderFetcher();
        MultiTaskRunner<Order> multiTaskRunner = new MultiTaskRunner<>(executor);

        OrdersFetcher ordersFetcher = new OrdersFetcher(
            multiTaskRunner,
            singleOrderFetcher
        );

        List<String> orderIds = of("123", "456", "789");
        try {
            System.out.println("Fetching orders concurrently:");
            long startTime = System.nanoTime();
            List<Order> orders = ordersFetcher.fetchAll(orderIds);
            long endTime = System.nanoTime();
            long durationMs = (endTime - startTime) / 1_000_000;
            System.out.println("Computation took " + durationMs + " ms");
            orders.forEach(System.out::println);
        } finally {
            executor.shutdown();
        }

        System.out.println("Fetching orders sequentially:");
        long startTime = System.nanoTime();
        for (String orderId : orderIds) {
            Order order = singleOrderFetcher.fetch(orderId);
            System.out.println(order);
        }
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        System.out.println("Computation took " + durationMs + " ms");
    }
}
