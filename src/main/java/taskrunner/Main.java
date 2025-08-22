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

        try {
            List<Order> orders = ordersFetcher.fetchAll(of("123", "456", "789"));
            orders.forEach(System.out::println);
        } finally {
            executor.shutdown();
        }
    }
}
