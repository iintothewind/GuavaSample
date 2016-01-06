package sample.guava.collections;

import com.google.common.base.*;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sample.guava.bean.Person;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * FluentIterable is an implementation of Iterable interface, it is an
 * enhancement of Iterator
 */
public class FluentIterableTest {
    Logger log = LogManager.getLogger(this.getClass().getName());

    private Person studentJohn = null;
    private Person yongPeter = null;
    private Person olderPeter = null;
    private Person boxerJohn = null;
    private ArrayList<Person> list = null;

    @Before
    public void setUp() {
        studentJohn = new Person("John", 20, "Student");
        yongPeter = new Person("Peter", 20, "Student");
        olderPeter = new Person("Peter", 21, "Student");
        boxerJohn = new Person("John", 20, "Boxer");
        list = Lists.newArrayList(studentJohn, yongPeter, olderPeter, boxerJohn);
    }

    @Test
    public void filterByPredicate() {
        FluentIterable<Person> iterable = FluentIterable.from(list).filter(new Predicate<Person>() {
            @Override
            public boolean apply(Person input) {
                Preconditions.checkNotNull(input);
                return Optional.fromNullable(input.getAge()).or(0) > 21;
            }
        });

        Assert.assertFalse(iterable.contains(studentJohn));
        Assert.assertFalse(iterable.contains(studentJohn));
        Assert.assertEquals(Iterables.contains(iterable, olderPeter), false);
        Assert.assertFalse(Iterables.contains(iterable, boxerJohn));
    }

    @Test
    public void filterByType() {
        this.log.debug("from(list).filter(Person.class) == " + FluentIterable.from(list).filter(Person.class));
    }

    @Test
    public void testTransform() throws Exception {
        FluentIterable<String> iterable = FluentIterable.from(list).transform(new Function<Person, String>() {
            @Override
            public String apply(Person input) {
                Preconditions.checkNotNull(input);
                return Joiner.on("-").useForNull("null").join(input.getName(), input.getAge(), input.getOccupation());
            }
        });
        this.log.debug("FluentIterable<String>: from(list).transform() == " + iterable);
    }

    @Test
    public void first() {
        FluentIterable<Person> iterable = FluentIterable.from(list);
        Optional<Person> possible = iterable.first();
        this.log.debug("filtered.first().get() == " + possible.get());
    }

    @Test
    public void firstMatch() {
        FluentIterable<Person> iterable = FluentIterable.from(list);
        Optional<Person> possible = iterable.firstMatch(new Predicate<Person>() {
            @Override
            public boolean apply(Person input) {
                Preconditions.checkNotNull(input);
                return "Peter".equals(input.getName()) && "Student".equals(input.getOccupation());
            }
        });
        this.log.debug("filtered.first().get() == " + possible.get());
    }

    @Test
    public void last() {
        FluentIterable<Person> iterable = FluentIterable.from(list);
        Optional<Person> possible = iterable.last();
        this.log.debug("filtered.last().get() == " + possible.get());
    }

    @Test
    public void anyMatch() {
        FluentIterable<Person> iterable = FluentIterable.from(list);
        boolean isMatch = iterable.anyMatch(new Predicate<Person>() {
            @Override
            public boolean apply(Person input) {
                Preconditions.checkNotNull(input);
                return "Peter".equals(input.getName()) && "Boxer".equals(input.getOccupation());
            }
        });
        Assert.assertFalse(isMatch);
    }

    @Test
    public void allMatch() {
        FluentIterable<Person> iterable = FluentIterable.from(list);
        boolean isMatch = iterable.allMatch(new Predicate<Person>() {
            @Override
            public boolean apply(Person input) {
                Preconditions.checkNotNull(input);
                return Optional.fromNullable(input.getAge()).or(0) > 19;
            }
        });
        Assert.assertTrue(isMatch);
    }

    @Test
    public void cycle() {
        FluentIterable<Person> iterable = FluentIterable.from(list).cycle();
        // the iterator cycles indefinitely
        Iterator<Person> iterator = iterable.iterator();
        for (int i = 0; i < 20; i++) {
            this.log.debug("next() == " + iterator.next());
        }

        while (!iterable.isEmpty()) {
            list.remove(iterable.first().get());
        }
    }

    @Test
    public void get() {
        this.log.debug("Person: get(1) == " + FluentIterable.from(list).get(1));
    }

    @Test
    public void limit() {
        this.log.debug("Person: limit(3) == " + FluentIterable.from(list).limit(3));
    }

    @Test
    public void skip() {
        this.log.debug("Person: skip(2) == " + FluentIterable.from(list).skip(2));
    }
}
