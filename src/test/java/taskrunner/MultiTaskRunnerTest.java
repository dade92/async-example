package taskrunner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MultiTaskRunnerTest {

    @Mock
    private ExecutorService executorService;

    private MultiTaskRunner<String> taskRunner;

    @BeforeEach
    void setUp() {
        // Configure executor service to run tasks directly in the test thread
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(executorService).execute(any(Runnable.class));

        taskRunner = new MultiTaskRunner<>(executorService);
    }

    @Test
    void runTasks() {
        Supplier<String> supplier1 = mock(Supplier.class);
        Supplier<String> supplier2 = mock(Supplier.class);
        Supplier<String> supplier3 = mock(Supplier.class);

        when(supplier1.get()).thenReturn("Task 1 Result");
        when(supplier2.get()).thenReturn("Task 2 Result");
        when(supplier3.get()).thenReturn("Task 3 Result");

        List<Supplier<String>> taskList = Arrays.asList(supplier1, supplier2, supplier3);

        List<String> actual = taskRunner.runTasks(taskList);

        verify(supplier1, times(1)).get();
        verify(supplier2, times(1)).get();
        verify(supplier3, times(1)).get();

        assertEquals(List.of("Task 1 Result", "Task 2 Result", "Task 3 Result"), actual);
    }
}
