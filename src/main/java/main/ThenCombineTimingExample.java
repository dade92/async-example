package main;

import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThenCombineTimingExample {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public CompletableFuture<String> fetchUserId() {
        return CompletableFuture.supplyAsync(() -> {
            log("Started fetching user ID...");
            simulateDelay(1000);
            log("Fetched user ID: 42");
            return "42";
        }, executor);
    }

    public CompletableFuture<String> fetchUserDetails() {
        return CompletableFuture.supplyAsync(() -> {
            log("Started fetching static user details (independent task)...");
            simulateDelay(1500);
            log("Fetched static user details.");
            return "User{name='John Doe'}";
        }, executor);
    }

    public void runIndependently() {
        log("Main thread starts async parallel tasks");

        CompletableFuture<String> futureA = fetchUserId();
        CompletableFuture<String> futureB = fetchUserDetails();

        futureA.thenCombine(futureB, (userId, userDetails) -> {
            return "Combined: ID=" + userId + ", " + userDetails;
        }).thenAccept(result -> {
            log("Final result: " + result);
            executor.shutdown();
        });

        log("Main thread finished setup (non-blocking)");
    }

    public void runTheSecondAfterTheFirst() {
        log("Main thread starts async chain");

        fetchUserId()
            .thenCompose(userId -> fetchUserDetails())
            .thenAccept(userDetails -> {
                log("Final result: " + userDetails);
                executor.shutdown();
            });

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

    public static void main(String[] args) {
        new ThenCombineTimingExample().runTheSecondAfterTheFirst();
    }
}
