package taskrunner;

public class OrderFetcher {
    public String fetch(String orderId) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Task " + orderId + " was interrupted");
        }
        String data = "data-from-task-" + orderId;
        return buildResult(orderId, data);
    }

    private static String buildResult(String orderId, String data) {
        return "Task " + orderId + " produced " + data +
            " on " + Thread.currentThread().getName();
    }
}
