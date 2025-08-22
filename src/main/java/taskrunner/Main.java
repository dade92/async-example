package taskrunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.List.of;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        SingleOrderFetcher singleOrderFetcher = new SingleOrderFetcher();
        MultiTaskRunner<String> multiTaskRunner = new MultiTaskRunner<>(executor);

        OrdersFetcher ordersFetcher = new OrdersFetcher(
            multiTaskRunner,
            singleOrderFetcher
        );

        try {
            ordersFetcher.fetchAll(of("123", "456", "789"));
        } finally {
            executor.shutdown();
        }
    }
}
