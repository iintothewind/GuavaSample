package sample.guava.concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UncaughtTest {

    @Before
    public void setUp() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("System is shutting down.");
            }
        });
    }

    @Test
    public void testErrorHandlerThread() throws InterruptedException {
        ErrorHandlerThread test = new ErrorHandlerThread(5, 0);
        test.start();
        test.join();
    }

    @Test
    public void testRunByPool() throws InterruptedException {
        InPool test = new InPool(5, 0);
        ExecutorService pool = Executors.newCachedThreadPool();

        try {
            pool.execute(test);
        } finally {
            pool.shutdown();
            pool.awaitTermination(1, TimeUnit.SECONDS);
        }

    }

}

final class InPool implements Runnable {
    Logger log = LogManager.getLogger(this.getClass().getName());
    private final int numerator;
    private final int denominator;

    public InPool(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    private void exit() {
        this.log.debug("numerator == " + numerator + ", denominator == " + denominator);
    }

    @Override
    public void run() {
        try {
            int result = numerator / denominator;
            this.log.debug("numerator/denominator == " + result);
        } catch (Throwable t) {
            if (t instanceof RuntimeException) {
                log.error("RuntimeException: " + t.getClass().getCanonicalName() + " occured while executing thread: "
                        + Thread.currentThread().getName() + " with error message: " + t.getMessage());
            } else {
                throw new IllegalStateException("Error occured during executing thread", t);
            }
        } finally {
            this.exit();
        }
    }
}

final class ErrorHandlerThread extends Thread implements UncaughtExceptionHandler {
    Logger log = LogManager.getLogger(this.getClass().getName());
    private final int numerator;
    private final int denominator;

    public ErrorHandlerThread(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public synchronized void start() {
        super.start();
        this.setUncaughtExceptionHandler(this);
    }

    @Override
    public void run() {
        int result = numerator / denominator;
        this.log.debug("numerator/denominator == " + result);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.warn("Error occured while executing thread: " + t.getName() + " with error message: " + e.getMessage());
        this.log.error("numerator == " + numerator + ", denominator == " + denominator);
    }

}