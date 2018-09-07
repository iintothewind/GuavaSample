package sample.guava.retry;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.nurkiewicz.asyncretry.AsyncRetryExecutor;
import com.nurkiewicz.asyncretry.RetryExecutor;


public class BasicTest {
    private ScheduledExecutorService scheduler;
    private RetryExecutor executor;

    @Before
    public void setUp() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        executor = new AsyncRetryExecutor(scheduler)
            .retryOn(RuntimeException.class)
            .withExponentialBackoff(50, 1)
            .withMaxRetries(5);
    }

    @After
    public void tearDown() throws InterruptedException {
        scheduler.awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    public void testRetry() {
        executor
            .getWithRetry(() -> Integer.parseInt("12a3"))
            .whenComplete((n, e) -> {
                if (n != null) {
                    System.out.println(n);
                } else {
                    e.printStackTrace();
                }
            });
    }
}
