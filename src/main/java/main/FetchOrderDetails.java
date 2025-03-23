package main;

import order.Order;
import order.OrderRepository;
import order.RestOrderRepository;
import user.RestUserRepository;
import user.User;
import user.UserRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FetchOrderDetails {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public FetchOrderDetails(
        OrderRepository orderRepository,
        UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    CompletableFuture<List<Order>> getOrders(String token) {
        return CompletableFuture.supplyAsync(
            () -> {
                log("Starting getOrders");
                List<Order> order = orderRepository.retrieveByUserToken(token);
                log("Finishing getOrders");
                return order;
            }
        );
    }

    CompletableFuture<User> getUser(String token) {
        return CompletableFuture.supplyAsync(
            () -> {
                log("Starting getUser");
                User user = userRepository.findByToken(token);
                log("Finishing getUser");
                return user;
            }
        );
    }

    private void log(String message) {
        System.out.println(LocalTime.now() + " | " + message);
    }

    public void run(String token, String orderId) {
//        TODO
    }

    public static void main(String[] args) {
        FetchOrderDetails fetchOrderDetails = new FetchOrderDetails(
            new RestOrderRepository(),
            new RestUserRepository()
        );

        fetchOrderDetails.run("XXX", "666");
    }
}