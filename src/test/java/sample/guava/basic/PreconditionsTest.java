package sample.guava.basic;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.Objects;

public class PreconditionsTest {
    Logger log = LogManager.getLogger(this.getClass().getName());

    @Test(expected = IllegalArgumentException.class)
    public void checkArgument() {
        int a = 1, b = 2;
        Preconditions.checkArgument(a == b);
        Preconditions.checkArgument(a == b, "a is not equal to b");
        Preconditions.checkArgument(a == b, "%s is not equal to %s", a, b);
    }

    @Test(expected = IllegalStateException.class)
    public void checkState() {
        String state = "OFF";
        Preconditions.checkState(Optional.fromNullable(state).or("OFF").equals("ON"));
        Preconditions.checkState(Optional.fromNullable(state).or("OFF").equals("ON"), "state is not ON");
        Preconditions.checkArgument(Optional.fromNullable(state).or("OFF").equals("ON"), "%s is not %s", "state", "ON");
    }

    @Test(expected = NullPointerException.class)
    public void checkNonNull() {
        Preconditions.checkNotNull(null);
        // Preconditions.checkNotNull(null, "input parameter is null");
        // Preconditions.checkNotNull(null, "%s is null", "input parameter");
        // Objects.requireNonNull(null, "input parameter is null");
    }

    @Test(expected = NullPointerException.class)
    public void jdk7CheckNonNull() {
        Objects.requireNonNull(null, "input parameter is null");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void checkElementIndex() {
        char[] charArray = "checkElementIndex".toCharArray();
        // Preconditions.checkElementIndex(charArray.length, charArray.length, "index");
        // Preconditions.checkPositionIndex(charArray.length, charArray.length, "index");
        Preconditions.checkPositionIndexes(-1, charArray.length, charArray.length);
    }
}
