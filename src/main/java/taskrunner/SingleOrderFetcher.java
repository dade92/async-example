package taskrunner;

public class SingleOrderFetcher {
    public Order fetch(String orderId) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Task " + orderId + " was interrupted");
        }
        String data = "data-from-task-" + orderId;
        return buildResult(orderId, data);
    }

    private static Order buildResult(String orderId, String data) {
        return new Order(orderId);
    }
}
