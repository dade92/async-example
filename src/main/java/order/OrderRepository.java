package order;

import java.math.BigDecimal;

public interface OrderRepository {
    Order retrieve(String orderId);
}