package main;

import order.Details;
import order.Order;
import order.OrderRepository;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.User;
import user.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultFetchOrderDetailsTest {

    private Mockery context;
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private DefaultFetchOrderDetails fetchOrderDetails;
    private ExecutorService executor;

    @BeforeEach
    public void setUp() {
        context = new Mockery() {{
            setThreadingPolicy(new Synchroniser());
        }};
        orderRepository = context.mock(OrderRepository.class);
        userRepository = context.mock(UserRepository.class);
        fetchOrderDetails = new DefaultFetchOrderDetails(orderRepository, userRepository);
        executor = Executors.newSingleThreadExecutor();
    }

    @AfterEach
    public void tearDown() {
        executor.shutdown();
    }

    @Test
    public void testFetchReturnsCombinedDetails() {
        String token = "token";
        User mockedUser = new User("XXX", "JDoe");
        List<Order> mockedOrders = List.of(new Order("order1", BigDecimal.TEN), new Order("order2", BigDecimal.TEN));

        context.checking(new Expectations() {{
            oneOf(orderRepository).retrieveByUserToken(token);
            will(returnValue(mockedOrders));

            oneOf(userRepository).findByToken(token);
            will(returnValue(mockedUser));
        }});

        Details result = fetchOrderDetails.fetch(token, executor);

        Assertions.assertEquals(
            result,
            new Details(
                mockedUser,
                mockedOrders
            )
        );
    }

    @Test
    public void testFetchAsyncReturnsCombinedDetails() throws Exception {
        String token = "token";
        User user = new User("AAA", "Alice");
        List<Order> orders = List.of(new Order("orderX", BigDecimal.TEN));

        context.checking(new Expectations() {{
            oneOf(orderRepository).retrieveByUserToken(token);
            will(returnValue(orders));

            oneOf(userRepository).findByToken(token);
            will(returnValue(user));
        }});

        Details result = fetchOrderDetails.fetchAsync(token, executor).join();

        Assertions.assertEquals(
            result,
            new Details(
                user,
                orders
            )
        );

    }
}
