package order;

import java.math.BigDecimal;
import java.util.List;

import static java.util.List.of;

public class RestOrderRepository implements OrderRepository {
    @Override
    public List<Order> retrieveByUserToken(String token) {
        try {
            Thread.sleep(2000);
            return of(
                new Order("123", BigDecimal.TEN),
                new Order("456", new BigDecimal("50"))
            );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order retrieveSingleOrder(String orderId) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Task " + orderId + " was interrupted");
        }
        return buildResult(orderId);
    }

    private static Order buildResult(String orderId) {
        return new Order(orderId, BigDecimal.TEN);
    }
}
