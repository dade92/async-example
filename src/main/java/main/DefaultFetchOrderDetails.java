package main;

import order.*;
import user.RestUserRepository;
import user.User;
import user.UserRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DefaultFetchOrderDetails implements FetchOrderDetails {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public DefaultFetchOrderDetails(
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

    public Details fetch(String token) {
        return getOrders(token)
            .thenCombine(
                getUser(token),
                (orders, user) -> {
                    log("Completed both get orders and get user");
                    return new Details(
                        user, orders
                    );
                })
            .join();
    }

    public CompletableFuture<Details> fetchAsync(String token) {
        return getOrders(token)
            .thenCombine(
                getUser(token),
                (orders, user) -> {
                    log("Completed both get orders and get user");
                    return new Details(
                        user, orders
                    );
                });
    }

    public static void main(String[] args) {
        DefaultFetchOrderDetails defaultFetchOrderDetails = new DefaultFetchOrderDetails(
            new RestOrderRepository(),
            new RestUserRepository()
        );

//        Details details = defaultFetchOrderDetails.fetch("XXX");
//        System.out.println("Details retrieved: " + details);

        defaultFetchOrderDetails.fetchAsync("XXX").thenAccept(
            (details) -> {
                System.out.println("Details retrieved: " + details);
            }
        );
    }
}