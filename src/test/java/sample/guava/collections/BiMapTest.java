package sample.guava.collections;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import sample.guava.bean.Person;

public class BiMapTest {
    Logger log = LogManager.getLogger(this.getClass().getName());

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

    @Test(expected = IllegalArgumentException.class)
    public void createBimap() {
        BiMap<String, String> bimap = HashBiMap.create();
        bimap.put("Student", "John");
        // java.lang.IllegalArgumentException: value already present: John
        bimap.put("Boxer", "John");
    }

    @Test
    public void forcePut() {
        BiMap<String, String> bimap = HashBiMap.create();
        bimap.put("Student", "John");
        bimap.forcePut("Boxer", "John");
        this.log.debug("BiMap<String, String> bimap == " + bimap);
    }

    @Test
    public void inverse() {
        this.log.debug("ImmutableBiMap = " + ImmutableBiMap.of("k1", "v1", "k2", "v2", "k3", "v3").inverse());
    }

}
