package sample.guava.collections;

import com.google.common.base.Objects;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.PeekingIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.List;

public class IteratorsTest {
    Logger log = LogManager.getLogger(this.getClass().getName());

    @Test
    public void peekingIterator() {
        PeekingIterator<Integer> pi = Iterators.peekingIterator(Lists.newArrayList(1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2,
                2, 2, 2, 2, null, null, null, 4, null, null, null, 3, 3, 3, 3, 3, 3, 3, 3, 3, null, null, null, null,
                5, 5, 5, 5, 5, 5, 5, 5, 5, 5).iterator());
        List<Integer> removeDuplicationList = Lists.newArrayList();
        while (pi.hasNext()) {
            Integer element = pi.next();
            while (pi.hasNext() && Objects.equal(element, pi.peek())) {
                pi.next();
            }
            if (element != null) {
                removeDuplicationList.add(element);
            }
        }

        this.log.debug("removeDuplicationList == " + removeDuplicationList);
    }

}
