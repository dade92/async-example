package order;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class RestOrderRepository implements OrderRepository {
    @Override
    public List<Order> retrieveByUserToken(String orderId) {
        try {
            Thread.sleep(2000);
            return Arrays.asList(
                new Order("123", BigDecimal.TEN),
                new Order("456", new BigDecimal("50"))
            );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
