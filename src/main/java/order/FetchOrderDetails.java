package order;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public interface FetchOrderDetails {
    Details fetch(String token);

    CompletableFuture<Details> fetchAsync(String token);
}