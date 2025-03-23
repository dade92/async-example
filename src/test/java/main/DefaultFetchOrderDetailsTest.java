package main;

import order.Details;
import order.Order;
import order.OrderRepository;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.User;
import user.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultFetchOrderDetailsTest {

    private final Mockery context = new Mockery() {{
        setThreadingPolicy(new Synchroniser());
    }};

    private final OrderRepository orderRepository = context.mock(OrderRepository.class);
    private final UserRepository userRepository = context.mock(UserRepository.class);
    private DefaultFetchOrderDetails fetchOrderDetails;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @BeforeEach
    public void setUp() {
        fetchOrderDetails = new DefaultFetchOrderDetails(orderRepository, userRepository, executor);
    }

    @AfterEach
    public void tearDown() {
        executor.shutdown();
    }

    @Test
    public void fetchHappyPath() {
        String token = "token";
        User user = new User("XXX", "JDoe");
        List<Order> orders = List.of(new Order("order1", BigDecimal.TEN), new Order("order2", BigDecimal.TEN));

        context.checking(new Expectations() {{
            oneOf(orderRepository).retrieveByUserToken(token);
            will(returnValue(orders));

            oneOf(userRepository).findByToken(token);
            will(returnValue(user));
        }});

        Details result = fetchOrderDetails.fetch(token);

        assertEquals(
            result,
            new Details(
                user,
                orders
            )
        );
    }

    @Test
    public void fetchAsyncHappyPath() throws Exception {
        String token = "token";
        User user = new User("AAA", "Alice");
        List<Order> orders = List.of(new Order("orderX", BigDecimal.TEN));

        context.checking(new Expectations() {{
            oneOf(orderRepository).retrieveByUserToken(token);
            will(returnValue(orders));

            oneOf(userRepository).findByToken(token);
            will(returnValue(user));
        }});

        Details result = fetchOrderDetails.fetchAsync(token).join();

        assertEquals(
            result,
            new Details(
                user,
                orders
            )
        );

    }
}
