package sample.guava.collections;

import com.google.common.base.*;
import com.google.common.collect.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import sample.guava.bean.Person;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MultimapsTest {
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

    @Test
    public void createMultimap() {
        ArrayListMultimap<String, String> arrayListMultimap = ArrayListMultimap.create();
        HashMultimap<String, String> hashMultimap = HashMultimap.create();
        LinkedHashMultimap<String, String> linkedHashMultimap = LinkedHashMultimap.create();
        LinkedListMultimap<String, String> linkedListMultimap = LinkedListMultimap.create();
        TreeMultimap<String, String> treeMultimap = TreeMultimap.create();
    }

    @Test
    public void asMap() {
        Map<String, List<String>> map = Multimaps.asMap(new ImmutableListMultimap.Builder<String, String>()
                .put("k1", "v1").put("k1", "v1").put("k1", "v2").put("k1", "v3").put("k2", "v1").build());
        this.log.debug("Map<String, List<String>> : Multimaps.asMap() == " + map);
    }

    @Test
    public void synchronizedListMultimap() {
        ImmutableListMultimap<String, String> immutableListMultimap = new ImmutableListMultimap.Builder<String, String>()
                .put("k1", "v1").put("k1", "v1").put("k1", "v2").put("k1", "v3").put("k2", "v1").build();
        this.log.debug("Map<String, List<String>> : Multimaps.asMap() == "
                + Multimaps.synchronizedListMultimap(immutableListMultimap));
    }

    @Test
    public void filterKeys() {
        ImmutableListMultimap<String, Person> immutableListMultimap = new ImmutableListMultimap.Builder<String, Person>()
                .put("Student", studentJohn).put("Student", yongPeter).put("Student", olderPeter)
                .put("Student", olderPeter).put("Boxer", boxerJohn).build();
        ListMultimap<String, Person> listMultimap = Multimaps.filterKeys(immutableListMultimap,
                new Predicate<String>() {
                    @Override
                    public boolean apply(String input) {
                        return Objects.equal("Boxer", input);
                    }
                });
        this.log.debug("ListMultimap<String, Person> : Multimaps.filterKeys() == " + listMultimap);
    }

    @Test
    public void filterValues() {
        ImmutableListMultimap<String, Person> immutableListMultimap = new ImmutableListMultimap.Builder<String, Person>()
                .put("Student", studentJohn).put("Student", yongPeter).put("Student", olderPeter)
                .put("Student", olderPeter).put("Boxer", boxerJohn).build();
        Multimap<String, Person> multimap = Multimaps.filterValues(immutableListMultimap, new Predicate<Person>() {
            @Override
            public boolean apply(Person input) {
                Preconditions.checkNotNull(input);
                return Objects.equal(input.getAge(), 22);
            }
        });
        this.log.debug("Multimap<String, Person> : Multimaps.filterValues() == " + multimap);
    }

    @Test
    public void filterEntries() {
        ImmutableListMultimap<String, Person> immutableListMultimap = new ImmutableListMultimap.Builder<String, Person>()
                .put("Student", studentJohn).put("Student", yongPeter).put("Student", olderPeter)
                .put("Student", olderPeter).put("Boxer", boxerJohn).build();
        Multimap<String, Person> multimap = Multimaps.filterEntries(immutableListMultimap,
                new Predicate<Entry<String, Person>>() {
                    @Override
                    public boolean apply(Entry<String, Person> input) {
                        Preconditions.checkNotNull(input);
                        String key = input.getKey();
                        Person person = input.getValue();
                        return Objects.equal(key, "Student") && Objects.equal(person.getAge(), 21);
                    }

                });
        this.log.debug("Multimap<String, Person> : Multimaps.filterEntries() == " + multimap);
    }

    @Test
    public void invertFrom() {
        ImmutableListMultimap<String, String> source = new ImmutableListMultimap.Builder<String, String>()
                .put("k1", "v1").put("k1", "v1").put("k1", "v2").put("k1", "v3").put("k2", "v1").build();
        Multimap<String, String> dest = ArrayListMultimap.create();
        Multimaps.invertFrom(source, dest);
        this.log.debug("Map<String, List<String>> : Multimaps.invertFrom() == " + dest);
    }

    @Test
    public void transformValues() {
        ImmutableListMultimap<String, Person> immutableListMultimap = new ImmutableListMultimap.Builder<String, Person>()
                .put("Student", studentJohn).put("Student", yongPeter).put("Student", olderPeter)
                .put("Student", olderPeter).put("Boxer", boxerJohn).build();
        ListMultimap<String, String> transformation = Multimaps.transformValues(immutableListMultimap,
                new Function<Person, String>() {
                    @Override
                    public String apply(Person person) {
                        Preconditions.checkNotNull(person);
                        return Joiner.on("-").useForNull("null")
                                .join(person.getName(), person.getAge(), person.getOccupation());
                    }
                });
        this.log.debug("ListMultimap<String, String> : Multimaps.transformValues() == " + transformation);
    }

    @Test
    public void transformEntries() {
        ImmutableListMultimap<String, Person> immutableListMultimap = new ImmutableListMultimap.Builder<String, Person>()
                .put("Student", studentJohn).put("Student", yongPeter).put("Student", olderPeter)
                .put("Student", olderPeter).put("Boxer", boxerJohn).build();
        ListMultimap<String, String> transformation = Multimaps.transformEntries(immutableListMultimap,
                new Maps.EntryTransformer<String, Person, String>() {
                    @Override
                    public String transformEntry(String key, Person person) {
                        Preconditions.checkNotNull(person);
                        return Joiner.on("-").useForNull("null")
                                .join(person.getName(), person.getAge(), person.getOccupation());
                    }
                });
        this.log.debug("ListMultimap<String, String> : Multimaps.transformEntries() == " + transformation);
    }

}
