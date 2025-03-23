package main;

import experimental.Experiment;
import order.DefaultFetchOrderDetails;
import order.RestOrderRepository;
import user.RestUserRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
//
//        DefaultFetchOrderDetails defaultFetchOrderDetails = new DefaultFetchOrderDetails(
//            new RestOrderRepository(),
//            new RestUserRepository(),
//            executor
//        );
//
////        Details details = defaultFetchOrderDetails.fetch("XXX);
////        System.out.println("Details retrieved: " + details);
//
//        defaultFetchOrderDetails.fetchAsync("XXX")
//            .thenAccept(
//                (details) -> System.out.println("Details retrieved: " + details)
//            )
//            .thenRun(executor::shutdown);

//        new ThenCombineTimingExample().runThreeInParallel();
//        new CompletableFutureChainExample().performAsyncChainedCalls();
        new Experiment(
            new RestOrderRepository(),
            new RestUserRepository(),
            executor
        ).run();
    }
}
