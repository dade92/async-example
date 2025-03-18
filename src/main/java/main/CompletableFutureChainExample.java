package main;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureChainExample {

    private final CollaboratorA collaboratorA = new CollaboratorA();
    private final CollaboratorB collaboratorB = new CollaboratorB();

    public void performAsyncChainedCalls() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executeAsync(executor)
            .thenAccept((result) -> System.out.println(result.result()));

        executor.shutdown();
    }

    private CompletableFuture<Result> executeAsync(ExecutorService executor) {
        return CompletableFuture
            .supplyAsync(() -> collaboratorA.fetchData(), executor)
            .thenCombine(
                CompletableFuture.supplyAsync(
                    collaboratorB::fetchData, executor),
                (resultA, resultB) -> new Result("Combined: [" + resultA + "] + [" + resultB + "]")
            );
    }

    public static void main(String[] args) {
        CompletableFutureChainExample example = new CompletableFutureChainExample();
        example.performAsyncChainedCalls();
    }

    static class CollaboratorA {
        public Result fetchData() {
            try {
                System.out.println("Calling collaborator A");
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            return new Result("Data from A");
        }
    }

    static class CollaboratorB {
        public Result fetchData() {
            try {
                System.out.println("Calling collaborator B");
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
