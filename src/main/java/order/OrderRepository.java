package order;

import java.util.List;

public interface OrderRepository {
    List<Order> retrieveByUserToken(String orderId);
}