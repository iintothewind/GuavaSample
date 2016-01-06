package sample.guava.concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SemaphoreTest {
    private final Logger log = LogManager.getLogger(this.getClass().getName());
    private final BoundedSet<String> boundedSet = new BoundedSet<>(5);

    @Test
    public void randomAddRemove() {
        ExecutorService pool = Executors.newCachedThreadPool();

        for (int i = 0; i < 100; i++) {
            final int random = new Random().nextInt(5);
            if (i % 2 != 0) {
                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.debug("boundedSet == " + boundedSet + ", " + random + " would be added if possible.");
                        boundedSet.add(String.valueOf(random));
                    }
                });
            } else {
                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.debug("boundedSet == " + boundedSet + ", " + random + " would be removed if existing.");
                        boundedSet.remove(String.valueOf(random));
                    }
                });
            }
        }

        try {
            pool.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            pool.shutdown();
        }
        log.debug("boundedSet == " + boundedSet);
    }
}
