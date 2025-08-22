package taskrunner;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        List<String> orderIds = List.of("123", "456", "789");

        OrderFetcher orderFetcher = new OrderFetcher();

        new OrdersFetcher(
            new MultiTaskRunner(executor),
            orderFetcher
        ).fetchAll(orderIds);
    }
}
