package taskrunner;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        SingleOrderFetcher singleOrderFetcher = new SingleOrderFetcher();
        MultiTaskRunner multiTaskRunner = new MultiTaskRunner(executor);

        OrdersFetcher ordersFetcher = new OrdersFetcher(
            multiTaskRunner,
            singleOrderFetcher
        );

        try {
            ordersFetcher.fetchAll(List.of("123", "456", "789"));
        } finally {
            executor.shutdown();
        }
    }
}
