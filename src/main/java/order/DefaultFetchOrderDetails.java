package order;

import user.User;
import user.UserRepository;
import utils.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class DefaultFetchOrderDetails implements FetchOrderDetails {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ExecutorService executor;

    public DefaultFetchOrderDetails(
        OrderRepository orderRepository,
        UserRepository userRepository,
        ExecutorService executor
    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.executor = executor;
    }

    private CompletableFuture<List<Order>> getOrders(String token) {
        return CompletableFuture.supplyAsync(
            () -> {
                Logger.log("Starting getOrders");
                List<Order> order = orderRepository.retrieveByUserToken(token);
                Logger.log("Finishing getOrders");
                return order;
            },
            executor
        );
    }

    private CompletableFuture<User> getUser(String token) {
        return CompletableFuture.supplyAsync(
            () -> {
                Logger.log("Starting getUser");
                User user = userRepository.findByToken(token);
                Logger.log("Finishing getUser");
                return user;
            },
            executor
        );
    }

    public Details fetch(String token) {
        return getOrders(token)
            .thenCombine(
                getUser(token),
                DefaultFetchOrderDetails::combineUserWithOrders
            )
            .join();
    }

    public CompletableFuture<Details> fetchAsync(String token) {
        return getOrders(token)
            .thenCombine(
                getUser(token),
                DefaultFetchOrderDetails::combineUserWithOrders
            );
    }

    private static Details combineUserWithOrders(List<Order> orders, User user) {
        Logger.log("Completed both get orders and get user");
        return new Details(
            user, orders
        );
    }

}