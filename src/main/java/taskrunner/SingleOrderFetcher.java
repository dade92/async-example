package taskrunner;

public class SingleOrderFetcher {
    public Order fetch(String orderId) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Task " + orderId + " was interrupted");
        }
        return buildResult(orderId);
    }

    private static Order buildResult(String orderId) {
        return new Order(orderId);
    }
}
