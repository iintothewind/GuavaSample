package sample.guava.basic;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.Map;

public class JoinerTest {
    Logger log = LogManager.getLogger(this.getClass().getName());

    @Test
    public void appendToWithIterable() {
        StringBuilder appender = Joiner.on("-")
                .appendTo(new StringBuilder(), ImmutableList.of("A", "B", "C", "D", "E"));
        this.log.debug("StringBuilder appender == " + appender);
    }

    @Test
    public void appendToWithArray() {
        StringBuilder appender = Joiner.on("-").appendTo(new StringBuilder(), new String[]{"A", "B", "C", "D", "E"});
        this.log.debug("StringBuilder appender == " + appender);
    }

    @Test
    public void appendToWithObjects() {
        StringBuilder appender = Joiner.on("-").appendTo(new StringBuilder(), "A", "B", "C", "D", "E");
        this.log.debug("StringBuilder appender == " + appender);
    }

    @Test
    public void join() {
        this.log.debug(Joiner.on("-").join("A", "B", "C", "D", "E"));
    }

    @Test
    public void skipNulls() {
        this.log.debug(Joiner.on("-").skipNulls().join("A", null, "C", null, "E"));
    }

    @Test
    public void useForNull() {
        this.log.debug(Joiner.on("-").useForNull("null").join("A", null, "C", null, "E"));
    }

    @Test
    public void withKeyValueSeparator() {
        Map<String, String> hashMap = Maps.newLinkedHashMap();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                hashMap.put("k" + i, "v" + i);
            } else {
                hashMap.put("k" + i, null);
            }
        }
        this.log.debug(Joiner.on(",").withKeyValueSeparator("=").useForNull("").join(hashMap));
    }
}
