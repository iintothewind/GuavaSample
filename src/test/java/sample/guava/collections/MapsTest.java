package sample.guava.collections;

import com.google.common.base.*;
import com.google.common.collect.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import sample.guava.bean.Color;
import sample.guava.bean.Person;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

public class MapsTest {
    Logger log = LogManager.getLogger(this.getClass().getName());

    private Person studentJohn = null;
    private Person yongPeter = null;
    private Person olderPeter = null;
    private Person boxerJohn = null;
    private HashSet<Person> set = null;

    @Before
    public void setUp() {
        studentJohn = new Person("John", 20, "Student");
        yongPeter = new Person("Peter", 21, "Student");
        olderPeter = new Person("Peter", 22, "Student");
        boxerJohn = new Person("John", 23, "Boxer");
        set = Sets.newHashSet(studentJohn, yongPeter, olderPeter, boxerJohn);
    }

    @Test
    public void newMap() {
        Map<String, String> hashMap = Maps.newHashMap();
        Map<String, String> identityHashMap = Maps.newIdentityHashMap();
        EnumMap<Color, String> map = Maps.newEnumMap(Color.class);
        Map<String, String> concurrentMap = Maps.newConcurrentMap();
        Map<String, String> linkedHashMap = Maps.newLinkedHashMap();
        Map<String, String> treeMap = Maps.newTreeMap();
    }

    @Test
    public void as() {
        this.log.debug("Converter<String, String> : "
                + Maps.asConverter(ImmutableBiMap.of("k1", "v1", "k2", "v2", "k3", "v3")));
        this.log.debug("Map<String, Person> : Maps.asMap() =="
                + Maps.asMap(Sets.newHashSet("John", "Peter"), new Function<String, Person>() {
            @Override
            public Person apply(final String input) {
                Preconditions.checkNotNull(input);
                return FluentIterable.from(set).firstMatch(new Predicate<Person>() {
                    @Override
                    public boolean apply(Person person) {
                        Preconditions.checkNotNull(person);
                        return input.equals(person.getName());
                    }
                }).get();
            }
        }));
    }

    @Test
    public void toMap() {
        this.log.debug("ImmutableMap<String, Person>  : Maps.toMap() =="
                + Maps.toMap(Sets.newHashSet("John", "Peter"), new Function<String, Person>() {
            @Override
            public Person apply(final String input) {
                Preconditions.checkNotNull(input);
                return FluentIterable.from(set).firstMatch(new Predicate<Person>() {
                    @Override
                    public boolean apply(Person person) {
                        Preconditions.checkNotNull(person);
                        return input.equals(person.getName());
                    }
                }).get();
            }
        }));
    }

    @Test
    public void uniqueIndex() {
        this.log.debug("ImmutableMap<String, Person>  : Maps.uniqueIndex() =="
                + Maps.uniqueIndex(set, new Function<Person, String>() {
            @Override
            public String apply(Person person) {
                Preconditions.checkNotNull(person);
                return Joiner.on("-").useForNull("null")
                        .join(person.getName(), person.getAge(), person.getOccupation());
            }
        }));
    }

    @Test
    public void difference() {
        Map<String, String> left = ImmutableMap.of("k1", "v1", "k2", "v2", "k3", "v3");
        Map<String, String> right = ImmutableMap.of("k2", "v2", "k3", "v3", "k4", "v4");
        this.log.debug("MapDifference<String, String> : Maps.difference() ==" + Maps.difference(left, right));
    }

    @Test
    public void differenceWithEquivalence() {
        Map<String, Person> left = ImmutableMap.of("John", studentJohn, "Peter", yongPeter);
        Map<String, Person> right = ImmutableMap.of("John", boxerJohn, "Peter", yongPeter);
        Equivalence<Person> equivalence = new Equivalence<Person>() {
            @Override
            protected boolean doEquivalent(Person left, Person right) {
                return Objects.equal(left, right);
            }

            @Override
            protected int doHash(Person person) {
                return Objects.hashCode(person);
            }

        };
        this.log.debug("MapDifference<String, String> : Maps.difference() =="
                + Maps.difference(left, right, equivalence));

    }

    @Test
    public void filterKeys() {
        Map<String, Person> personMap = ImmutableMap.of("John", studentJohn, "Peter", yongPeter);
        this.log.debug("Map<String, Person> : Maps.filterKeys() =="
                + Maps.filterKeys(personMap, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return "John".equals(input);
            }
        }));
    }

    @Test
    public void filterValues() {
        Map<String, Person> personMap = ImmutableMap.of("John", studentJohn, "Peter", yongPeter);
        this.log.debug("Map<String, Person> : Maps.filterKeys() =="
                + Maps.filterValues(personMap, new Predicate<Person>() {
            @Override
            public boolean apply(Person input) {
                return input != null && input.getAge() > 20;
            }
        }));
    }

    @Test
    public void filterEntries() {
        Map<String, Person> personMap = ImmutableMap.of("John", studentJohn, "Peter", yongPeter);
        this.log.debug("Map<String, Person>  : Maps.filterEntries() =="
                + Maps.filterEntries(personMap, new Predicate<Entry<String, Person>>() {
            @Override
            public boolean apply(Entry<String, Person> input) {
                String key = input.getKey();
                Person person = input.getValue();
                Preconditions.checkNotNull(key);
                Preconditions.checkNotNull(person);
                return Objects.equal(key, person.getName()) && person.getAge() > 20;
            }
        }));
    }

    @Test
    public void fromProperties() {
        this.log.debug("Map<String, String> : Maps.fromProperties() =="
                + Maps.fromProperties(System.getProperties()));
    }

    @Test
    public void transformValues() {
        Map<String, Person> personMap = ImmutableMap.of("John", studentJohn, "Peter", yongPeter);
        Map<String, String> transmation = Maps.transformValues(personMap, new Function<Person, String>() {
            @Override
            public String apply(Person input) {
                Preconditions.checkNotNull(input);
                return Joiner.on("-").useForNull("null").join(input.getName(), input.getAge(), input.getOccupation());
            }
        });
        this.log.debug("Map<String, String> : Maps.transformValues() == " + transmation);
    }

    @Test
    public void transformEntries() {
        Map<String, Person> personMap = ImmutableMap.of("John", studentJohn, "Peter", yongPeter);
        Map<String, String> transmation = Maps.transformEntries(personMap,
                new Maps.EntryTransformer<String, Person, String>() {
                    @Override
                    public String transformEntry(String key, Person person) {
                        Preconditions.checkNotNull(person);
                        return Joiner.on("-").useForNull("null")
                                .join(person.getName(), person.getAge(), person.getOccupation());
                    }
                });
        this.log.debug("Map<String, String> : Maps.transformEntries() == " + transmation);
    }

}
