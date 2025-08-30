package main;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureChainExample {

    public static void main(String[] args) {
        new CompletableFutureChainExample().performAsyncChainedCalls();
    }

    private final CollaboratorA collaboratorA = new CollaboratorA();
    private final CollaboratorB collaboratorB = new CollaboratorB();

    public void performAsyncChainedCalls() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

//        executeAsync("42", executor)
//            .thenAccept((result) -> System.out.println(result.result()));

        Result join = composeAsync("44", executor).join();

        System.out.println("Result from composeAsync: " + join.result());

        executor.shutdown();
    }

    private CompletableFuture<Result> composeAsync(String id, ExecutorService executor) {
        return CompletableFuture.supplyAsync(() -> collaboratorA.fetchData(id), executor)
            .thenCompose(resultA ->
                CompletableFuture.supplyAsync(() -> collaboratorB.fetchData(id), executor)
            );
    }

    private CompletableFuture<Result> executeAsync(String id, ExecutorService executor) {

        return CompletableFuture
            .supplyAsync(() -> collaboratorA.fetchData(id), executor)
            .thenCombine(
                CompletableFuture.supplyAsync(
                    () -> collaboratorB.fetchData(id), executor
                ),
                (resultA, resultB) -> new Result("Combined: [" + resultA + "] + [" + resultB + "]")
            );
    }

    static class CollaboratorA {
        public Result fetchData(String id) {
            try {
                System.out.println("Calling collaborator A with id " + id);
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            return new Result("Data from A");
        }
    }

    static class CollaboratorB {
        public Result fetchData(String id) {
            try {
                System.out.println("Calling collaborator B with id " + id);
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {
            }
            return new Result("Data from B");
        }
    }

}

record Result(
    String result
) {
}
