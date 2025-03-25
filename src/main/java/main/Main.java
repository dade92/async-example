package main;

import experimental.Experiment;
import order.DefaultFetchOrderDetails;
import order.RestOrderRepository;
import user.RestUserRepository;
import utils.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        Logger.log("Main starts");

        ExecutorService executor = Executors.newFixedThreadPool(4);

//        executeFetchDetails(executor);

        executeAsyncKotlin(executor);

        Logger.log("Main ends");
    }

    private static void executeFetchDetails(ExecutorService executor) {
        DefaultFetchOrderDetails defaultFetchOrderDetails = new DefaultFetchOrderDetails(
            new RestOrderRepository(),
            new RestUserRepository(),
            executor
        );

//        Details details = defaultFetchOrderDetails.fetch("XXX);
//        System.out.println("Details retrieved: " + details);

        defaultFetchOrderDetails.fetchAsync("XXX")
            .thenAccept(
                (details) -> Logger.log("Details retrieved: " + details)
            )
            .thenRun(executor::shutdown);
    }

    private static void executeAsyncKotlin(ExecutorService executor) {
        new Experiment(
            new RestOrderRepository(),
            new RestUserRepository(),
            executor
        ).run("my-token")
            .thenAccept(
                details -> Logger.log("Final details: " + details)
            )
            .thenRun(executor::shutdown);
    }
}
