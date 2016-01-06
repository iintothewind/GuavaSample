package sample.guava.collections;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import sample.guava.bean.Person;

import java.util.ArrayList;

public class ListsTest {
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
    public void charactersOf() {
        this.log
                .debug("ImmutableList<Character>: Lists.charactersOf() = " + Lists.charactersOf("This is a string."));
    }

    @Test
    public void partition() {
        this.log.debug("List<List<Character>> : Lists.partition() = "
                + Lists.partition(Lists.charactersOf("This is a string."), 2));
    }

    @Test
    public void reverse() {
        this.log.debug("List<Character> : Lists.partition() = "
                + Lists.reverse(Lists.charactersOf("This is a string.")));
    }

    @Test
    public void transform() {
        this.log.debug("List<String> : Lists.transform() = " + Lists.transform(list, new Function<Person, String>() {
            @Override
            public String apply(Person input) {
                Preconditions.checkNotNull(input);
                return Joiner.on("-").useForNull("null").join(input.getName(), input.getAge(), input.getOccupation());
            }
        }));
    }


}
