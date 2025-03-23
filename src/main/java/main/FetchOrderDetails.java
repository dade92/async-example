package main;

import order.Order;
import order.OrderRepository;
import order.RestOrderRepository;
import user.RestUserRepository;
import user.User;
import user.UserRepository;

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

    CompletableFuture<Order> getOrder(String orderId) {
        return CompletableFuture.supplyAsync(
            () -> {
                return orderRepository.retrieve(orderId);
            }
        );
    }

    CompletableFuture<User> getUser(String token) {
        return CompletableFuture.supplyAsync(
            () -> {
                return userRepository.findByToken(token);
            }
        );
    }

    public void run() {
//        TODO
    }

    public static void main(String[] args) {
        FetchOrderDetails fetchOrderDetails = new FetchOrderDetails(
            new RestOrderRepository(),
            new RestUserRepository()
        );

        fetchOrderDetails.run();
    }
}