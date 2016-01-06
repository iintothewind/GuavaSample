package sample.guava.cache;

import com.google.common.collect.MapMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sample.guava.bean.Person;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class MapMakerTest {
    Logger log = LogManager.getLogger();

    private Person studentJohn = null;
    private Person yongPeter = null;
    private Person olderPeter = null;
    private Person boxerJohn = null;

    @Before
    public void setUp() {
        studentJohn = new Person("John", 20, "Student");
        yongPeter = new Person("Peter", 21, "Student");
        olderPeter = new Person("Peter", 22, "Student");
        boxerJohn = new Person("John", 23, "Boxer");
    }

    @Test
    public void weakReference() {
        Object referent = new Object();
        WeakReference<Object> weakReference = new WeakReference<Object>(referent);

        Assert.assertSame(referent, weakReference.get());

        referent = null;
        System.gc();
        Assert.assertNull(weakReference.get());
    }

    @Test
    public void weakHashMap() throws InterruptedException {
        Map<Object, Object> weakHashMap = new WeakHashMap<Object, Object>();
        Object key = new Object();
        Object value = new Object();
        weakHashMap.put(key, value);
        Assert.assertTrue(weakHashMap.containsValue(value));

        key = null;
        System.gc();
        TimeUnit.MILLISECONDS.sleep(1000);
        Assert.assertFalse(weakHashMap.containsValue(value));
    }

    @Test
    public void testWeakKeys() throws InterruptedException {
        ConcurrentMap<Object, Person> map = new MapMaker().weakKeys().makeMap();
        Object key = new Object();
        map.put(key, studentJohn);
        this.log.debug("map.get(key) == " + map.get(key));
        this.log.debug("map.get(test) == " + map.get("test"));
        key = null;
        System.gc();
        TimeUnit.MILLISECONDS.sleep(1000);
        Assert.assertNull(map.get(key));
        // entry is recycled, size remains the same, probably a bug.
        this.log.debug("map.get(key) == " + map.size());
        map.forEach((k, v) -> log.debug(String.format("k=%s,v=%s", k, v)));
        Assert.assertFalse(map.containsKey(key));
        Assert.assertFalse(map.containsValue(studentJohn));
    }

    @Test
    public void testWeakValues() throws InterruptedException {
        ConcurrentMap<Object, Object> map = new MapMaker().weakValues().makeMap();
        Object key = new Object();

        map.put(key, studentJohn);
        this.log.debug("map.get(key) == " + map.get(key));
        this.log.debug("map.get(test) == " + map.get("test"));
        studentJohn = null;
        System.gc();
        TimeUnit.MILLISECONDS.sleep(1000);
        Assert.assertFalse(map.containsValue(studentJohn));
        Assert.assertNull(map.get(key));
        Assert.assertEquals(0, map.size());
    }
}
