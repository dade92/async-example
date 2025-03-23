package main;

import order.*;
import user.RestUserRepository;
import user.User;
import user.UserRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    CompletableFuture<List<Order>> getOrders(String token, ExecutorService executor) {
        return CompletableFuture.supplyAsync(
            () -> {
                log("Starting getOrders");
                List<Order> order = orderRepository.retrieveByUserToken(token);
                log("Finishing getOrders");
                return order;
            },
            executor
        );
    }

    CompletableFuture<User> getUser(String token, ExecutorService executor) {
        return CompletableFuture.supplyAsync(
            () -> {
                log("Starting getUser");
                User user = userRepository.findByToken(token);
                log("Finishing getUser");
                return user;
            },
            executor
        );
    }

    private void log(String message) {
        System.out.println(LocalTime.now() + " | " + message);
    }

    public Details fetch(String token, ExecutorService executor) {
        return getOrders(token, executor)
            .thenCombine(
                getUser(token, executor),
                (orders, user) -> {
                    log("Completed both get orders and get user");
                    return new Details(
                        user, orders
                    );
                })
            .join();
    }

    public CompletableFuture<Details> fetchAsync(String token, ExecutorService executor) {
        return getOrders(token, executor)
            .thenCombine(
                getUser(token, executor),
                (orders, user) -> {
                    log("Completed both get orders and get user");
                    return new Details(
                        user, orders
                    );
                });
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        DefaultFetchOrderDetails defaultFetchOrderDetails = new DefaultFetchOrderDetails(
            new RestOrderRepository(),
            new RestUserRepository()
        );

//        Details details = defaultFetchOrderDetails.fetch("XXX", executor);
//        System.out.println("Details retrieved: " + details);

        defaultFetchOrderDetails.fetchAsync("XXX", executor)
            .thenAccept(
                (details) -> System.out.println("Details retrieved: " + details)
            )
            .thenRun(executor::shutdown);
    }
}