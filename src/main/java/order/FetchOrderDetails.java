package order;

import java.util.concurrent.CompletableFuture;

public interface FetchOrderDetails {
    Details fetch(String token);

    CompletableFuture<Details> fetchAsync(String token);
}