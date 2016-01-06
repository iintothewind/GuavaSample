package sample.guava.basic;


import com.google.common.base.Strings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class StringsTest {
    Logger log = LogManager.getLogger(this.getClass().getName());

    @Test
    public void commonPrefix() {
        this.log.debug("commonPrefix == " + Strings.commonPrefix("Premier", "Premiere"));
    }

    @Test
    public void commonSuffix() {
        this.log.debug("commonSuffix == " + Strings.commonSuffix("symmetric", "asymmetric"));
    }

    @Test
    public void emptyToNull() {
        Assert.assertEquals(Strings.emptyToNull(""), null);
        Assert.assertEquals(Strings.nullToEmpty(null), "");
        Assert.assertEquals(Strings.isNullOrEmpty(null), true);
        Assert.assertEquals(Strings.isNullOrEmpty(""), true);
    }

    @Test
    public void padEnd() {
        this.log.debug("padEnd == " + Strings.padEnd("4.", 5, '0'));
    }

    @Test
    public void padStart() {
        this.log.debug("padStart == " + Strings.padStart("101", 5, '0'));
    }
}
