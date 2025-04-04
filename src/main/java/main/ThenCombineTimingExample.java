package main;

import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.allOf;

public class ThenCombineTimingExample {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public CompletableFuture<String> fetchUserId() {
        return CompletableFuture.supplyAsync(
            () -> {
                log("Started fetching user ID...");
                simulateDelay(1000);
                log("Fetched user ID: 42");
                return "42";
            },
            executor
        );
    }

    public CompletableFuture<String> fetchUserDetails() {
        return CompletableFuture.supplyAsync(
            () -> {
                log("Started fetching user details (independent task)...");
                simulateDelay(1500);
                log("Fetched static user details.");
                return "User{name='John Doe'}";
            },
            executor
        );
    }

    public CompletableFuture<String> fetchUserDetails(String userId) {
        return CompletableFuture.supplyAsync(
            () -> {
                log("Started fetching user details for user " + userId);
                simulateDelay(1500);
                log("Fetched user details.");
                return "User{name='John Doe'}";
            },
            executor
        );
    }

    public CompletableFuture<String> fetchOrder() {
        return CompletableFuture.supplyAsync(
            () -> {
                log("Started fetching order (independent task)...");
                simulateDelay(1500);
                log("Fetched order.");
                return "Order=56";
            },
            executor
        );
    }

    public void runIndependently() {
        log("Main thread starts async parallel tasks");

        fetchUserId()
            .thenCombine(
                fetchUserDetails(),
                (userId, userDetails) -> "Combined: ID=" + userId + ", " + userDetails
            )
            .thenAccept(result -> {
                log("Final result: " + result);
                executor.shutdown();
            })
            .thenRun(() -> log("All tasks completed"));

        log("Main thread finished setup (non-blocking)");
    }

    public void runTheSecondAfterTheFirst() {
        log("Main thread starts async chain");

        fetchUserId()
            .thenCompose((userId) -> fetchUserDetails(userId))
            .thenAccept(
                (userDetails) -> {
                    log("Final result: " + userDetails);
                    executor.shutdown();
                }
            )
            .thenRun(() -> log("All tasks completed"));

        log("Main thread finished setup (non-blocking)");
    }

    public void runTheSecondWithFirstAndCollectAllResults() {
        log("Main thread starts async chain");

        fetchUserId()
            .thenAcceptBoth(
                fetchUserDetails(),
                (userId, details) -> {
                    log("Final result: " + userId + " " + details);
                    executor.shutdown();
                }
            )
            .thenRun(() -> log("All tasks completed"));

        log("Main thread finished setup (non-blocking)");
    }

    public void runAll() {
        log("Main thread starts async chain");

        allOf(fetchUserId(), fetchUserDetails(), fetchOrder())
            .thenRun(() -> {
                log("All tasks completed");
                executor.shutdown();
            });

        log("Main thread finished setup (non-blocking)");
    }

    public void runThreeInParallel() {
        log("Main thread starts async chain");

        fetchUserId()
            .thenCombine(
                fetchUserDetails(),
                (userId, userDetails) -> {
                    log("Final result: " + userId + ", " + userDetails);
                    return concat.apply(userId, userDetails);
                }
            )
            .thenAcceptBoth(
                fetchOrder(),
                (details, order) -> log("Final result: " + details + ", " + order)
            )
            .thenRun(executor::shutdown);

        log("Main thread finished setup (non-blocking)");
    }

    public void runAsyncWithTransformations() {
        log("Main thread starts async chain");
        fetchUserId()
            .thenCombine(
                fetchUserDetails(),
                concat
            )
            .thenApply(extracted)
            .thenAccept(acceptResult)
            .thenRun(executor::shutdown);
        log("Main thread finished setup (non-blocking)");
    }

    Consumer<Integer> acceptResult = (Integer length) -> log("Length of the output: " + length);

    Function<String, Integer> extracted = (String result) -> {
        log("Final result: " + result);
        return result.split(" ").length;
    };

    BiFunction<String, String, String> concat = (String userId, String userDetails) -> userId + ", " + userDetails;

    public void runEither() {
        log("Main thread starts async chain");
        fetchUserId()
            .acceptEither(
                fetchUserDetails(),
                (firstResult) -> {
                    log("Final result: " + firstResult);
                }
            )
            .thenRun(executor::shutdown);

        log("Main thread finished setup (non-blocking)");
    }

    private void simulateDelay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    private void log(String message) {
        System.out.println(LocalTime.now() + " | " + message);
    }

}
