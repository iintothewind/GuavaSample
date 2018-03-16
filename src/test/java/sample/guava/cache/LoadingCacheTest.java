package sample.guava.cache;

import com.google.common.cache.*;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;


public class LoadingCacheTest {
    private final Logger log = LogManager.getLogger();

    @Test
    public void getUnchecked() {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };

        LoadingCache<String, String> cache = CacheBuilder.newBuilder().build(loader);
        assertThat(cache.size()).isEqualTo(0);

        try {
            assertThat(cache.get("simple test")).isEqualTo("SIMPLE TEST");
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // prefer using getUnchecked() because no checked exceptions will be thrown in loader
        assertThat(cache.getUnchecked("simple test")).isEqualTo("SIMPLE TEST");
    }

    @Test
    public void get() {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                if (Pattern.compile("[a-zA-Z]+").matcher(key).matches()) {
                    return key.toUpperCase();
                } else {
                    throw new InvalidCacheLoadException(String.format("%s contains non-alphabetical letters", key));
                }
            }
        };

        LoadingCache<String, String> cache = CacheBuilder.newBuilder().build(loader);
        assertThat(cache.size()).isEqualTo(0);

        try {
            // loader implemented to throw exception, get() is prefered
            assertThat(cache.get("abc")).isEqualTo("ABC");
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void weakKeyValues() {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };

        LoadingCache<String, String> cache = CacheBuilder.newBuilder().weakKeys().weakValues().build(loader);
        assertThat(cache.getUnchecked("simple test")).isEqualTo("SIMPLE TEST");
    }

    @Test
    public void softKeyValues() {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };

        LoadingCache<String, String> cache = CacheBuilder.newBuilder().softValues().build(loader);
    }

    @Test
    public void evictByLRU() {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };
        //Allow the garbage collector to collect cached values
        //VMs "bias against clearing recently-created or recently-used soft references"
        //But in practice "SoftReferences will always be kept for at least one GC after their last access"
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().weakKeys().weakValues().maximumSize(200).build(loader);
        assertThat(cache.getUnchecked("simple test")).isEqualTo("SIMPLE TEST");
    }

    @Test
    public void evictByWeigher() {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };
        //Weight is only measured once, when an entry is added to the cache
        //Weight is only used to determine whether the cache is over capacity; not for selecting what to evict
        Weigher<String, String> weighByLength = (key, value) -> value.length();

        LoadingCache<String, String> cache = CacheBuilder.newBuilder().weakKeys().weakValues().maximumWeight(200).weigher(weighByLength).build(loader);
        assertThat(cache.getUnchecked("simple test")).isEqualTo("SIMPLE TEST");
    }

    @Test
    public void timeToIdle() {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };
        //Elements will expire after the specified time has elapsed since the most recent access
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().expireAfterAccess(2, TimeUnit.MINUTES).maximumSize(200).build(loader);
    }

    @Test
    public void timeToLive() {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };
        //Elements will expire after the specified time has elapsed since the entry's creation or update
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().expireAfterAccess(2, TimeUnit.MINUTES).expireAfterWrite(2, TimeUnit.MINUTES).maximumSize(200).build(loader);
    }

    @Test
    public void cacheStats() {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };
        //Weight is only measured once, when an entry is added to the cache
        //Weight is only used to determine whether the cache is over capacity; not for selecting what to evict
        Weigher<String, String> weighByLength = (key, value) -> value.length();

        LoadingCache<String, String> cache = CacheBuilder.newBuilder().weakKeys().weakValues().maximumWeight(200).weigher(weighByLength).recordStats().build(loader);
        cache.getUnchecked("simple test");
        cache.getUnchecked("apple");
        cache.getUnchecked("simple test");
        cache.getUnchecked("brother");
        cache.getUnchecked("cat");
        cache.getUnchecked("cat");
        cache.getUnchecked("dog");
        cache.getUnchecked("simple test");

        CacheStats stats = cache.stats();
        log.info("hitRate: {}", stats.hitRate());
        log.info("missRate: {}", stats.missRate());
        log.info("loadSuccessCount: {}", stats.loadSuccessCount());
        log.info("averageLoadPenalty: {}", stats.averageLoadPenalty());

        cache.getUnchecked("dog");
        cache.getUnchecked("apple");
        cache.getUnchecked("rat");
        cache.getUnchecked("duct");
        cache.getUnchecked("cat");
        cache.getUnchecked("cat");
        cache.getUnchecked("dog");
        cache.getUnchecked("simple test");

        log.info("hitRate: {}", cache.stats().hitRate());
        log.info("missRate: {}", cache.stats().missRate());
        log.info("loadSuccessCount: {}", cache.stats().loadSuccessCount());
        log.info("totalLoadTime: {}", cache.stats().totalLoadTime());

        CacheStats variance = cache.stats().minus(stats);
        log.info("hitRate: {}", variance.hitRate());
        log.info("missRate: {}", variance.missRate());
        log.info("loadSuccessCount: {}", variance.loadSuccessCount());
        log.info("totalLoadTime: {}", variance.totalLoadTime());
    }

    @Test
    public void configuration() {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };
        LoadingCache<String, String> cache = CacheBuilder.from("maximumSize=200,expireAfterWrite=2m").build(loader);
    }

    @Test
    public void removalNotification() {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };
        RemovalListener<String, String> listener = notification -> {
            if (notification.wasEvicted()) {
                log.info("removed {} -> {} , removalCause: {}", notification.getKey(), notification.getValue(), notification.getCause().name());
            }
        };
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(2).removalListener(listener).build(loader);
        cache.getUnchecked("simple test");
        cache.getUnchecked("apple");
        cache.getUnchecked("simple test");
        cache.getUnchecked("brother");
        cache.getUnchecked("cat");
        cache.getUnchecked("cat");
        log.info("cache.size() : {}", cache.size());
    }

    @Test
    public void refresh() throws InterruptedException {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };
        RemovalListener<String, String> listener = notification -> {
            if (notification.wasEvicted()) {
                log.info("removed {} -> {} , removalCause: {}", notification.getKey(), notification.getValue(), notification.getCause().name());
            }
        };
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(2).expireAfterWrite(3, TimeUnit.SECONDS).removalListener(listener).build(loader);
        cache.getUnchecked("test");
        cache.getUnchecked("apple");
        TimeUnit.SECONDS.sleep(1);
        cache.refresh("test");
        TimeUnit.SECONDS.sleep(2);
        cache.getUnchecked("tiger");
        log.info("cache.size(): {}", cache.size());
    }

    @Test
    public void autoRefresh() throws InterruptedException {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };

        RemovalListener<String, String> listener = notification -> {
            if (notification.wasEvicted()) {
                log.info("removed {} -> {} , removalCause: {}", notification.getKey(), notification.getValue(), notification.getCause().name());
            }
        };
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(2).expireAfterWrite(3, TimeUnit.SECONDS).refreshAfterWrite(2, TimeUnit.SECONDS).removalListener(listener).build(loader);
        cache.getUnchecked("test");
        cache.getUnchecked("apple");
        TimeUnit.SECONDS.sleep(1);
        cache.getUnchecked("tiger");
        TimeUnit.SECONDS.sleep(1);
        cache.getUnchecked("lion");
        TimeUnit.SECONDS.sleep(1);
        cache.getUnchecked("panther");
        TimeUnit.SECONDS.sleep(1);
        cache.getUnchecked("leo");
    }

    @Test
    public void asyncRefresh() throws InterruptedException {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }

            //Avoid blocking any user threads by providing an asynchronous CacheLoader.reloadimplementation
            @Override
            public ListenableFuture<String> reload(String key, String oldValue) {
                ListenableFutureTask<String> task = ListenableFutureTask.create(() -> load(key));
                Executors.newCachedThreadPool().execute(task);
                return task;
            }
        };

        RemovalListener<String, String> listener = notification -> {
            if (notification.wasEvicted()) {
                log.info("removed {} -> {} , removalCause: {}", notification.getKey(), notification.getValue(), notification.getCause().name());
            }
        };
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(2).expireAfterWrite(3, TimeUnit.SECONDS).refreshAfterWrite(2, TimeUnit.SECONDS).removalListener(listener).build(loader);
        cache.getUnchecked("test");
        cache.getUnchecked("apple");
        TimeUnit.SECONDS.sleep(1);
        cache.getUnchecked("tiger");
        TimeUnit.SECONDS.sleep(1);
        cache.getUnchecked("lion");
        TimeUnit.SECONDS.sleep(1);
        cache.getUnchecked("panther");
        TimeUnit.SECONDS.sleep(1);
        cache.getUnchecked("leo");
    }

    @Test
    public void getWhenCacheMiss() throws ExecutionException {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };

        RemovalListener<String, String> listener = notification -> {
            if (notification.wasEvicted()) {
                log.info("removed {} -> {} , removalCause: {}", notification.getKey(), notification.getValue(), notification.getCause().name());
            }
        };
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(2).expireAfterWrite(3, TimeUnit.SECONDS).refreshAfterWrite(2, TimeUnit.SECONDS).removalListener(listener).build(loader);
        final String key = "Apple";
        final String value = cache.get(key, key::toLowerCase);
        assertThat(value).isEqualTo("apple");
    }

    @Test
    public void nonloadingCache() {
        //nonloading cache supports: put, getIfPresent, and get(K, Callable)
        Cache<String, String> cache = CacheBuilder.newBuilder().build();
        cache.put("add", "plus");
        assertThat(cache.getIfPresent("test")).isNullOrEmpty();
        assertThat(cache.getIfPresent("add")).isEqualTo("plus");
    }

    @Test
    public void disableCaching() {
        // The canonical way to do this is using maximumSize(0)
        Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(0).build();
    }

    @Test
    public void mapView() {
        Cache<String, String> cache = CacheBuilder.newBuilder().build();
        cache.put("add", "plus");
        // All ConcurrentMapwrite operations are implemented
        // However the canonical way to write to a cache is still with a CacheLoaderor a Callable
        cache.asMap();
    }


}
