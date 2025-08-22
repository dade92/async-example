package taskrunner;

import order.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdersFetcherTest {

    @Mock
    private MultiTaskRunner<Order> multiTaskRunner;

    @Mock
    private SingleOrderFetcher singleOrderFetcher;

    private OrdersFetcher ordersFetcher;

    @BeforeEach
    void setUp() {
        ordersFetcher = new OrdersFetcher(multiTaskRunner, singleOrderFetcher);
    }

    @Test
    void fetchAll() {
        List<String> orderIds = List.of("order-1", "order-2", "order-3");

        when(multiTaskRunner.runTasks(anyList())).thenReturn(
            List.of(
                new Order("order-1", BigDecimal.TEN),
                new Order("order-2", BigDecimal.TEN),
                new Order("order-3", BigDecimal.TEN))
        );

        List<Order> orders = ordersFetcher.fetchAll(orderIds);

        assertEquals(List.of(
                new Order("order-1", BigDecimal.TEN),
                new Order("order-2", BigDecimal.TEN),
                new Order("order-3", BigDecimal.TEN)),
            orders);
    }

}
