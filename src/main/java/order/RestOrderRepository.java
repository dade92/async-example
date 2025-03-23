package order;

import java.math.BigDecimal;

public class RestOrderRepository implements OrderRepository {
    @Override
    public Order retrieve(String orderId) {
        try {
            Thread.sleep(2000);
            return new Order("123", BigDecimal.TEN);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
