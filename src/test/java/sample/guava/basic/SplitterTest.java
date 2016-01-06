package sample.guava.basic;

import com.google.common.base.Splitter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class SplitterTest {
    Logger log = LogManager.getLogger(this.getClass().getName());

    @Test
    public void splitterShow() {
        this.log.debug("Iterable<String> split() == "
                + Splitter.on(',').trimResults().omitEmptyStrings().split(" foo, ,bar, quux,"));
    }

    @Test
    public void fixedLength() {
        this.log.debug("Iterable<String> fixedLength(2).split() == " + Splitter.fixedLength(2).split("abcde"));
    }

    @Test
    public void onPattern() {
        String demo = "Once more into the fray...\nInto the last good fight I'll ever know...\nLive and die on this day...\nLive and die on this day...";
        this.log.debug("Iterable<String> .onPattern() == "
                + Splitter.onPattern("\\.+\\n?").omitEmptyStrings().split(demo));
    }

    @Test
    public void limit() {
        this.log.debug("Iterable<String> limit(3) == " + Splitter.on('-').limit(3).split("a-b-c-d"));
    }

    @Test
    public void withKeyValueSeparator() {
        String maps = "k0=v0,k1=,k2=v2,k3=,k4=v4,k5=,k6=v6,k7=,k8=v8,k9=";
        this.log.debug("Map<String, String> withKeyValueSeparator() == "
                + Splitter.on(',').trimResults().omitEmptyStrings().withKeyValueSeparator("=").split(maps));
    }

    @Test
    public void withKeyValueSeparator1() {
        String maps = "k0=v0,";
        this.log.debug("Map<String, String> withKeyValueSeparator() == "
                + Splitter.on(',').trimResults().omitEmptyStrings().withKeyValueSeparator("=").split(maps));
    }
}
