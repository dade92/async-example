package taskrunner;

import order.Order;
import order.OrderRepository;
import order.RestOrderRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.List.of;

public class Main {
    private static final List<String> ORDER_IDS = of("123", "456", "789");

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        OrderRepository orderRepository = new RestOrderRepository();
        MultiTaskRunner<Order> multiTaskRunner = new MultiTaskRunner<>(executor);

        OrdersFetcher ordersFetcher = new OrdersFetcher(
            multiTaskRunner,
            orderRepository
        );

        try {
            System.out.println("Fetching orders concurrently:");
            long startTime = System.nanoTime();
            List<Order> orders = ordersFetcher.fetchAll(ORDER_IDS);
            long endTime = System.nanoTime();
            long durationMs = (endTime - startTime) / 1_000_000;
            System.out.println("Computation took " + durationMs + " ms");
            orders.forEach(System.out::println);
        } finally {
            executor.shutdown();
        }

        System.out.println("Fetching orders sequentially:");
        long startTime = System.nanoTime();
        for (String orderId : ORDER_IDS) {
            Order order = orderRepository.retrieveSingleOrder(orderId);
            System.out.println(order);
        }
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        System.out.println("Computation took " + durationMs + " ms");
    }
}
