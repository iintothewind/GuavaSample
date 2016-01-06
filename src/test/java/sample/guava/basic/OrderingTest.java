package sample.guava.basic;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import sample.guava.bean.Person;

import java.util.Comparator;
import java.util.List;

public class OrderingTest {
    Logger log = LogManager.getLogger(this.getClass().getName());

    List<String> messages = ImmutableList.of("zoy", "zzz", "zzz", "abc", "ade", "bd", "bdf", "cde", "cf", "dfg", "asf",
            "asd", "aa", "aaa", "saf", "waq", "was", "xsd");
    List<Integer> ints = ImmutableList.of(9, 8, 7, 5, 4, 3, 2, 1, 6);

    Comparator<String> byLengthOrdering = new Comparator<String>() {
        @Override
        public int compare(String left, String right) {
            return Ints.compare(left.length(), right.length());
        }
    };

    @Test
    public void immutableSortedCopy() {
        this.log.debug("sortedCopy = " + Ordering.natural().immutableSortedCopy(ints));
    }

    @Test
    public void OrderByLengthOrdering() {
        this.log.debug("sortedCopy = " + Ordering.from(byLengthOrdering).sortedCopy(messages));
    }

    @Test
    public void OrderByLengthCompoundNatural() {
        // first order by length, if equals, order by natural secondly
        //this.logger.debug("sortedCopy = " + Ordering.from(byLengthOrdering).compound(Ordering.natural()).sortedCopy(messages));
    }

    @Test
    public void natural() {
        this.log.debug("sortedCopy = " + Ordering.natural().sortedCopy(messages));
    }

    @Test
    public void naturalReverse() {
        this.log.debug("sortedCopy = " + Ordering.natural().reverse().sortedCopy(messages));
    }

    @Test
    public void nullsLast() {
        this.log.debug("sortedCopy = "
                + Ordering.natural().nullsLast().sortedCopy(Lists.newArrayList("t", null, "e", "s", null, "t", null)));
        this.log.debug("sortedCopy = "
                + Ordering.natural().reverse().nullsLast()
                .sortedCopy(Lists.newArrayList("t", null, "e", "s", null, "t", null)));
    }

    @Test
    public void nullsFirst() {
        this.log.debug("sortedCopy = "
                + Ordering.natural().nullsFirst().sortedCopy(Lists.newArrayList("t", null, "e", "s", null, "t", null)));
        this.log.debug("sortedCopy = "
                + Ordering.natural().reverse().nullsFirst()
                .sortedCopy(Lists.newArrayList("t", null, "e", "s", null, "t", null)));
    }

    @Test
    public void usingToString() {
        Person studentJohn = new Person("John", 20, "Student");
        Person yongPeter = new Person("Peter", 20, "Student");
        Person olderPeter = new Person("Peter", 21, "Student");
        Person boxerJohn = new Person("John", 20, "Boxer");
        this.log.debug("sortedCopy = "
                + Ordering.usingToString()
                .sortedCopy(Lists.newArrayList(studentJohn, boxerJohn, yongPeter, olderPeter)));
    }

    @Test
    public void min() {
        this.log.debug("sortedCopy = " + Ordering.natural().min(1, 3, 2, 4));
        this.log.debug("sortedCopy = " + Ordering.natural().min(ints));
    }

    @Test
    public void max() {
        this.log.debug("sortedCopy = " + Ordering.natural().max("a", "c", "d"));
        this.log.debug("sortedCopy = " + Ordering.natural().max(ints));
    }

    @Test
    public void greatestOf() {
        this.log.debug("sortedCopy = " + Ordering.natural().greatestOf(ints, 3));
    }

    @Test
    public void leastOf() {
        this.log.debug("sortedCopy = " + Ordering.natural().leastOf(ints, 3));
    }

    @Test
    public void isOrdered() {
        this.log.debug("sortedCopy = " + Ordering.natural().isOrdered(Lists.newArrayList(1, 3, 2, 3)));
        this.log.debug("sortedCopy = " + Ordering.natural().isOrdered(Lists.newArrayList(1, 2, 3, 3)));
        this.log.debug("sortedCopy = " + Ordering.natural().isStrictlyOrdered(Lists.newArrayList(1, 2, 3, 3)));
    }

}
