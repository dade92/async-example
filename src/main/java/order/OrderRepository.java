package order;

import java.util.List;

public interface OrderRepository {
    List<Order> retrieveByUserToken(String token);
    Order retrieveSingleOrder(String orderId);
}