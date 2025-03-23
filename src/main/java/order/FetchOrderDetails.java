package order;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public interface FetchOrderDetails {
    Details fetch(String token, ExecutorService executor);

    CompletableFuture<Details> fetchAsync(String token, ExecutorService executor);
}